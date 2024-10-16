package Streams

import akka.actor.ActorSystem
import akka.stream.{OverflowStrategy}
import akka.stream.scaladsl.{Flow, Sink, Source}
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

/**
 * Demonstrates Backpressure and Overflow Strategies in Akka Streams
 */
object BackPressureBasics extends App {

  // Initialize ActorSystem and ExecutionContext for Akka Streams
  implicit val system: ActorSystem = ActorSystem("BackPressureSystem")
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

  // Define a fast Source that produces integers from 1 to 1000
  val fastSource = Source(1 to 1000)

  // Define a slow Sink that processes each element with a 1-second delay
  val slowSink = Sink.foreach[Int] { x =>
    // Simulate slow processing for the Sink by adding a delay
    Thread.sleep(1000)
    println(s"Sink processed: $x")
  }

  /**
   * Basic Flow without buffering - demonstrating backpressure
   * Each element is printed, then incremented by 1 and passed to the sink
   */
  val simpleFlow = Flow[Int].map { x =>
    println(s"Flow received: $x")
    x + 1 // Increment each element
  }

  // Connect fastSource to slowSink via simpleFlow and apply backpressure using async boundaries
  // fastSource.async.to(slowSink).run() // Without flow, simple source-to-sink backpressure (uncomment to try)

  // Uncomment to try basic flow with async to apply backpressure
  // fastSource.via(simpleFlow).async.to(slowSink).async.run()

  /**
   * Buffered Flow with OverflowStrategy - controls what happens when buffer fills up
   * Buffer size = 10, strategy = dropHead (drop the oldest elements in the buffer when full)
   */
  val bufferedFlow = simpleFlow.buffer(10, overflowStrategy = OverflowStrategy.dropHead)

  // Run the stream: Fast source, flow with buffer, slow sink
  fastSource.async
    .via(bufferedFlow).async
    .to(slowSink)
    .run()

  /*
   * Explanation of output:
   * - The source produces elements rapidly.
   * - The buffer allows up to 10 elements to be held before applying the overflow strategy.
   * - When the buffer is full, the oldest element is dropped (dropHead strategy).
   * - The sink processes elements slowly, one per second.
   * - You will see the flow processing elements, then some of the oldest elements will be dropped.
   * - Only the latest 10 elements will reach the sink in the end.
   */

  /**
   * Overflow strategies supported by Akka Streams:
   * - dropHead: Drop the oldest element in the buffer
   * - dropTail: Drop the newest element in the buffer
   * - dropNew: Drop the incoming element (the element that caused overflow)
   * - dropBuffer: Drop the entire buffer
   * - backpressure: Signal backpressure to slow down the source
   * - fail: Fail the stream if overflow occurs
   */

  // Example of throttling the source: Limits the rate of elements produced (10 elements per second)
  //  fastSource.throttle(10, 1.second).runWith(Sink.foreach(println))

  /*
   * Output Summary:
   * - For the first 10-16 elements, the buffer will hold them until the sink catches up.
   * - As more elements are produced, the buffer fills up and starts dropping the oldest.
   * - You will observe that elements such as 1, 2, etc., will be dropped.
   * - The sink will eventually process only the latest 10 elements from 991 to 1000.
   */
}
