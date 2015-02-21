package com.tekacs.combo

object Main extends App {
  import com.tekacs.combo.Context._

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
