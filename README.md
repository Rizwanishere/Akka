# Akka Overview

Akka is a powerful toolkit and runtime for building highly concurrent, distributed, and fault-tolerant systems in Java and Scala. It simplifies the development of systems that scale across multiple processors and machines. Akka is particularly known for its actor-based model of concurrency, which abstracts away low-level thread management and allows developers to focus on high-level application logic.

## Key Features of Akka:

### 1. Actor Model
- Akka uses the actor model to manage concurrency.
- Actors are lightweight, isolated entities that communicate through asynchronous message passing.
- This helps avoid shared state and minimizes the chances of race conditions.

### 2. Concurrency and Parallelism
- Actors in Akka can run in parallel without any explicit thread management from the developer.
- Each actor handles messages asynchronously, allowing for scalable, non-blocking applications.

### 3. Fault Tolerance
- Akka supports supervision strategies where actors can supervise child actors and restart them in case of failure.
- This helps create resilient systems that can recover from errors automatically.

### 4. Distributed Computing
- Akka allows you to build distributed systems where actors can communicate across network boundaries.
- It abstracts the complexity of network protocols, making it easier to develop systems that span multiple nodes.

### 5. Clustered Systems
- Akka clustering enables multiple nodes to form a cluster, offering features like node discovery, partitioning, and state replication across nodes.

### 6. Akka Streams
- Tools for processing streams of data asynchronously and in a non-blocking way, useful for reactive data processing pipelines.

### 7. Akka HTTP
- Akka includes a powerful HTTP server and client for building RESTful services and APIs.

## Use Cases
Akka is frequently used in applications that require high availability, scalability, and low-latency communication, such as:
- Microservices
- IoT systems
- Real-time processing systems

## Akka Toolkit Components:

- **Akka Actors**: For building concurrent and scalable applications using the actor model.
- **Akka Streams**: For processing and handling large streams of data in a reactive and non-blocking manner.
- **Akka HTTP**: For creating RESTful web services and clients.
- **Akka Cluster**: For building distributed and clustered systems.
- **Akka Persistence**: For managing state and enabling event sourcing in actor-based applications.

# Akka Project Setup

## Step 1: Create New Project
- Use Java 21, Scala & sbt to create the project.

### Example `build.sbt`:
```sbt
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "Akka-actors"
  )

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.20"
```

## Step 2: Create Playground.scala
#### Path: src/main/scala/Playground/Playground.scala
```scala
package Playground

import akka.actor.ActorSystem

object Playground extends App {
  val actorSystem = ActorSystem("HelloAkka")
  println(actorSystem.name) // Output: HelloAkka
}

```