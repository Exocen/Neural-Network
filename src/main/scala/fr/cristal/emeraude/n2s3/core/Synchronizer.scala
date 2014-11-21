package fr.cristal.emeraude.n2s3.core

import akka.actor.Actor

class Synchronizer extends Actor {

  val queue = scala.collection.mutable.PriorityQueue[Spike]()(Ordering.by(_.timestamp))
  var shouldIStart = true

  /**
    * Hook to be extended to enable observation.
    */
  def process(s: Spike): Unit = s match {
    case s: Spike =>
      if (queue.isEmpty) {
        register(s)
        sender ! SpikeAck
        if (shouldIStart) {
          self ! Done
          shouldIStart = false
        }
      } else {
        register(s)
        sender ! SpikeAck
      }
  }

  def register(s: Spike) = {
    process(s)
    queue.enqueue(s)
  }

  def receive = {

    case s: Spike => process(s)

    case Done =>
      if (queue.isEmpty) { shouldIStart = true }
      else {
        val s = queue.dequeue()
        s.destination ! s
      }

    case _ => throw new Exception("Unknown message in receive of Synchronizer")

  }
}
