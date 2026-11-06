package uae.cloudsim;

import org.cloudsimplus.autoscaling.HorizontalVmScalingSimple;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Autoscaling {
    private static final double CPU_OVERLOAD_THRESHOLD = 0.8;
    private static final AtomicInteger scaledVmCount = new AtomicInteger(0);

    public static void resetScaledCount() { scaledVmCount.set(0); }
    public static int getScaledCount() { return scaledVmCount.get(); }

    public static void enableHorizontalScaling(java.util.List<Vm> vms,
                                               java.util.function.Supplier<Vm> vmSupplier) {
        Predicate<Vm> overloadPredicate = vm -> vm.getCpuPercentUtilization() > CPU_OVERLOAD_THRESHOLD;

        java.util.function.Supplier<Vm> countingSupplier = () -> {
            scaledVmCount.incrementAndGet();
            return vmSupplier.get();
        };

        for (Vm vm : vms) {
            HorizontalVmScalingSimple scaling = new HorizontalVmScalingSimple();
            scaling.setVmSupplier(countingSupplier);
            scaling.setOverloadPredicate(overloadPredicate);
            vm.setHorizontalScaling(scaling);
        }
    }
}