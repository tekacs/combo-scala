package com.tekacs.combo

import akka.actor.{Actor, ActorLogging, Props}
import com.tekacs.combo.Combo.Fact

import scala.concurrent.duration._

object Main extends App {
  import com.tekacs.combo.Context._

  val actors = topics.map(topic => combo.subscribe(Combo.Topic.fromString(topic), mainActor))

  class TempActor extends Actor with ActorLogging {
    def receive = {
      case Fact(topic, data) =>
        combo.add(topic, data)
    }
  }
  import scala.concurrent.ExecutionContext.Implicits.global
  application.scheduler.schedule(
    0.seconds,
    1.millisecond,
    application.actorOf(Props[TempActor]),
    new Fact("test", s"""{"tick": ${System.nanoTime}}""")
  )

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run() = {
      comboSystem.shutdown()
      application.shutdown()
    }
  }))

  application.awaitTermination()
}
