package uae.cloudsim;

import org.cloudsimplus.util.Log;
import ch.qos.logback.classic.Level;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.vms.Vm;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Point d'entree de la simulation CloudSim Plus pour l'Universite Abdelmalek Essaadi.
 *
 * Simule 6 scenarios : 2 politiques x 3 periodes (Normale, Inscriptions, Examens).
 * L'autoscaling horizontal est active uniquement en periode de pointe.
 *
 * Resultats affiches en console et ecrits dans simulation_results.csv.
 */
public class UaeCloudSim {

    private static final double COST_PER_VM_HOUR = 0.05;

    public static void main(String[] args) {
        Log.setLevel(Level.OFF);
        System.out.println("================================================================");
        System.out.println("  SIMULATION CLOUD UNIVERSITAIR - Universite Abdelmalek Essaadi");
        System.out.println("  CloudSim Plus - Architecture Hybride (Tanger + Tétouan)");
        System.out.println("================================================================");

        List<SimulationResult> results = new ArrayList<>();

        String[] periods = {"NORMALE", "INSCRIPTIONS", "EXAMENS"};
        Policies[] policies = {Policies.TIMESHARED_SIMPLE, Policies.SPACESHARED_BESTFIT};

        PrintWriter csvWriter = null;
        try {
            csvWriter = new PrintWriter("simulation_results.csv");
            csvWriter.println(SimulationResult.csvHeader());

            for (String period : periods) {
                for (Policies policy : policies) {
                    SimulationResult result = runScenario(period, policy);
                    results.add(result);
                    System.out.println(result);
                    csvWriter.println(result.toCsvLine());
                    csvWriter.flush();
                }
            }
        } catch (Exception e) {
            System.err.println("[ERREUR] " + e.getMessage());
        } finally {
            if (csvWriter != null) csvWriter.close();
        }

        System.out.println("\n================================================================");
        System.out.println("  SIMULATION TERMINEE - Resultats ecrits dans simulation_results.csv");
        System.out.println("================================================================");
    }

    private static SimulationResult runScenario(String period, Policies policy) {
        String scenarioName = String.format("S%d - %s / %s",
                scenarioIndex(period, policy), period, policy);
        boolean autoscaling = period.equals("INSCRIPTIONS") || period.equals("EXAMENS");

        CloudSim simulation = new CloudSim();
        Infrastructure infra = new Infrastructure(simulation, policy);
        List<Vm> vmTemplateList = infra.getVmList();

        if (autoscaling) {
            setupAutoscaling(infra);
        }

        DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);
        broker.submitVmList(vmTemplateList);

        List<Cloudlet> cloudletList = Workload.createCloudlets(period);
        broker.submitCloudletList(cloudletList);

        simulation.start();

        List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        int initialVmCount = vmTemplateList.size();
        int finalVmCount = broker.getVmCreatedList().size();

        double makespan = Metrics.computeMakespan(finishedCloudlets);
        double avgResponseTime = Metrics.computeAvgResponseTime(finishedCloudlets);
        double avgCpu = Metrics.computeAvgCpuUtilization(infra.getAllHosts());
        double avgRam = Metrics.computeAvgRamUtilization(infra.getAllHosts());
        double cost = Metrics.computeEstimatedCost(finalVmCount, makespan);

        return new SimulationResult(
                scenarioName, period, cloudletList.size(),
                policy.schedulerClassName, policy.allocationClassName, autoscaling,
                makespan, avgResponseTime, avgCpu, avgRam,
                initialVmCount, finalVmCount, cost);
    }

    private static int scenarioIndex(String period, Policies policy) {
        int pIdx = switch (period) {
            case "NORMALE" -> 0;
            case "INSCRIPTIONS" -> 1;
            case "EXAMENS" -> 2;
            default -> 0;
        };
        int polIdx = (policy == Policies.TIMESHARED_SIMPLE) ? 0 : 1;
        return pIdx * 2 + polIdx + 1;
    }

    private static void setupAutoscaling(Infrastructure infra) {
        Supplier<Vm> vmSupplier = () -> {
            Vm vm = new org.cloudbus.cloudsim.vms.VmSimple(
                    0, Infrastructure.VM_MIPS_PER_PE * Infrastructure.VM_PES, Infrastructure.VM_PES)
                    .setRam(Infrastructure.VM_RAM_MB)
                    .setSize(Infrastructure.VM_STORAGE_MB)
                    .setBw(Infrastructure.VM_BW_MBPS)
                    .setCloudletScheduler(new org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared());
            return vm;
        };
        Autoscaling.enableHorizontalScaling(infra.getVmList(), vmSupplier);
    }

    private static void writeCsv(List<SimulationResult> results) {
        try (PrintWriter writer = new PrintWriter("simulation_results.csv")) {
            writer.println(SimulationResult.csvHeader());
            for (SimulationResult r : results) {
                writer.println(r.toCsvLine());
            }
            System.out.println("\n[CSV] Resultats ecrits dans simulation_results.csv");
        } catch (Exception e) {
            System.err.println("[ERREUR] Impossible d'ecrire le CSV: " + e.getMessage());
        }
    }
}