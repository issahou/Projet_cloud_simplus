# Cloud Computing Universitaire - Simulation CloudSim Plus (UAE)

Projet de simulation d'un cloud universitaire pour l'Université Abdelmalek Essaadi (UAE) avec CloudSim Plus.

## Structure
- `cloudsim-uae/` : projet Maven Java (simulation CloudSim Plus).
- `generer_rapport.py` : génération du rapport `.docx`.
- `architecture_uae.md`, `sujet.md` : sujet et architecture.
- `simulation_results.csv` : résultats de simulation.
- `rapport_uae*.docx` : rapports générés.

## Prérequis
- Java 21
- Maven 3.9+
- Python 3 + `python-docx`

## Exécuter la simulation
```bash
cd cloudsim-uae
mvn compile
mvn exec:java
```
Les résultats sont écrits dans `simulation_results.csv` (à la racine du projet Java).

## Générer le rapport
```bash
cd ..
python generer_rapport.py
```
Le fichier `rapport_uae.docx` est généré (ou `rapport_uae_final.docx` si le fichier est verrouillé).

## Scénarios
6 scénarios : 2 politiques × 3 périodes (Normale, Inscriptions, Examens), avec autoscaling horizontal en pointe.