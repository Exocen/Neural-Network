package fr.cristal.emeraude.n2s3.corenetwork.exceptions

/**
 * @author wgouzer & qbailleul
 */

/**
 * Exception thrown when a neuron receives an unknown message
 * @param msg the message you want to associate with the exception
 */
case class UnknownMessageNeuronException(msg: String) extends Exception(msg)
