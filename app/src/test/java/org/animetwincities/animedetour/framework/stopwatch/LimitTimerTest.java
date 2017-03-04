package org.animetwincities.animedetour.framework.stopwatch;

import inkapplicaitons.android.logger.Logger;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LimitTimerTest
{
    @Test
    public void time() throws Exception
    {
        Logger spyLogger = mock(Logger.class);

        LimitTimer timer = new LimitTimer(spyLogger, "test", 500, TimeUnit.MILLISECONDS);
        timer.start();
        timer.finish();

        verify(spyLogger).debug(anyString(), anyString(), any());
    }

    @Test
    public void timeFailure() throws Exception
    {
        Logger spyLogger = mock(Logger.class);

        LimitTimer timer = new LimitTimer(spyLogger, "test", 0, TimeUnit.MILLISECONDS);
        timer.start();
        Thread.sleep(1);

        timer.finish();

        verify(spyLogger).error(anyString(), anyString(), any());
    }
}
