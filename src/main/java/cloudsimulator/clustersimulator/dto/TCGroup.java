package cloudsimulator.clustersimulator.dto;

/**
 * Treatment category group of instances;
 */
public class TCGroup {

    private String name;
    private double sla;
    private long  vmNumber;
    private long cost;

    public TCGroup() {
    }

    public TCGroup(String name, double sla, long vmNumber) {
        this.name = name;
        this.sla = sla;
        this.vmNumber = vmNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSla() {
        return sla;
    }

    public void setSla(double sla) {
        this.sla = sla;
    }

    public long getVmNumber() {
        return vmNumber;
    }

    public void setVmNumber(long vmNumber) {
        this.vmNumber = vmNumber;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
