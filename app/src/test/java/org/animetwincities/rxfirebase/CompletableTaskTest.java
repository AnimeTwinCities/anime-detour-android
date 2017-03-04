package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CompletableTaskTest
{
    @Test
    public void test() throws Exception
    {
        Task spyTask = mock(Task.class);
        CompletableEmitter fakeEmitter = mock(CompletableEmitter.class);

        CompletableTask test = new CompletableTask(spyTask);

        test.subscribe(fakeEmitter);

        verify(spyTask).addOnCompleteListener(any());
    }
}
