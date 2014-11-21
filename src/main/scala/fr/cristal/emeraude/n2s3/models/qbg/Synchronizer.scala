package fr.cristal.emeraude.n2s3.models.qbg

import akka.actor.ActorRef
import fr.cristal.emeraude.n2s3.core
import fr.cristal.emeraude.n2s3.core.{Spike, WriteToLogFile}


class Synchronizer(logger: ActorRef) extends core.Synchronizer {

  override def process(s: Spike): Unit = s match {
    case s: Spike =>

      s match {
        case PreSynapticSpike(t, d) => logger !
          WriteToLogFile("%s %s %s".format("PreSynapticSpike", t, d))
        case PostSynapticBackwardSpike(t, d, se) => logger !
          WriteToLogFile("%s %s %s %s".format("PostSynapticBackwardSpike", t, d, se))
        case PostSynapticForwardSpike(t, d) => logger !
          WriteToLogFile("%s %s %s".format("PostSynapticForwardSpike", t, d))
      }

      super.process(s)
  }
}
