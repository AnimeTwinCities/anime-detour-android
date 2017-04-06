package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;
import io.reactivex.disposables.CompositeDisposable;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CompletableTaskTest
{
    @Test
    public void test() throws Exception
    {
        Task spyTask = mock(Task.class);
        CompositeDisposable disposables = mock(CompositeDisposable.class);
        CompletableEmitter fakeEmitter = mock(CompletableEmitter.class);

        CompletableTaskOnSubscribe test = new CompletableTaskOnSubscribe(disposables, spyTask);

        test.subscribe(fakeEmitter);

        verify(spyTask).addOnCompleteListener(any());
    }
}
