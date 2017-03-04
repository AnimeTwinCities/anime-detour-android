package org.animetwincities.animedetour.framework.stopwatch;

import inkapplicaitons.android.logger.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Creates timers that are injected with the application logger.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Singleton
public class TimerFactory {
    final private Logger logger;

    @Inject
    public TimerFactory(Logger logger) {
        this.logger = logger;
    }

    /**
     * Create a timer with an upper limit and start it.
     *
     * @param name The name of the operation that is being timed.
     * @param timeLimit The maximum amount of time that the timer can take
     *                  before it is considered an error.
     * @param timeUnit The unit of time that the limit is expressed in.
     * @return A started timer that can be finished for a timer result.
     */
    public LimitTimer startForLimit(String name, long timeLimit, TimeUnit timeUnit) {
        LimitTimer timer = new LimitTimer(this.logger, name, timeLimit, timeUnit);
        timer.start();

        return timer;
    }
}
