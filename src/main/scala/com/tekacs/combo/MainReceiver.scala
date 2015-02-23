package com.tekacs.combo

import akka.actor.{Actor, ActorLogging}
import org.json4s.jackson.JsonMethods._

class MainReceiver extends Actor with ActorLogging {
  override def receive = {
    case Combo.Fact(topic, data) =>
      println(s"$topic: ${compact(render(data))}")
  }
}
