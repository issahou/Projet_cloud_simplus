se referer a architecture_uae.md

Partie 1. Simulation d'un cloud universitaire avec CloudSim Plus
Proposer une architecture Cloud Computing pour l'Université Abdelmalek Essaâdi, puis la simuler avec CloudSim
Plus.
1. Se documentersur l'université : nombre d'établissements et de villes, nombre d'étudiant(e)s et d'enseignants,
services numériques utilisés ou potentiels (plateforme e-learning, inscriptions, messagerie, examens en ligne,
bibliothèque numérique...). Citer vos sources.
2. Proposer une architecture : nombre et emplacement des datacenters, nombre et capacité des serveurs
(hôtes), machines virtuelles par service, modèle de service (IaaS, PaaS, SaaS) et de déploiement (cloud privé,
public, hybride). Justifier par un schéma.
3. Modéliser les charges : nombre de connexions et types de requêtes (cloudlets) pour une période normale et
pour une période de pointe (inscriptions, examens).
4. Simuler les deux périodes dans CloudSim Plus et comparer au moins deux politiques : ordonnancement des
tâches (CloudletSchedulerTimeShared / CloudletSchedulerSpaceShared) ou allocation des VM
(VmAllocationPolicySimple / VmAllocationPolicyBestFit). Ajouter des VM automatiquement en
période de pointe (HorizontalVmScaling).
5. Analyser les résultats : temps de réponse, temps d'exécution, taux d'utilisation des hôtes, coût. Conclure :
l'architecture proposée suffit-elle en période de pointe?


À rendre : le schéma de l'architecture, le code Java de la simulation, un tableau des résultats et leur analyse.

je veux le code du rapport bien fait et complet en py_docx ( repondant clairement a toute les questions ) avec le plans et la ou se dois inserer les captures ( moi meme ) .