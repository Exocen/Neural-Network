package fr.cristal.emeraude.n2s3.core.exceptions

/**
  * Exceptions related to the Logging feature
  * @author wgouzer & qbailleul
  */

class LoggingException extends Exception

/**
  * Exception thrown when the file descriptor is not yet open
  * @param msg the message you want to associate with the exception
  */
case class CantOpenLogFileException(msg: String) extends LoggingException
case class CantWriteLogFileException(msg: String) extends LoggingException
case class CantCloseLogFileException(msg: String) extends LoggingException
