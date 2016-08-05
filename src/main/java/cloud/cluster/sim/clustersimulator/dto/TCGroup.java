package cloud.cluster.sim.clustersimulator.dto;

/**
 * Treatment category group of instances;
 */
public class TCGroup {

    private TreatmentCategory treatmentCategory;
    private long  vmNumber;

    public TCGroup() {
    }

    public TCGroup(TreatmentCategory treatmentCategory, long vmNumber) {
        this.treatmentCategory = treatmentCategory;
        this.vmNumber = vmNumber;
    }

    public TreatmentCategory getTreatmentCategory() {
        return treatmentCategory;
    }

    public void setTreatmentCategory(TreatmentCategory treatmentCategory) {
        this.treatmentCategory = treatmentCategory;
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

        TCGroup tcGroup = (TCGroup) o;

        return treatmentCategory.equals(tcGroup.treatmentCategory);

    }

    @Override
    public int hashCode() {
        return treatmentCategory.hashCode();
    }
}
