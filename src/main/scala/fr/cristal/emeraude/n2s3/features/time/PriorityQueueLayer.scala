package fr.cristal.emeraude.n2s3.features.time

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.event.Logging
import fr.cristal.emeraude.n2s3.corenetwork.Welcome

/**
 * TODO THIS SCALADOC
 * @author wgouzer & qbailleul
 */
class PriorityQueueLayer(
  layer: Int) extends Actor {

  val log = Logging(context.system, this)
  var waitingline = scala.collection.mutable.Seq[Any]() //TODO: Corriger le Any

  def processFW(v: Double, timestamp: Long, dest: ActorRef) = {
    sender ! ack()
    if (waitingline.isEmpty) {
      waitingline = waitingline :+ ForwardPQL(v, timestamp, dest)
      self ! done()
    } else {
      waitingline = waitingline :+ ForwardPQL(v, timestamp, dest)
    }
  }

  def processBW(v: Double, timestamp: Long, dest: ActorRef) = {
    sender ! ack()
    if (waitingline.isEmpty) {
      waitingline = waitingline :+ BackwardPQL(v, timestamp, dest)
      self ! done()
    } else {
      waitingline = waitingline :+ BackwardPQL(v, timestamp, dest)
    }
  }

  def processDone = {

    if (!waitingline.isEmpty) {

      waitingline(0) match {

        case ForwardPQL(v: Double, timestamp: Long, dest: ActorRef) =>
          dest ! SpikeForward(v, timestamp)
          waitingline = waitingline.tail

        case BackwardPQL(v: Double, timestamp: Long, dest: ActorRef) =>
          dest ! SpikeBackward(v, timestamp)
          waitingline = waitingline.tail

        /**
         * TODO :mettre une exception
         */
        case _ => println("wtf man ?")

      }
    } //TODO enlever si useless
    else { println("PQL : Stop") }
  }

  /**
   * TODO: Scaladoc
   */
  def receive = {
    case ForwardPQL(v: Double, timestamp: Long, dest: ActorRef) =>
      println("PQ : reception FPQL")
      processFW(v, timestamp, dest)

    case BackwardPQL(v: Double, timestamp: Long, dest: ActorRef) =>
      println("PQ : reception BPQL")
      processBW(v, timestamp, dest)

    case done() => processDone

    case Welcome => // debug purposes
  }

}
