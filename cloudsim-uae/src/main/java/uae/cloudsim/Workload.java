package uae.cloudsim;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;

import java.util.ArrayList;
import java.util.List;

/**
 * Modélisation des charges de travail (Cloudlets) pour l'UAE.
 * Trois périodes : Normale, Inscriptions, Examens.
 */
public class Workload {

    // Longueurs des cloudlets en MI (Million Instructions)
    public static final long CLOUDLET_LENGTH_NORMAL = 10_000;
    public static final long CLOUDLET_LENGTH_INSCRIPTION = 20_000;
    public static final long CLOUDLET_LENGTH_EXAM = 30_000;

    public static final int CLOUDLET_PES = 1;
    public static final long CLOUDLET_FILE_SIZE = 1024;
    public static final long CLOUDLET_OUTPUT_SIZE = 1024;

    // --- Période Normale : 15 000 cloudlets ---
    public static final int NORMAL_ENT = 10_000;
    public static final int NORMAL_ELEARNING = 3_000;
    public static final int NORMAL_BIBLIOTHEQUE = 1_000;
    public static final int NORMAL_MESSAGERIE = 1_000;
    public static final int NORMAL_TOTAL = 15_000;

    // --- Période Inscriptions : 40 000 cloudlets ---
    public static final int INSCRIPTION_INSCRIPTION = 25_000;
    public static final int INSCRIPTION_CONSULTATION = 10_000;
    public static final int INSCRIPTION_PAIEMENT = 5_000;
    public static final int INSCRIPTION_TOTAL = 40_000;

    // --- Période Examens : 50 000 cloudlets (specification) ---
    public static final int EXAM_PASSAGE = 30_000;
    public static final int EXAM_SAUVEGARDE = 10_000;
    public static final int EXAM_CONSULTATION_SUJET = 10_000;
    public static final int EXAM_TOTAL = 50_000;

    /**
     * Crée tous les cloudlets pour une période donnée.
     * @param period "NORMALE", "INSCRIPTIONS" ou "EXAMENS"
     * @return liste de cloudlets
     */
    public static List<Cloudlet> createCloudlets(String period) {
        List<Cloudlet> cloudlets = new ArrayList<>();
        int id = 0;

        switch (period) {
            case "NORMALE":
                id = addCloudlets(cloudlets, id, "ENT", NORMAL_ENT, CLOUDLET_LENGTH_NORMAL);
                id = addCloudlets(cloudlets, id, "ELEARNING", NORMAL_ELEARNING, CLOUDLET_LENGTH_NORMAL);
                id = addCloudlets(cloudlets, id, "BIBLIOTHEQUE", NORMAL_BIBLIOTHEQUE, CLOUDLET_LENGTH_NORMAL);
                id = addCloudlets(cloudlets, id, "MESSAGERIE", NORMAL_MESSAGERIE, CLOUDLET_LENGTH_NORMAL);
                break;
            case "INSCRIPTIONS":
                id = addCloudlets(cloudlets, id, "INSCRIPTION", INSCRIPTION_INSCRIPTION, CLOUDLET_LENGTH_INSCRIPTION);
                id = addCloudlets(cloudlets, id, "CONSULTATION", INSCRIPTION_CONSULTATION, CLOUDLET_LENGTH_NORMAL);
                id = addCloudlets(cloudlets, id, "PAIEMENT", INSCRIPTION_PAIEMENT, CLOUDLET_LENGTH_INSCRIPTION);
                break;
            case "EXAMENS":
                id = addCloudlets(cloudlets, id, "PASSAGE_EXAMEN", EXAM_PASSAGE, CLOUDLET_LENGTH_EXAM);
                id = addCloudlets(cloudlets, id, "SAUVEGARDE_REPONSES", EXAM_SAUVEGARDE, CLOUDLET_LENGTH_EXAM);
                id = addCloudlets(cloudlets, id, "CONSULTATION_SUJET", EXAM_CONSULTATION_SUJET, CLOUDLET_LENGTH_NORMAL);
                break;
            default:
                throw new IllegalArgumentException("Période inconnue: " + period);
        }
        return cloudlets;
    }

    private static int addCloudlets(List<Cloudlet> cloudlets, int startId,
                                    String type, int count, long length) {
        for (int i = 0; i < count; i++) {
            Cloudlet cloudlet = new CloudletSimple(startId + i, length, CLOUDLET_PES)
                    .setFileSize(CLOUDLET_FILE_SIZE)
                    .setOutputSize(CLOUDLET_OUTPUT_SIZE)
                    .setUtilizationModel(new UtilizationModelFull());
            cloudlets.add(cloudlet);
        }
        return startId + count;
    }

    public static int getCloudletCount(String period) {
        return switch (period) {
            case "NORMALE" -> NORMAL_TOTAL;
            case "INSCRIPTIONS" -> INSCRIPTION_TOTAL;
            case "EXAMENS" -> EXAM_TOTAL;
            default -> 0;
        };
    }
}