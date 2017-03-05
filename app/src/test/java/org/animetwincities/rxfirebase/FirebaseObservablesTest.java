package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FirebaseObservablesTest
{
    @Test
    public void createsObservables() throws Exception
    {
        Task fakeTask = mock(Task.class);

        Completable completable = FirebaseObservables.fromVoidTask(fakeTask);
        Observable observableTask = FirebaseObservables.fromTask(fakeTask);

        assertNotNull(completable);
        assertNotNull(observableTask);
    }
}
