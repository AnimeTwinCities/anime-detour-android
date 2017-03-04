package org.animetwincities.animedetour.framework.stopwatch;

import inkapplicaitons.android.logger.Logger;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TimerFactoryTest
{
    @Test
    public void startForLimit() throws Exception
    {
        Logger fakeLogger = mock(Logger.class);

        TimerFactory factory = new TimerFactory(fakeLogger);

        LimitTimer test = factory.startForLimit("test", 5, TimeUnit.MILLISECONDS);

        assertNotNull(test);
    }
}
