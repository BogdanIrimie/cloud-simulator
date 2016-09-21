package cloud.cluster.sim.clustersimulator.dto;

public class Chance {
    private double favorable;
    private int possible;
    private double chance;

    public Chance(double favorable, int possible) {
        this.favorable = favorable;
        this.possible = possible;
    }

    public double getFavorable() {
        return favorable;
    }

    public void setFavorable(double favorable) {
        this.favorable = favorable;
    }

    public int getPossible() {
        return possible;
    }

    public void setPossible(int possible) {
        this.possible = possible;
    }

    public double getChance() {
        chance = favorable / possible;
        return chance;
    }

}
