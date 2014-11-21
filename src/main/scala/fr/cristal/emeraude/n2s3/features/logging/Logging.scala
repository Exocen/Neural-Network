package fr.cristal.emeraude.n2s3.features.logging

import java.io.FileOutputStream

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import fr.cristal.emeraude.n2s3.core._
import fr.cristal.emeraude.n2s3.core.exceptions.{CantCloseLogFileException, CantWriteLogFileException}

/**
  * TODO THIS SCALADOC
  */
class Logging(filename: String) extends Actor {
  val log = akka.event.Logging(context.system, this)
  val fos: Option[FileOutputStream] = Some(new FileOutputStream(filename))

  def writeToLogFile(datalog: String) = {
    val data = datalog.map(_.toByte).toArray
    lazy val msg = "The FileOutputStream is probably not open yet, Open the LogFile first !"
    fos.getOrElse(throw new CantWriteLogFileException(msg)).write(data)
  }

  def closeLogFile() = {
    lazy val msg = "The FileOutputStream is probably not open yet, Open the LogFile first !"
    fos.getOrElse(throw new CantCloseLogFileException(msg)).close()
  }

  def printToStdout(datalog: String) = println(datalog)

  def receive = {
    case WriteToLogFile(datalog) => writeToLogFile(datalog)
    case CloseLogFile => closeLogFile()
    case PrintToStdout(datalog) => printToStdout(datalog)
  }
}

object Logging {
  var path = ""

  def log(logname: String, system: ActorSystem): ActorRef = {
    val pathname = path + "/" + logname
    new java.io.File(path + "/").mkdirs() // create the directory's tree
    system.actorOf(Props(new Logging(pathname)), name = logname)
  }
}
