package fr.cristal.emeraude.n2s3.models.qbg

import akka.actor.ActorRef
import fr.cristal.emeraude.n2s3.core
import fr.cristal.emeraude.n2s3.core.exceptions.UnknownMessageSynapseException
import fr.cristal.emeraude.n2s3.core.{Spike, WriteToLogFile}

/**
 * Synapses are the link between two Neurons.
 *
 * @constructor create a new Synapse in a form of an actor with a weight, a parent
 *              and a child
 *
 * @author wgouzer & qbailleul
 */
class Synapse(
               pre: ActorRef,
               post: ActorRef,
               synchronizer: ActorRef,
               logger: ActorRef
               ) extends core.Synapse(pre, post, synchronizer) {

  //Gmin
  val delay = 2
  val stdpWindow = 5
  var g = 0.5
  var queuePost = scala.collection.mutable.PriorityQueue[Spike]()(Ordering.by(_.timestamp))
  var queuePre = scala.collection.mutable.PriorityQueue[Spike]()(Ordering.by(_.timestamp))

  /**
   * Behavior of the synapse.
   * @see fr.cristal.emeraude.n2s3.core.Synapse#receive
   */
  override def processSpike(s: Spike) = s match {

    case PreSynapticSpike(t, dest) =>
      queuePre.enqueue(PostSynapticForwardSpike(t, dest))
      fire(PostSynapticForwardSpike(t + delay, dest))

    case PostSynapticBackwardSpike(t, dest, send) =>
      queuePost = queuePost filter (w => t - w.timestamp <= stdpWindow)
      queuePost foreach { _ =>
        weight_modifier(t)
      }
      done()

    case _ ⇒ throw UnknownMessageSynapseException(
      "Synapse received an unknown message, don't know what to do")
  }

  /**
   * simple formula to test this simulation
   */
  def weight_modifier(timestamp: Int) = {
    // mahyar's formula
    logger ! WriteToLogFile("%s %s %s %s".format(self, timestamp + delay, g, stdpWindow)) // send weight value
    System.out.println("%s %s".format(g.toString, (timestamp + delay).toString))

    fire(PostSynapticForwardSpike(timestamp + delay, post))
  }
}
