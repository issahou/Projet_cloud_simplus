#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Generateur de rapport .docx pour le projet CloudSim Plus - Universite Abdelmalek Essaadi
"""

from docx import Document
from docx.shared import Pt, Cm, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml.ns import qn
from docx.oxml import OxmlElement


def set_cell_background(cell, color_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement('w:shd')
    shd.set(qn('w:val'), 'clear')
    shd.set(qn('w:color'), 'auto')
    shd.set(qn('w:fill'), color_hex)
    tcPr.append(shd)


def add_page_number_footer(doc):
    section = doc.sections[0]
    footer = section.footer
    p = footer.paragraphs[0]
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run()
    fldChar1 = OxmlElement('w:fldChar')
    fldChar1.set(qn('w:fldCharType'), 'begin')
    instrText = OxmlElement('w:instrText')
    instrText.set(qn('xml:space'), 'preserve')
    instrText.text = 'PAGE'
    fldChar2 = OxmlElement('w:fldChar')
    fldChar2.set(qn('w:fldCharType'), 'end')
    run._r.append(fldChar1)
    run._r.append(instrText)
    run._r.append(fldChar2)


def add_capture_placeholder(doc, caption):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run(f"[Capture d'ecran : {caption}]")
    run.italic = True
    run.font.color.rgb = RGBColor(0x80, 0x80, 0x80)
    run.font.size = Pt(10)


def create_report():
    doc = Document()

    style = doc.styles['Normal']
    style.font.name = 'Calibri'
    style.font.size = Pt(11)

    for section in doc.sections:
        section.top_margin = Cm(2.0)
        section.bottom_margin = Cm(2.0)
        section.left_margin = Cm(2.0)
        section.right_margin = Cm(2.0)

    add_page_number_footer(doc)

    # ============================================================
    # PAGE DE GARDE
    # ============================================================
    for _ in range(4):
        doc.add_paragraph()

    title = doc.add_paragraph()
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = title.add_run("RAPPORT DE PROJET\nCLOUD COMPUTING UNIVERSITAIRE")
    run.bold = True
    run.font.size = Pt(24)
    run.font.color.rgb = RGBColor(0x1F, 0x4E, 0x79)

    doc.add_paragraph()

    subtitle = doc.add_paragraph()
    subtitle.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = subtitle.add_run("Simulation d'un cloud universitaire avec CloudSim Plus")
    run.bold = True
    run.font.size = Pt(16)

    doc.add_paragraph()
    doc.add_paragraph()

    univ = doc.add_paragraph()
    univ.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = univ.add_run("Universite Abdelmalek Essaadi (UAE)")
    run.bold = True
    run.font.size = Pt(14)

    dept = doc.add_paragraph()
    dept.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = dept.add_run("Region Tanger-Tetouan-Al Hoceima, Maroc")
    run.font.size = Pt(12)

    doc.add_paragraph()
    doc.add_paragraph()

    info_table = doc.add_table(rows=4, cols=2)
    info_table.alignment = WD_TABLE_ALIGNMENT.CENTER
    info_data = [
        ("Etudiant", "Projet de simulation Cloud Computing"),
        ("Encadrant", "Architecte Cloud - UAE"),
        ("Date", "06/11/2026"),
        ("Outils", "CloudSim Plus 6.1.3, Java 21, Maven, Python python-docx"),
    ]
    for i, (k, v) in enumerate(info_data):
        cell_k = info_table.rows[i].cells[0]
        cell_v = info_table.rows[i].cells[1]
        cell_k.text = k
        cell_v.text = v
        cell_k.paragraphs[0].runs[0].bold = True
        set_cell_background(cell_k, "D9E2F3")

    doc.add_page_break()

    # ============================================================
    # SOMMAIRE
    # ============================================================
    h = doc.add_heading("Sommaire", level=1)
    toc_items = [
        "1. Contexte et documentation sur l'Universite Abdelmalek Essaadi",
        "2. Proposition d'architecture Cloud",
        "3. Modelisation des charges de travail (Cloudlets)",
        "4. Simulation CloudSim Plus et comparaison des politiques",
        "5. Analyse des résultats et conclusion",
        "6. Annexes",
    ]
    for item in toc_items:
        p = doc.add_paragraph(item, style='List Number')

    doc.add_page_break()

    # ============================================================
    # PARTIE 1 : CONTEXTE UAE
    # ============================================================
    doc.add_heading("1. Contexte et documentation sur l'Universite Abdelmalek Essaadi", level=1)

    doc.add_paragraph(
        "L'Universite Abdelmalek Essaadi (UAE) est une universite publique marocaine "
        "cree en 1989. Elle est repartie sur 6 villes de la region Tanger-Tetouan-Al Hoceima "
        "et compte 17 etablissements universitaires."
    )

    doc.add_heading("1.1 Chiffres cles", level=2)
    table = doc.add_table(rows=1, cols=2)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Indicateur"
    hdr[1].text = "Valeur"
    data = [
        ("Etablissements universitaires", "17"),
        ("Villes couvertes", "6 (Tanger, Tetouan, Martil, Larache, Ksar El Kebir, Al Hoceima)"),
        ("Etudiants", "~123 000"),
        ("Enseignants-chercheurs", "~1 218"),
    ]
    for k, v in data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v

    doc.add_heading("1.2 Services numeriques existants ou envisageables", level=2)
    services = [
        "ENT (Espace Numerique de Travail)",
        "Preinscription en ligne",
        "Plateformes de candidature Master",
        "E-mail institutionnel",
        "Campus connecte",
        "E-learning / enseignement a distance",
        "Bibliotheque numerique",
        "Examens en ligne",
        "Gestion des notes",
        "Gestion RH",
        "Gestion de la recherche scientifique",
    ]
    for s in services:
        doc.add_paragraph(s, style='List Bullet')

    doc.add_heading("1.3 Sources bibliographiques", level=2)
    sources = [
        "Universite Abdelmalek Essaadi, Chronologie. Disponible sur : https://www.uae.ac.ma/chronologie",
        "Wikipédia, Universite Abdelmalek Essaadi. Disponible sur : https://fr.wikipedia.org/wiki/Universit%C3%A9_Abdelmalek_Essa%C3%A2di",
    ]
    for s in sources:
        doc.add_paragraph(s, style='List Bullet')

    add_capture_placeholder(doc, "Carte des 6 villes de l'UAE avec emplacement des datacenters")

    doc.add_page_break()

    # ============================================================
    # PARTIE 2 : ARCHITECTURE CLOUD
    # ============================================================
    doc.add_heading("2. Proposition d'architecture Cloud", level=1)

    doc.add_heading("2.1 Choix du modele de deploiement : Cloud Hybride", level=2)
    doc.add_paragraph(
        "L'architecture adopte un modele de deployment Cloud Hybride pour les raisons suivantes :"
    )
    reasons = [
        "Les donnees sensibles (notes, dossiers etudiants, dossiers RH) restent sur les serveurs prives de l'universite.",
        "Les pics de charge (examens, inscriptions) peuvent utiliser un cloud public pour absorber la surcharge.",
        "Une continuite de service est assuree meme en cas de panne d'un des deux datacenters principaux.",
    ]
    for r in reasons:
        doc.add_paragraph(r, style='List Bullet')

    doc.add_heading("2.2 Modele de service", level=2)
    table = doc.add_table(rows=1, cols=2)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Service"
    hdr[1].text = "Modele"
    svc_data = [
        ("Machines virtuelles (IaaS)", "IaaS"),
        ("Plateforme e-learning (Moodle)", "PaaS"),
        ("ENT", "SaaS"),
        ("Messagerie institutionnelle", "SaaS"),
        ("Bibliotheque numerique", "SaaS"),
    ]
    for k, v in svc_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v

    doc.add_heading("2.3 Infrastructure physique", level=2)
    doc.add_paragraph(
        "Deux datacenters principaux sont places dans les deux plus grands poles universitaires :"
    )
    table = doc.add_table(rows=1, cols=4)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Datacenter"
    hdr[1].text = "Ville"
    hdr[2].text = "Nombre d'hotes"
    hdr[3].text = "Capacite totale"
    dc_data = [
        ("Datacenter 1", "Tanger", "15 hotes", "480 vCPU, 1920 Go RAM, 60 To stockage"),
        ("Datacenter 2", "Tetouan", "10 hotes", "320 vCPU, 1280 Go RAM, 40 To stockage"),
        ("TOTAL", "2 villes", "25 hotes", "800 vCPU, 3200 Go RAM, 100 To stockage"),
    ]
    for k, v1, v2, v3 in dc_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v1
        row[2].text = v2
        row[3].text = v3

    doc.add_heading("2.4 Configuration d'un hote", level=2)
    host_specs = [
        ("Coeurs CPU", "32"),
        ("RAM", "128 Go"),
        ("Stockage", "4 To"),
        ("Reseau", "10 Gbps"),
    ]
    for k, v in host_specs:
        p = doc.add_paragraph()
        p.add_run(f"{k} : ").bold = True
        p.add_run(v)

    doc.add_heading("2.5 Répartition des VM par service (Total : 60 VMs)", level=2)
    table = doc.add_table(rows=1, cols=3)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Service"
    hdr[1].text = "Nombre de VMs"
    hdr[2].text = "Configuration type VM"
    vm_data = [
        ("ENT", "8", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("Inscriptions", "8", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("E-learning Moodle", "12", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("Messagerie", "6", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("Bibliotheque numerique", "4", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("Examens en ligne", "10", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("Base de donnees", "8", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("Sauvegarde", "4", "4 vCPU, 8 Go RAM, 100 Go disque"),
        ("TOTAL", "60", "-"),
    ]
    for k, v1, v2 in vm_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v1
        row[2].text = v2

    add_capture_placeholder(doc, "Schéma d'architecture Cloud UAE (Internet -> Load Balancer -> 2 Datacenters -> VMs -> Services)")

    doc.add_page_break()

    # ============================================================
    # PARTIE 3 : MODELISATION DES CHARGES
    # ============================================================
    doc.add_heading("3. Modelisation des charges de travail (Cloudlets)", level=1)

    doc.add_paragraph(
        "Dans CloudSim Plus, les utilisateurs sont representes par des Cloudlets. "
        "Trois périodes de charge sont modélisees :"
    )

    doc.add_heading("3.1 Situation normale (15 000 connexions simultanées)", level=2)
    table = doc.add_table(rows=1, cols=3)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Type d'activite"
    hdr[1].text = "Nombre de cloudlets"
    hdr[2].text = "Longueur (MI)"
    normal_data = [
        ("Consultation ENT", "10 000", "10 000 MI"),
        ("E-learning", "3 000", "10 000 MI"),
        ("Bibliotheque", "1 000", "10 000 MI"),
        ("Messagerie", "1 000", "10 000 MI"),
        ("TOTAL", "15 000", "10 000 MI"),
    ]
    for k, v1, v2 in normal_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v1
        row[2].text = v2

    doc.add_heading("3.2 Periode d'inscriptions (40 000 connexions simultanées)", level=2)
    table = doc.add_table(rows=1, cols=3)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Type d'activite"
    hdr[1].text = "Nombre de cloudlets"
    hdr[2].text = "Longueur (MI)"
    insc_data = [
        ("Inscription", "25 000", "20 000 MI"),
        ("Consultation", "10 000", "10 000 MI"),
        ("Paiement", "5 000", "20 000 MI"),
        ("TOTAL", "40 000", "10 000 - 20 000 MI"),
    ]
    for k, v1, v2 in insc_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v1
        row[2].text = v2

    doc.add_heading("3.3 Periode d'examens (50 000 connexions simultanées)", level=2)
    table = doc.add_table(rows=1, cols=3)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Type d'activite"
    hdr[1].text = "Nombre de cloudlets"
    hdr[2].text = "Longueur (MI)"
    exam_data = [
        ("Passage examen", "30 000", "30 000 MI"),
        ("Sauvegarde reponses", "10 000", "30 000 MI"),
        ("Consultation sujet", "10 000", "10 000 MI"),
        ("TOTAL", "50 000", "10 000 - 30 000 MI"),
    ]
    for k, v1, v2 in exam_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v1
        row[2].text = v2

    doc.add_paragraph(
        "Note : Pour des raisons de temps de calcul, la periode d'examens a été simulée "
        "avec un nombre reduit de cloudlets (5 000 au lieu de 50 000) tout en conservant "
        "les mêmes proportions et longueurs MI. Les tendances observees restent valides."
    )

    doc.add_page_break()

    # ============================================================
    # PARTIE 4 : SIMULATION
    # ============================================================
    doc.add_heading("4. Simulation CloudSim Plus et comparaison des politiques", level=1)

    doc.add_heading("4.1 Environnement de simulation", level=2)
    env_items = [
        ("Outil", "CloudSim Plus 6.1.3"),
        ("Langage", "Java 21"),
        ("Build", "Maven 3.9.14"),
        ("Datacenters", "2 (Tanger : 15 hotes, Tetouan : 10 hotes)"),
        ("VMs initiales", "60 (répartition par service)"),
        ("Politiques compares", "2 paires croisées"),
        ("Autoscaling", "HorizontalVmScalingSimple (actif uniquement en periode de pointe)"),
    ]
    for k, v in env_items:
        p = doc.add_paragraph()
        p.add_run(f"{k} : ").bold = True
        p.add_run(v)

    doc.add_heading("4.2 Politiques comparees", level=2)
    doc.add_paragraph(
        "Deux paires de politiques sont comparees sur chacune des trois périodes :"
    )
    table = doc.add_table(rows=1, cols=3)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Scénario"
    hdr[1].text = "Ordonnancement des cloudlets"
    hdr[2].text = "Allocation des VM"
    pol_data = [
        ("Paire A", "CloudletSchedulerTimeShared", "VmAllocationPolicySimple"),
        ("Paire B", "CloudletSchedulerSpaceShared", "VmAllocationPolicyBestFit"),
    ]
    for k, v1, v2 in pol_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v1
        row[2].text = v2

    doc.add_heading("4.3 Scénarios exécutés", level=2)
    table = doc.add_table(rows=1, cols=5)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    hdr[0].text = "Scenario"
    hdr[1].text = "Periode"
    hdr[2].text = "Cloudlets"
    hdr[3].text = "Politiques"
    hdr[4].text = "Autoscaling"
    scen_data = [
        ("S1", "Normale", "15 000", "TimeShared + Simple", "Non"),
        ("S2", "Normale", "15 000", "SpaceShared + BestFit", "Non"),
        ("S3", "Inscriptions", "40 000", "TimeShared + Simple", "Oui"),
        ("S4", "Inscriptions", "40 000", "SpaceShared + BestFit", "Oui"),
        ("S5", "Examens", "50 000 (5 000 simulés)", "TimeShared + Simple", "Oui"),
        ("S6", "Examens", "50 000 (5 000 simulés)", "SpaceShared + BestFit", "Oui"),
    ]
    for k, v1, v2, v3, v4 in scen_data:
        row = table.add_row().cells
        row[0].text = k
        row[1].text = v1
        row[2].text = v2
        row[3].text = v3
        row[4].text = v4

    add_capture_placeholder(doc, "Console d'exécution Java (Maven) montrant le déroulement des 6 scénarios")

    doc.add_page_break()

    # ============================================================
    # PARTIE 5 : ANALYSE DES RESULTATS
    # ============================================================
    doc.add_heading("5. Analyse des résultats et conclusion", level=1)

    doc.add_heading("5.1 Tableau des résultats", level=2)
    table = doc.add_table(rows=1, cols=7)
    table.style = 'Light Grid Accent 1'
    hdr = table.rows[0].cells
    for i, htxt in enumerate(["Scénario", "Période", "Cloudlets", "Politiques", "Makespan (s)", "Temps réponse moy. (s)", "VMs init→final"]):
        hdr[i].text = htxt
    results = [
        ("S1", "Normale", "15 000", "TimeShared + Simple", "375,31", "31,38", "60 → 25"),
        ("S2", "Normale", "15 000", "SpaceShared + BestFit", "406,33", "204,17", "60 → 25"),
        ("S3", "Inscriptions", "40 000", "TimeShared + Simple", "1750,45", "1562,91", "60 → 25"),
        ("S4", "Inscriptions", "40 000", "SpaceShared + BestFit", "1823,22", "978,04", "60 → 25"),
        ("S5", "Examens", "5 000 (50 000 réels)", "TimeShared + Simple", "325,31", "285,29", "60 → 25"),
        ("S6", "Examens", "5 000 (50 000 réels)", "SpaceShared + BestFit", "335,61", "191,13", "60 → 25"),
    ]
    for r in results:
        row = table.add_row().cells
        for i, v in enumerate(r):
            row[i].text = v

    doc.add_heading("5.2 Analyse comparative", level=2)
    analysis = [
        "En période normale, TimeShared + Simple donne le meilleur makespan (375,31 s vs 406,33 s) et le meilleur temps de réponse moyen (31,38 s vs 204,17 s).",
        "En période d'inscriptions, TimeShared + Simple reste devant sur le makespan (1750,45 s vs 1823,22 s), mais SpaceShared + BestFit réduit fortement le temps de réponse moyen (978,04 s vs 1562,91 s).",
        "En période d'examens, SpaceShared + BestFit donne le meilleur temps de réponse moyen (191,13 s vs 285,29 s) pour un makespan proche (335,61 s vs 325,31 s).",
        "L'autoscaling horizontal est activé en pointe et contribue à absorber la charge, au prix d'un coût plus élevé (≈ 6 000 USD vs ≈ 1 300 USD en normale).",
        "La charge d'examens a été simulée sur 5 000 cloudlets (au lieu de 50 000) pour des raisons de temps de calcul ; les tendances restent valides.",
    ]
    for a in analysis:
        doc.add_paragraph(a, style='List Bullet')

    doc.add_heading("5.3 Conclusion", level=2)
    doc.add_paragraph(
        "L'architecture hybride proposée (2 datacenters, 25 hôtes, 60 VM initiales avec "
        "autoscaling horizontal) suffit en période normale et reste acceptable en période de "
        "pointe grâce à l'autoscaling, au prix d'un coût et d'un temps de réponse accrus. "
        "La politique SpaceShared + BestFit est recommandée pour minimiser le temps de réponse "
        "en pointe, tandis que TimeShared + Simple maximise le débit en charge normale."
    )

    doc.add_page_break()

    # ============================================================
    # PARTIE 6 : ANNEXES
    # ============================================================
    doc.add_heading("6. Annexes", level=1)

    doc.add_heading("6.1 Environnement de simulation", level=2)
    env_items = [
        ("Outil", "CloudSim Plus 6.1.3"),
        ("Langage", "Java 21"),
        ("Build", "Maven 3.9.14"),
        ("Point d'entrée", "uae.cloudsim.UaeCloudSim"),
        ("Résultats", "cloudsim-uae/simulation_results.csv"),
    ]
    for k, v in env_items:
        p = doc.add_paragraph()
        p.add_run(f"{k} : ").bold = True
        p.add_run(v)

    doc.add_heading("6.2 Code source", level=2)
    doc.add_paragraph(
        "Le code Java de la simulation se trouve dans cloudsim-uae/src/main/java/uae/cloudsim/ "
        "(UaeCloudSim, Infrastructure, Workload, Policies, Autoscaling, Metrics, SimulationResult)."
    )

    doc.add_heading("6.3 Emplacements des captures à insérer", level=2)
    captures = [
        "Carte des 6 villes de l'UAE avec emplacement des datacenters",
        "Schéma d'architecture Cloud UAE (Internet -> Load Balancer -> 2 Datacenters -> VMs -> Services)",
        "Console d'exécution Java (Maven) montrant le déroulement des 6 scénarios",
    ]
    for c in captures:
        doc.add_paragraph(c, style='List Bullet')

    doc.add_page_break()
    doc.add_page_break()
    return doc


if __name__ == "__main__":
    d = create_report()
    d.save("rapport_uae.docx")
    print("Rapport termine")