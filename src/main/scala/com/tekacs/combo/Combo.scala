package com.tekacs.combo

import akka.actor.{ActorRef, ActorSystem, Props}
import dispatch.Defaults._
import dispatch._
import org.json4s.JValue
import org.json4s.JsonAST.{JString, JArray}
import org.json4s.jackson.JsonMethods._

object Combo {
  case class Config(host: String, port: Option[Int], scheme: Option[String])
  case class Topic(path: String*) {
    override def toString = path.mkString("/")
  }
  object Topic {
    def fromString(string: String): Topic = Topic(string.split("/"): _*)
  }
}
class Combo(val config: Combo.Config, val actorSystem: ActorSystem) {
  import com.tekacs.combo.Combo._

  val scheme = config.scheme.getOrElse("http")
  val port = config.port.getOrElse(if (scheme == "http") 80 else 443)

  val basePath = s"$scheme://${config.host}:$port"
  val topicBase = s"$basePath/topics"

  def factsPath(topic: Topic) = s"$topicBase/$topic/facts"
  def subscriptionBase(topic: Topic) = s"$topicBase/$topic/subscriptions"
  def subscriptionNext(topic: Topic, sub_id: String) = s"${subscriptionBase(topic)}/$sub_id/next"

  def topics: Future[List[String]] = {
    Http(url(topicBase) OK as.String).map(parse(_)).map {
      case JArray(xs) => xs.map {
        case JString(str) => str
        case _ => ???
      }
      case _ => ???
    }
  }

  def facts(topic: Combo.Topic): Future[List[JValue]] = {
    Http(url(factsPath(topic)) OK as.String).map(parse(_)).map {
      case JArray(xs) => xs
      case _ => ???
    }
  }

  def subscribe(topic: Combo.Topic, targetActor: ActorRef): ActorRef = {
    val actor = actorSystem.actorOf(Props.create(classOf[TopicActor], targetActor, this, topic))
    actor ! TopicActor.First
    actor
  }

  def add(topic: Combo.Topic, data: JValue): Future[Throwable] = {
    val request = url(factsPath(topic)).POST
    request << compact(render(data))
    Http(request OK as.String).failed
  }
}
