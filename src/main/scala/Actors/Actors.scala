package Actors

import akka.actor.{Actor, ActorSystem, Props}

object Actors extends App {
  // This code demonstrates the basic setup and usage of actors in Akka

  // 1. Setup Actor System
  // An ActorSystem is a container that manages the lifecycle of actors
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name) // firstActorSystem

  // 2. Create actors
  // Word count actor
  class WordCountActor extends Actor {
    // Internal Data
    // It serves as a simple example of how actors can maintain and
    // update internal state based on the messages they receive.
    var totalWords = 0

    // Behavior - is a functional interface that defines how an actor handles messages.
    def receive: Receive = { // type Receive = PartialFunction[Any, Unit] i.e It takes Any type but only return Unit
      case message: String =>
        println(s"I have received a message: $message")
        totalWords += message.split(" ").length
        println(s"Total words of ${self.path.name} is $totalWords")

      case msg => println(s"I cannot understand ${msg.toString}")
    }
  }

  // 3. Instantiate our actor
  var wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  var anotherWordCounter = actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")

  // 4. Communicate
  // Using infix notation: wordCounter.!("I am learn...")
  // !: The tell method, used to send an asynchronous message.
  wordCounter ! "I am learning Akka and it's pretty cool!"
  anotherWordCounter ! "This is the second message"
  anotherWordCounter ! 100

  // I have received a message:I am learning akka and its pretty cool
  // I have received a message:This is the second message
  // Total words of wordCounter is 8
  // Total words of anotherWordCounter is 5
  // I cannot understand 100


  // -------------------------------Second Example-------------------------------------------


  // Creating an actor
  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"Hi, my name is $name")
      case _ =>
    }
  }

  // Creating Props for instantiation of the actor (This method is used in the below code)
  object Person {
    def props(name: String) = Props(new Person(name))
  }

  // Instantiate our Person Actor
  val person = actorSystem.actorOf(Person.props("Rizwan")) // using the object Person method here
  person ! "hi" // Hi, my name is Rizwan
}