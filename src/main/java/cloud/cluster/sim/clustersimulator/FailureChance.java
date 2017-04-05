package cloud.cluster.sim.clustersimulator;

import cloud.cluster.sim.clustersimulator.dto.Chance;
import cloud.cluster.sim.utilities.SimSettingsExtractor;

public class FailureChance {

    private static final int NUMBER_OF_DAYS_FOR_SLA = 30;
    private static final int NUMBER_OF_SECONDS_IN_DAY = 86400;          // 24 hours * 60 minutes * 60 seconds

    private int totalNumberOfSeconds = NUMBER_OF_DAYS_FOR_SLA * NUMBER_OF_SECONDS_IN_DAY;

    Chance compute(double sla) {
        long downtime;
        double mttr, failures;

        downtime = (long) (totalNumberOfSeconds *  (100 - sla)) / 100;
        mttr = SimSettingsExtractor.getSimulationSettings().getMttr();
        failures = downtime / mttr;

        return new Chance(failures, totalNumberOfSeconds);
    }
}
