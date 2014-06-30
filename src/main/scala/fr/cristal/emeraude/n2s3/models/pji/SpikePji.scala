package fr.cristal.emeraude.n2s3.models.pji

import akka.actor.ActorRef
import fr.cristal.emeraude.n2s3.corenetwork.Spike

/**
 * Simulate a pre-synaptic spike, a simple spike altered by the computing
 * done on it
 */
case class SpikeForward(
  v: Double) extends Spike

/**
 * Simulate a post-synaptic spike, a simple spike before all the computing
 * done on it
 */
case class SpikeBackward(
  v: Double) extends Spike

/**
 * This message is present only for "demoing" the simulation to people
 */
case class addMessage(immune: ActorRef) extends Spike
