package cloud.cluster.sim.clustersimulator.dto;

/**
 * A {@link MicroDataCenter}
 * represents the quality requirements for a group of resources, sharing the same SLA.
 */
public class MicroDataCenter {
    private String name;
    private double sla;
    private long cost;

    public MicroDataCenter() {
    }

    public MicroDataCenter(String name, double sla, long cost) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MicroDataCenter that = (MicroDataCenter) o;

        if (Double.compare(that.sla, sla) != 0) return false;
        if (cost != that.cost) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(sla);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (cost ^ (cost >>> 32));
        return result;
    }
}
