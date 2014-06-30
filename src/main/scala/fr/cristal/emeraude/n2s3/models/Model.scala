package fr.cristal.emeraude.n2s3.models

import akka.actor._
import fr.cristal.emeraude.n2s3.corenetwork.Neuron
import fr.cristal.emeraude.n2s3.corenetwork.Synapse

/**
 * TODO : this scaladoc
 * @author wgouzer & qbailleul
 */
trait Model {
  // TODO : this scaladoc
  def createNeuron(layer: Int): Neuron

  // TODO : this scaladoc + check ordre des parametres
  def createSynapse(parent: ActorRef, child: ActorRef): Synapse
}
