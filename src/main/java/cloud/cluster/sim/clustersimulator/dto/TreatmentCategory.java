package cloud.cluster.sim.clustersimulator.dto;

/**
 * A {@link cloud.cluster.sim.clustersimulator.dto.TreatmentCategory}
 * represents the quality requirements for a group of resources, sharing the same SLA.
 */
public class TreatmentCategory {
    private String name;
    private double sla;
    private long cost;

    public TreatmentCategory() {
    }

    public TreatmentCategory(String name, double sla, long cost) {
        this.name = name;
        this.sla = sla;
        this.cost = cost;
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

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
