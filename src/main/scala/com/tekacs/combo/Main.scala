package com.tekacs.combo

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus
import ficus.Ficus._
import ficus.readers.ArbitraryTypeReader._

object Main extends App {
  val comboSystem = ActorSystem("combo")
  val application = ActorSystem("application")

  val pack = getClass.getPackage
  val (name, version) = (pack.getImplementationTitle, pack.getImplementationVersion)

  val config = ConfigFactory.load()
  val comboConfig = config.as[Combo.Config]("combo")

  val combo = new Combo(comboConfig, comboSystem)
  val mainActor = application.actorOf(Props[MainReceiver])

  val topics = config.as[List[String]]("combo.topics")
  for (topic <- topics) {
    combo.subscribe(Combo.Topic.fromString(topic), mainActor)
  }

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run() = {
      comboSystem.shutdown()
      application.shutdown()
    }
  }))

  application.awaitTermination()
}
