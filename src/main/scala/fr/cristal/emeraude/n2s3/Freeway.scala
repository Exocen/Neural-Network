package fr.cristal.emeraude.n2s3

import fr.cristal.emeraude.n2s3.features.io.EventInputStream

object Freeway extends App {
  val filename = "data/aerdata/freeway.mat.dat"
  val reader = new EventInputStream(filename)

  println(reader.size)

  for (i <- 0 to reader.size.toInt) {
    println(reader.readEventsByTime(100))
    Thread.sleep(2000)
  }
}
