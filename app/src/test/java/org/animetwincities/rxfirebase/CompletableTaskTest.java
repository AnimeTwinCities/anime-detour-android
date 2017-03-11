package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CompletableTaskTest
{
    @Test
    public void test() throws Exception
    {
        Task spyTask = mock(Task.class);
        CompletableEmitter fakeEmitter = mock(CompletableEmitter.class);

        CompletableTaskOnSubscribe test = new CompletableTaskOnSubscribe(spyTask);

        test.subscribe(fakeEmitter);

        verify(spyTask).addOnCompleteListener(any());
    }
}
