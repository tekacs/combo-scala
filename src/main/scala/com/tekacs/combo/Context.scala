package com.tekacs.combo

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus
import ficus.Ficus._
import ficus.readers.ArbitraryTypeReader._

object Context {
  val comboSystem = ActorSystem("combo")
  val application = ActorSystem("application")

  val pack = getClass.getPackage
  val (name, version) = (pack.getImplementationTitle, pack.getImplementationVersion)

  val config = ConfigFactory.load()
  val comboConfig = config.as[Combo.Config]("combo")

  val combo = new Combo(comboConfig, comboSystem)
  val topics = Context.config.as[List[String]]("combo.topics")

  val mainActor = application.actorOf(Props[MainReceiver])
}
