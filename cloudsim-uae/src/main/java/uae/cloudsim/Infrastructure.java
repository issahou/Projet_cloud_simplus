package uae.cloudsim;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * Infrastructure physique de l'UAE : 2 datacenters (Tanger + Tétouan),
 * 25 hôtes physiques, 60 VMs réparties par service.
 */
public class Infrastructure {

    public static final int HOST_PES = 32;
    public static final long HOST_RAM_MB = 128_000;
    public static final long HOST_STORAGE_MB = 4_000_000;
    public static final long HOST_BW_MBPS = 10_000;
    public static final double HOST_MIPS_PER_PE = 2500.0;

    public static final int VM_PES = 4;
    public static final long VM_RAM_MB = 8_000;
    public static final long VM_STORAGE_MB = 100_000;
    public static final long VM_BW_MBPS = 1_000;
    public static final double VM_MIPS_PER_PE = 1000.0;

    public static final int HOSTS_TANGER = 15;
    public static final int HOSTS_TETOUAN = 10;

    public static final int VM_ENT = 8;
    public static final int VM_INSCRIPTIONS = 8;
    public static final int VM_ELEARNING = 12;
    public static final int VM_MESSAGERIE = 6;
    public static final int VM_BIBLIOTHEQUE = 4;
    public static final int VM_EXAMENS = 10;
    public static final int VM_BASEDONNEES = 8;
    public static final int VM_SAUVEGARDE = 4;

    public static final int TOTAL_VMS =
            VM_ENT + VM_INSCRIPTIONS + VM_ELEARNING + VM_MESSAGERIE +
                    VM_BIBLIOTHEQUE + VM_EXAMENS + VM_BASEDONNEES + VM_SAUVEGARDE;

    private final CloudSim simulation;
    private final Policies policy;
    private final List<DatacenterSimple> datacenters;
    private final List<Vm> vmList;
    private final List<Host> allHosts;

    public Infrastructure(CloudSim simulation, Policies policy) {
        this.simulation = simulation;
        this.policy = policy;
        this.datacenters = new ArrayList<>();
        this.vmList = new ArrayList<>();
        this.allHosts = new ArrayList<>();
        createDatacenters(policy);
        createVms();
    }

    private void createDatacenters(Policies policy) {
        List<Host> tangerHosts = createHostList(HOSTS_TANGER, "Tanger-H");
        DatacenterSimple dcTanger = new DatacenterSimple(simulation, tangerHosts,
                createVmAllocationPolicy(policy));
        dcTanger.getCharacteristics()
                .setCostPerSecond(0.0).setCostPerMem(0.0)
                .setCostPerStorage(0.0).setCostPerBw(0.0);
        datacenters.add(dcTanger);
        allHosts.addAll(tangerHosts);

        List<Host> tetouanHosts = createHostList(HOSTS_TETOUAN, "Tetouan-H");
        DatacenterSimple dcTetouan = new DatacenterSimple(simulation, tetouanHosts,
                createVmAllocationPolicy(policy));
        dcTetouan.getCharacteristics()
                .setCostPerSecond(0.0).setCostPerMem(0.0)
                .setCostPerStorage(0.0).setCostPerBw(0.0);
        datacenters.add(dcTetouan);
        allHosts.addAll(tetouanHosts);
    }

    private List<Host> createHostList(int count, String namePrefix) {
        List<Host> hosts = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            List<Pe> peList = new ArrayList<>(HOST_PES);
            for (int j = 0; j < HOST_PES; j++) {
                peList.add(new PeSimple(HOST_MIPS_PER_PE));
            }
            HostSimple host = new HostSimple(peList);
            host.setRamProvisioner(new ResourceProvisionerSimple());
            host.setBwProvisioner(new ResourceProvisionerSimple());
            host.setVmScheduler(new VmSchedulerTimeShared());
            hosts.add(host);
        }
        return hosts;
    }

    private VmAllocationPolicy createVmAllocationPolicy(Policies policy) {
        switch (policy) {
            case TIMESHARED_SIMPLE:
                return new org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple();
            case SPACESHARED_BESTFIT:
                return new org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit();
            default:
                return new org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple();
        }
    }

    private void createVms() {
        int id = 0;
        id = addVms(id, "ENT", VM_ENT);
        id = addVms(id, "MESSAGERIE", VM_MESSAGERIE);
        id = addVms(id, "BIBLIOTHEQUE", VM_BIBLIOTHEQUE);
        id = addVms(id, "SAUVEGARDE", 2);
        id = addVms(id, "INSCRIPTIONS", VM_INSCRIPTIONS);
        id = addVms(id, "ELEARNING", VM_ELEARNING);
        id = addVms(id, "EXAMENS", VM_EXAMENS);
        id = addVms(id, "BASEDONNEES", VM_BASEDONNEES);
        id = addVms(id, "SAUVEGARDE", 2);
    }

    private int addVms(int startId, String service, int count) {
        for (int i = 0; i < count; i++) {
            VmSimple vm = new VmSimple(startId + i, VM_MIPS_PER_PE * VM_PES, VM_PES);
            vm.setRam(VM_RAM_MB);
            vm.setSize(VM_STORAGE_MB);
            vm.setBw(VM_BW_MBPS);
            vm.setCloudletScheduler(createCloudletScheduler(policy));
            vmList.add(vm);
        }
        return startId + count;
    }

    private CloudletScheduler createCloudletScheduler(Policies p) {
        return switch (p) {
            case TIMESHARED_SIMPLE -> new org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared();
            case SPACESHARED_BESTFIT -> new org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared();
        };
    }

    public List<DatacenterSimple> getDatacenters() { return datacenters; }
    public List<Vm> getVmList() { return vmList; }
    public List<Host> getAllHosts() { return allHosts; }
    public int getTotalHosts() { return allHosts.size(); }
}