package fr.cristal.emeraude.n2s3.features.io

import net.sf.jaer.aemonitor.AEPacketRaw
import net.sf.jaer.eventio.AEFileInputStream

/**
 * Reads AER files using jAER (jar file in lib directory).
 * All the methods return an IndexedSeq[Event]. The timestamps are
 * normalized to start at 0.
 *
 * @author boulet
 * @param filename: String, the name of the file to read from
 */
class EventInputStream(filename: String) {
  private val aeis = new AEFileInputStream(new java.io.File(filename))
  val startTime = aeis.getFirstTimestamp
  val size = aeis.size()

  /**
   * @param p: AEPacketRaw, the packet of raw events read from the file
   * @return an IndexedSeq of Events
   */
  private def eventsFromAEPacket(p: AEPacketRaw) =
    for (
      i <- 0 to p.getNumEvents - 1;
      r = p.getEvent(i); // has to be done in the for loop
      e = Event(r.address, r.timestamp - startTime) // because getEvent always points to the same memory area
    ) yield e

  /**
   * @param n, the number of events to read
   * @return sequence of events
   */
  def readEventsByNumber(n: Int) = eventsFromAEPacket(aeis.readPacketByNumber(n))

  /**
   * @param dt, the time interval in µs to read events from the last read event
   * @return sequence of events
   */
  def readEventsByTime(dt: Int) = eventsFromAEPacket(aeis.readPacketByTime(dt))

  /**
   * @return Sequence of all the events in the file.
   */
  def readAllEvents() = eventsFromAEPacket(aeis.readPacketByNumber(aeis.size().toInt))
}