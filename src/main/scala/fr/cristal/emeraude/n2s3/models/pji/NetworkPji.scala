package fr.cristal.emeraude.n2s3.models.pji

import scala.util.Random
import akka.actor.ActorRef
import fr.cristal.emeraude.n2s3.corenetwork.Network
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.Synapse
import fr.cristal.emeraude.n2s3.corenetwork.Input

/**
  * TODO this scaladoc
  * @author wgouzer & qbailleul
  */
class NetworkPji(
  nbNPL: Seq[Int],
  threshold: Double = 8) extends Network {

  override val nbNeuronPerLayer = nbNPL

  /**
    * Function for demoing the application only
    * Send a message to the first Neuron in the first layer
    * @param v spike (that's the message)
    */
  override def firingSpikes() {
    val v: Double = 10.0
    neuronsActorRef(0) map { _ ! Input(v) }
  }

  /**
    * TODO : this scaladoc
    */
  def createNeuron(layer: Int): Neuron = {
    new NeuronPji(layer, this.threshold)
  }

  /**
    * TODO : this scaladoc
    */
  def createSynapse(pre: ActorRef, post: ActorRef): Synapse = {
    val rand = new Random()
    new SynapsePji(rand.nextDouble() * 10, pre, post)
  }

}
