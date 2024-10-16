package Actors

import akka.actor.{Actor, ActorSystem, Props}

object UnBecome {

  class moodActor extends Actor {

    override def receive: Receive = happy

    def happy: Receive = {
      case "How are you" => println("I am happy")
      case "mood change" => context.become(sad)
      case "revert" =>
        context.unbecome()
        println("Reverting back to previous mood")
    }

    def sad: Receive = {
      case "How are you" => println("I am sad")
      case "revert" =>
        context.unbecome()
        println("Reverting the mood to Happy :)")
    }
  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MyActorSystem")
    val actor = system.actorOf(Props[moodActor], "MoodActor")

    actor ! "How are you" // I am happy
    actor ! "mood change"

    actor ! "How are you" // I am sad
    actor ! "revert" // Reverting the mood to Happy :)

    actor ! "How are you" // I am happy
  }
}

/**
 * Explanation:
 * The actor starts with the happy behavior.
 * When it receives "change mood", it switches to the sad behavior.
 * If it receives "revert", it uses unbecome to go back to the previous behavior (in this case, happy).
 * This shows how unbecome helps the actor return to its previous state before a behavior change.
 * */

/**
 * WHAT IS BECOME AND UNBECOME METHOD??
 * In Akka, become and unbecome allow an actor to change its behavior dynamically during runtime.
 *
 * become: Changes the actor's behavior to a new set of message handlers. This means the actor starts handling messages differently.
 * unbecome: Reverts the actor's behavior back to what it was before the last become call.
 * This is useful when the actor needs to respond differently based on its state. You can switch between different behaviors without creating new actors or stopping the current one.
 * */