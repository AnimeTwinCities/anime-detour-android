package org.animetwincities.animedetour.framework.stopwatch;

import inkapplicaitons.android.logger.Logger;
import inkapplications.guava.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Stopwatch with an upper time limit, for debugging.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class LimitTimer {
    final private Logger logger;
    final private Stopwatch stopwatch;
    final private String name;
    final private long timeLimit;
    final private TimeUnit timeUnit;

    public LimitTimer(Logger logger, String name, long timeLimit, TimeUnit timeUnit) {
        this.logger = logger;
        this.stopwatch = Stopwatch.createUnstarted();
        this.name = name;
        this.timeLimit = timeLimit;
        this.timeUnit = timeUnit;
    }

    /**
     * Stop the timer and log the result.
     *
     * Logs an error message if the timer surpassed the limit specified.
     */
    public void finish() {
        this.stopwatch.stop();
        if (stopwatch.elapsed(timeUnit) > timeLimit) {
            this.logger.error("%s operation took too long: %s", this.name, this.stopwatch);
        } else {
            this.logger.debug("%s operation took %s", this.name, this.stopwatch);
        }
    }

    /** Start the internal timer. */
    public void start() {
        this.stopwatch.start();
    }
}
