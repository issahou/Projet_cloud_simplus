package uae.cloudsim;

/**
 * Paires de politiques comparées dans la simulation.
 * Chaque paire associe une politique d'ordonnancement des cloudlets
 * et une politique d'allocation des VM sur les hôtes.
 */
public enum Policies {
    TIMESHARED_SIMPLE("CloudletSchedulerTimeShared", "VmAllocationPolicySimple"),
    SPACESHARED_BESTFIT("CloudletSchedulerSpaceShared", "VmAllocationPolicyBestFit");

    public final String schedulerClassName;
    public final String allocationClassName;

    Policies(String schedulerClassName, String allocationClassName) {
        this.schedulerClassName = schedulerClassName;
        this.allocationClassName = allocationClassName;
    }

    @Override
    public String toString() {
        return schedulerClassName + " + " + allocationClassName;
    }
}