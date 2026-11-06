package uae.cloudsim;

/**
 * Résultat d'un scénario de simulation CloudSim Plus.
 * Contient toutes les métriques nécessaires pour l'analyse et le rapport.
 */
public class SimulationResult {

    /** Nom du scénario (ex: "S1 - Normale / TimeShared + Simple") */
    public final String scenarioName;
    /** Période simulée: "NORMALE", "INSCRIPTIONS", "EXAMENS" */
    public final String period;
    /** Nombre de cloudlets soumis */
    public final int cloudletCount;
    /** Politique d'ordonnancement des cloudlets */
    public final String schedulerPolicy;
    /** Politique d'allocation des VM sur les hôtes */
    public final String allocationPolicy;
    /** Autoscaling horizontal activé (uniquement en pointe) */
    public final boolean autoscaling;
    /** Makespan: temps de fin du dernier cloudlet (secondes) */
    public final double makespanSeconds;
    /** Temps de réponse moyen par cloudlet (secondes) */
    public final double avgResponseTimeSeconds;
    /** Utilisation CPU moyenne des hôtes (%) */
    public final double avgCpuUtilizationPercent;
    /** Utilisation RAM moyenne des hôtes (%) */
    public final double avgRamUtilizationPercent;
    /** Nombre de VMs initiales */
    public final int initialVmCount;
    /** Nombre de VMs après autoscaling (peut être supérieur) */
    public final int finalVmCount;
    /** Coût estimé (USD) basé sur VM-heures */
    public final double estimatedCostUsd;

    public SimulationResult(String scenarioName, String period, int cloudletCount,
                            String schedulerPolicy, String allocationPolicy, boolean autoscaling,
                            double makespanSeconds, double avgResponseTimeSeconds,
                            double avgCpuUtilizationPercent, double avgRamUtilizationPercent,
                            int initialVmCount, int finalVmCount, double estimatedCostUsd) {
        this.scenarioName = scenarioName;
        this.period = period;
        this.cloudletCount = cloudletCount;
        this.schedulerPolicy = schedulerPolicy;
        this.allocationPolicy = allocationPolicy;
        this.autoscaling = autoscaling;
        this.makespanSeconds = makespanSeconds;
        this.avgResponseTimeSeconds = avgResponseTimeSeconds;
        this.avgCpuUtilizationPercent = avgCpuUtilizationPercent;
        this.avgRamUtilizationPercent = avgRamUtilizationPercent;
        this.initialVmCount = initialVmCount;
        this.finalVmCount = finalVmCount;
        this.estimatedCostUsd = estimatedCostUsd;
    }

    /** Écrit l'en-tête CSV */
    public static String csvHeader() {
        return "Scenario,Periode,Cloudlets,Ordonnancement,Allocation,Autoscaling," +
               "Makespan_s,TempsReponseMoyen_s,CPU_Util_%,RAM_Util_%,VMs_Initiales,VMs_Finales,Cout_USD";
    }

    /** Écrit une ligne CSV pour ce résultat */
    public String toCsvLine() {
        return String.format("%s,%s,%d,%s,%s,%b,%.2f,%.2f,%.2f,%.2f,%d,%d,%.4f",
                scenarioName, period, cloudletCount, schedulerPolicy, allocationPolicy, autoscaling,
                makespanSeconds, avgResponseTimeSeconds,
                avgCpuUtilizationPercent, avgRamUtilizationPercent,
                initialVmCount, finalVmCount, estimatedCostUsd);
    }

    @Override
    public String toString() {
        return String.format(
                "%s | Periode=%s | Cloudlets=%d | %s/%s | Autoscaling=%b | " +
                "Makespan=%.2fs | RespMoy=%.2fs | CPU=%.1f%% | RAM=%.1f%% | VMs=%d->%d | Cout=%.4f$",
                scenarioName, period, cloudletCount, schedulerPolicy, allocationPolicy, autoscaling,
                makespanSeconds, avgResponseTimeSeconds,
                avgCpuUtilizationPercent, avgRamUtilizationPercent,
                initialVmCount, finalVmCount, estimatedCostUsd);
    }
}