package com.tekacs.combo

import akka.actor.{Actor, ActorLogging}

class MainReceiver extends Actor with ActorLogging {
  override def receive: Receive = {
    case TopicActor.Fact(topic, None) =>
      println(s"$topic: nothing new")
    case TopicActor.Fact(topic, Some(data)) =>
      println(s"$topic: $data")
  }
}
