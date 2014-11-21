package fr.cristal.emeraude.n2s3.features.io

import fr.cristal.emeraude.n2s3.UnitSpec

class TestEventInputStream extends UnitSpec {
  val filename = "data/aerdata/MovingDarkBox.aedat"

  "The EventInputStream.readEventsByNumber(n) method" should "return n events" in {
    val eis = new EventInputStream(filename)
    val events = eis.readEventsByNumber(20)
    println(events)
    assert(events.length === 20)
  }

  "The EventInputStream.readAllEvents method" should "read all events" in {
    val eis = new EventInputStream(filename)
    val events = eis.readAllEvents()
    assert(eis.size === events.size)
  }
}
