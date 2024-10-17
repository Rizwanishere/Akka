package part3testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike
import scala.util.Random

// TestKit initializes ActorSystem and provides testing utilities
class BasicSpec extends TestKit(ActorSystem("BasicSpec"))
  with ImplicitSender // Automatically sets the test actor as the sender
  with AnyWordSpecLike // For BDD-style testing with `should`/`in`
  with BeforeAndAfterAll { // Setup and teardown

  // Shutdown ActorSystem after tests
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import BasicSpec._

  "a simple actor" should {
    "send back the same message" in {
      val echoActor = system.actorOf(Props[SimpleActor]) // Create actor
      echoActor ! "Hello, sup this is a test message" // Send message
      expectMsg("Hello, sup this is a test message") // Expect same message back
    }
  }

  "a black hole actor" should {
    "consume message without reply" in {
      val blackhole = system.actorOf(Props[Blackhole])
      blackhole ! "Another test message"
      expectNoMessage() // No response expected
    }
  }

  "a lab test actor" should {
    val labTestActor = system.actorOf(Props[LabTestActor])

    "turn a string to uppercase" in {
      labTestActor ! "I Love Akka"
      val reply = expectMsgType[String] // Expect a string message
      assert(reply == "I LOVE AKKA") // Assert uppercase transformation
    }

    "reply with either 'hello' or 'hi'" in {
      labTestActor ! "greeting"
      expectMsgAnyOf("hello", "hi") // Expect one of these messages
    }

    "reply with both 'Scala' and 'Akka'" in {
      labTestActor ! "favoriteTech"
      expectMsgAllOf("Scala", "Akka") // Expect both messages
    }
  }
}

// Companion object to hold actor definitions
object BasicSpec {

  // Actor that sends back any received message
  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message => sender() ! message
    }
  }

  // Actor that consumes messages and never replies
  class Blackhole extends Actor {
    override def receive: Receive = Actor.emptyBehavior
  }

  // Actor that replies with specific behaviors
  class LabTestActor extends Actor {
    val random = new Random()

    override def receive: Receive = {
      case "greeting" =>
        // Randomly sends "hi" or "hello"
        if (random.nextBoolean()) sender() ! "hi" else sender() ! "hello"
      case "favoriteTech" =>
        // Sends both "Scala" and "Akka"
        sender() ! "Scala"
        sender() ! "Akka"
      case message: String =>
        // Converts message to uppercase and sends back
        sender() ! message.toUpperCase()
    }
  }
}
