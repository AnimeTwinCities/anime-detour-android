package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.ObservableEmitter;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RxResultAdapterTest {
    @Test
    public void success() throws Exception {
        ObservableEmitter spyEmitter = mock(ObservableEmitter.class);
        Object fakeResult = new Object();
        Task fakeTask = mock(Task.class);
        when(fakeTask.isSuccessful()).thenReturn(true);
        when(fakeTask.getResult()).thenReturn(fakeResult);

        RxResultAdapter adapter = new RxResultAdapter(spyEmitter);
        adapter.onComplete(fakeTask);

        verify(spyEmitter, times(1)).onNext(fakeResult);
        verify(spyEmitter, times(1)).onComplete();
        verify(spyEmitter, never()).onError(any());
    }

    @Test
    public void failure() throws Exception {
        ObservableEmitter spyEmitter = mock(ObservableEmitter.class);
        Exception fakeException = new Exception();
        Task fakeTask = mock(Task.class);
        when(fakeTask.isSuccessful()).thenReturn(false);
        when(fakeTask.getException()).thenReturn(fakeException);

        RxResultAdapter adapter = new RxResultAdapter(spyEmitter);
        adapter.onComplete(fakeTask);

        verify(spyEmitter, never()).onNext(any());
        verify(spyEmitter, never()).onComplete();
        verify(spyEmitter, times(1)).onError(fakeException);
    }

    @Test
    public void unknownFailure() throws Exception {
        ObservableEmitter spyEmitter = mock(ObservableEmitter.class);
        Task fakeTask = mock(Task.class);
        when(fakeTask.isSuccessful()).thenReturn(false);
        when(fakeTask.getException()).thenReturn(null);

        RxResultAdapter adapter = new RxResultAdapter(spyEmitter);
        adapter.onComplete(fakeTask);

        verify(spyEmitter, never()).onNext(any());
        verify(spyEmitter, never()).onComplete();
        verify(spyEmitter, times(1)).onError(isA(UnknownTaskException.class));
    }
}
