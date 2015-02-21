package com.tekacs.combo

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.tekacs.combo.Combo.Topic
import dispatch.Defaults._
import dispatch._
import org.json4s.JValue
import org.json4s.JsonAST.JString
import org.json4s.jackson.JsonMethods._

import scala.util.{Failure, Success}

class TopicActor(targetActor: ActorRef, combo: Combo, topic: Combo.Topic) extends Actor with ActorLogging {
  import com.tekacs.combo.TopicActor._

  override def receive: Receive = {
    case First =>
      log.info(s"Subscribed to $topic")
      Http(url(combo.subscriptionBase(topic)).POST OK as.String).map(parse(_)).map {
        _ \ "subscription_id" match {
          case JString(str) => str
          case _ => ???
        }
      } onComplete {
        case Success(sub_id) => self ! Next(sub_id)
        case Failure(ex) => throw ex
      }
    case Next(sub_id) =>
      Http(url(combo.subscriptionNext(topic, sub_id)) OK as.String)
        .map(parse(_))
        .map((msg) => targetActor ! Fact(topic, msg))
        .andThen { case _ => self ! Next(sub_id) }
  }
}
object TopicActor {
  case object First
  case class Next(sub_id: String)

  case class Fact(topic: Topic, data: JValue)
}
