package cloud.cluster.sim.clustersimulator.dto;

/**
 * A VM is an execution engine, able to execute tasks.
 */
public class Vm {
    private Task task;
    private MicroDataCenter microDataCenter;

    public Vm(MicroDataCenter microDataCenter) {
        this.microDataCenter = microDataCenter;
        this.task = new Task();
    }

    public Vm(Task task, MicroDataCenter microDataCenter) {
        this.task = task;
        this.microDataCenter = microDataCenter;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public MicroDataCenter getMicroDataCenter() {
        return microDataCenter;
    }

    public void setMicroDataCenter(MicroDataCenter microDataCenter) {
        this.microDataCenter = microDataCenter;
    }
}
