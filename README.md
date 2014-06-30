# PJI

## TODO / PROGRESS
* ~~Restructuration du code pour une meilleur généricité (extensibilité)~~
   * ~~Synchronisation à la fin de la liaison neuron/synapses (send ?
      message `[Future]`)~~
* ~~Correction d'un bug majeur qui ne créait pas correctement les synapses~~
   * ~~Arcs créés automatiquements : changement d'une Seq(neurone, couche) à un
     tableau[couche][indiceNeurone]~~
   * ~~Arcs créés manuellements : changement d'une Seq(Int, Int) à (Int, Int, Int, Int) pour
     désigner (couche_1, indiceNeurone_1, couche_2, indiceNeurone_2)~~
* `[In progress]` Ajouter du monitoring dans le code (modification du poids etc...) et pouvoir
     écrire la sortie dans un fichier
* `[In progress]` Modèle de temps (information de temps sur le spike `[Double]`)
   * Neuron / Synapse, mémoriser les derniers événements (derniers spikes lors d'un intervalle de
  temps)
* ~~mutation of actors with 'become' ?~~ add functions (will be overrided accordingly) for every
  "def receive"
* DSL pour la création automatique d'un réseau
* Lire/Écrire des fichiers .aer
