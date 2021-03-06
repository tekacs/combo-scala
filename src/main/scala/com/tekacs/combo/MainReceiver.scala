package com.tekacs.combo

import akka.actor.{Actor, ActorLogging}

class MainReceiver extends Actor with ActorLogging {
  override def receive: Receive = {
    case Combo.Fact(topic, data) =>
      println(s"$topic: $data")
  }
}
