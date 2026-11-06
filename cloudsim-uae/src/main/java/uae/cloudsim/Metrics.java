package uae.cloudsim;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.hosts.Host;

import java.util.List;

/**
 * Collecte et calcule les métriques de simulation après exécution.
 */
public class Metrics {

    /**
     * Calcule le makespan (temps de fin du dernier cloudlet).
     */
    public static double computeMakespan(List<Cloudlet> finishedCloudlets) {
        return finishedCloudlets.stream()
                .mapToDouble(Cloudlet::getFinishTime)
                .max()
                .orElse(0.0);
    }

    /**
     * Calcule le temps de réponse moyen par cloudlet (finishTime - submissionTime).
     */
    public static double computeAvgResponseTime(List<Cloudlet> finishedCloudlets) {
        if (finishedCloudlets.isEmpty()) return 0.0;
        return finishedCloudlets.stream()
                .mapToDouble(c -> c.getFinishTime() - c.getSubmissionDelay())
                .average()
                .orElse(0.0);
    }

    /**
     * Calcule l'utilisation CPU moyenne de tous les hôtes (%).
     */
    public static double computeAvgCpuUtilization(List<Host> hosts) {
        if (hosts.isEmpty()) return 0.0;
        return hosts.stream()
                .mapToDouble(Host::getCpuPercentUtilization)
                .average()
                .orElse(0.0);
    }

    /**
     * Calcule l'utilisation RAM moyenne de tous les hôtes (%).
     */
    public static double computeAvgRamUtilization(List<Host> hosts) {
        if (hosts.isEmpty()) return 0.0;
        return hosts.stream()
                .mapToDouble(Host::getRamUtilization)
                .average()
                .orElse(0.0);
    }

    /**
     * Estime le coût (USD) basé sur le nombre de VMs et le makespan.
     * Tarif hypothétique: 0.05 $ par VM-heure.
     */
    public static double computeEstimatedCost(int vmCount, double makespanSeconds) {
        double hours = makespanSeconds / 3600.0;
        return vmCount * hours * 0.05;
    }
}