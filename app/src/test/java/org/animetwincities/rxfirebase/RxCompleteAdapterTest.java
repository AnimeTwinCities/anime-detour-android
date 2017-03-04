package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RxCompleteAdapterTest
{
    @Test
    public void success() throws Exception
    {
        CompletableEmitter spyEmitter = mock(CompletableEmitter.class);
        Task fakeTask = mock(Task.class);
        when(fakeTask.isSuccessful()).thenReturn(true);

        RxCompleteAdapter rxCompleteAdapter = new RxCompleteAdapter(spyEmitter);
        rxCompleteAdapter.onComplete(fakeTask);

        verify(spyEmitter).onComplete();
    }

    @Test
    public void error() throws Exception
    {
        CompletableEmitter spyEmitter = mock(CompletableEmitter.class);
        Task fakeTask = mock(Task.class);
        Exception fakeException = mock(Exception.class);

        when(fakeTask.isSuccessful()).thenReturn(false);
        when(fakeTask.getException()).thenReturn(fakeException);

        RxCompleteAdapter rxCompleteAdapter = new RxCompleteAdapter(spyEmitter);
        rxCompleteAdapter.onComplete(fakeTask);

        verify(spyEmitter).onError(fakeException);
    }

    @Test
    public void unknownError() throws Exception
    {
        CompletableEmitter spyEmitter = mock(CompletableEmitter.class);
        Task fakeTask = mock(Task.class);

        when(fakeTask.isSuccessful()).thenReturn(false);
        when(fakeTask.getException()).thenReturn(null);

        RxCompleteAdapter rxCompleteAdapter = new RxCompleteAdapter(spyEmitter);
        rxCompleteAdapter.onComplete(fakeTask);

        verify(spyEmitter).onError(isA(UnknownTaskException.class));
    }
}
