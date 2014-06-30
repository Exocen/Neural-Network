package fr.cristal.emeraude.n2s3.corenetwork.exceptions

/**
 * @author wgouzer & qbailleul
 */

/**
 * Exception thrown when a synapse receives an unknown message
 * @param msg the message you want to associate with the exception
 */
case class UnknownMessageSynapseException(msg: String) extends Exception(msg)
