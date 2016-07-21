package cloud.cluster.sim.clustersimulator.dto;

/**
 * A VM is an execution engine, able to execute tasks.
 */
public class Vm {
    private Task task;
    private TreatmentCategory treatmentCategory;

    public Vm() {
    }

    public Vm(TreatmentCategory treatmentCategory) {
        this.treatmentCategory = treatmentCategory;
    }

    public Vm(Task task, TreatmentCategory treatmentCategory) {
        this.task = task;
        this.treatmentCategory = treatmentCategory;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TreatmentCategory getTreatmentCategory() {
        return treatmentCategory;
    }

    public void setTreatmentCategory(TreatmentCategory treatmentCategory) {
        this.treatmentCategory = treatmentCategory;
    }
}
