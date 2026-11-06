package uae.cloudsim;

import org.cloudsimplus.autoscaling.HorizontalVmScalingSimple;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.function.Predicate;

/**
 * Configuration de l'autoscaling horizontal (HorizontalVmScalingSimple).
 * Uniquement activé pendant les périodes de pointe (Inscriptions, Examens).
 * Lorsqu'une VM dépasse 80% d'utilisation CPU, une nouvelle VM identique est ajoutée.
 */
public class Autoscaling {

    private static final double CPU_OVERLOAD_THRESHOLD = 0.8;

    /**
     * Active l'autoscaling sur une liste de VMs.
     * Chaque VM surchargée déclenche la création d'une nouvelle VM via le fournisseur.
     *
     * @param vms liste des VMs à surveiller
     * @param vmSupplier fournisseur de nouvelles VMs (même configuration)
     */
    public static void enableHorizontalScaling(java.util.List<Vm> vms,
                                               java.util.function.Supplier<Vm> vmSupplier) {
        Predicate<Vm> overloadPredicate = vm -> vm.getCpuPercentUtilization() > CPU_OVERLOAD_THRESHOLD;

        for (Vm vm : vms) {
            HorizontalVmScalingSimple scaling = new HorizontalVmScalingSimple();
            scaling.setVmSupplier(vmSupplier);
            scaling.setOverloadPredicate(overloadPredicate);
            vm.setHorizontalScaling(scaling);
        }
    }
}