package fr.cristal.emeraude.n2s3.models.timemodel

import akka.actor._
import fr.cristal.emeraude.n2s3.corenetwork.Spike

/**
 * Simulate a post-synaptic spike, a simple spike before all the computing
 * done on it
 */
case class SpikeBackward(
  v: Double) extends Spike

/**
 * Simulate a pre-synaptic spike, a simple spike altered by the computing
 * done on it
 */
case class SpikeForward(v: Double) extends Spike
