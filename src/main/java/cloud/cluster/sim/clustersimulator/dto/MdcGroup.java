package cloud.cluster.sim.clustersimulator.dto;

/**
 * Treatment category group of instances;
 */
public class MdcGroup {

    private MicroDataCenter microDataCenter;
    private long  vmNumber;

    public MdcGroup() {
    }

    public MdcGroup(MicroDataCenter microDataCenter, long vmNumber) {
        this.microDataCenter = microDataCenter;
        this.vmNumber = vmNumber;
    }

    public MicroDataCenter getMicroDataCenter() {
        return microDataCenter;
    }

    public void setMicroDataCenter(MicroDataCenter microDataCenter) {
        this.microDataCenter = microDataCenter;
    }

    public long getVmNumber() {
        return vmNumber;
    }

    public void setVmNumber(long vmNumber) {
        this.vmNumber = vmNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MdcGroup mdcGroup = (MdcGroup) o;

        return microDataCenter.equals(mdcGroup.microDataCenter);

    }

    @Override
    public int hashCode() {
        return microDataCenter.hashCode();
    }
}
