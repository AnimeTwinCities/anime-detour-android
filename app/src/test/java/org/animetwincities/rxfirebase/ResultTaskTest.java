package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.ObservableEmitter;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ResultTaskTest
{
    @Test
    public void subscribe() throws Exception
    {
        Task spyTask = mock(Task.class);
        ObservableEmitter fakeEmitter = mock(ObservableEmitter.class);
        ResultTaskOnSubscribe resultTask = new ResultTaskOnSubscribe(spyTask);

        resultTask.subscribe(fakeEmitter);

        verify(spyTask, times(1)).addOnCompleteListener(any());
    }
}
