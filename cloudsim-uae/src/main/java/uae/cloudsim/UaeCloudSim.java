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
            int extra = 15;
            int startId = vmTemplateList.size();
            for (int i = 0; i < extra; i++) {
                org.cloudbus.cloudsim.vms.VmSimple vm = new org.cloudbus.cloudsim.vms.VmSimple(
                        startId + i, Infrastructure.VM_MIPS_PER_PE * Infrastructure.VM_PES, Infrastructure.VM_PES);
                vm.setRam(Infrastructure.VM_RAM_MB);
                vm.setSize(Infrastructure.VM_STORAGE_MB);
                vm.setBw(Infrastructure.VM_BW_MBPS);
                org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler scheduler =
                        (policy == Policies.SPACESHARED_BESTFIT)
                                ? new org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared()
                                : new org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared();
                vm.setCloudletScheduler(scheduler);
                vmTemplateList.add(vm);
            }
        }

        Autoscaling.resetScaledCount();
        if (autoscaling) {
            setupAutoscaling(infra, policy);
        }

        DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);
        broker.submitVmList(vmTemplateList);

        List<Cloudlet> cloudletList = Workload.createCloudlets(period);
        broker.submitCloudletList(cloudletList);

        final double[] cpuSum = {0.0};
        final double[] ramSum = {0.0};
        final int[] samples = {0};
        simulation.addOnClockTickListener(e -> {
            cpuSum[0] += Metrics.computeAvgCpuUtilization(infra.getAllHosts());
            ramSum[0] += Metrics.computeAvgRamUtilization(infra.getAllHosts());
            samples[0]++;
        });

        simulation.start();

        List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();
        int initialVmCount = vmTemplateList.size();
        int finalVmCount = initialVmCount + Autoscaling.getScaledCount();

        double makespan = Metrics.computeMakespan(finishedCloudlets);
        double avgResponseTime = Metrics.computeAvgResponseTime(finishedCloudlets);
        double avgCpu = samples[0] > 0 ? cpuSum[0] / samples[0] : 0.0;
        double avgRam = samples[0] > 0 ? ramSum[0] / samples[0] : 0.0;
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

    private static void setupAutoscaling(Infrastructure infra, Policies policy) {
        Supplier<Vm> vmSupplier = () -> {
            org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler scheduler =
                    (policy == Policies.SPACESHARED_BESTFIT)
                            ? new org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared()
                            : new org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared();
            Vm vm = new org.cloudbus.cloudsim.vms.VmSimple(
                    0, Infrastructure.VM_MIPS_PER_PE * Infrastructure.VM_PES, Infrastructure.VM_PES)
                    .setRam(Infrastructure.VM_RAM_MB)
                    .setSize(Infrastructure.VM_STORAGE_MB)
                    .setBw(Infrastructure.VM_BW_MBPS)
                    .setCloudletScheduler(scheduler);
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