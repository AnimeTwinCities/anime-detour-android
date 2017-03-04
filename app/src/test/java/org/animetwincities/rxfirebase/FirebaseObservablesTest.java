package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.Completable;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FirebaseObservablesTest
{
    @Test
    public void fromVoidTask() throws Exception
    {
        Task fakeTask = mock(Task.class);

        Completable completable = FirebaseObservables.fromVoidTask(fakeTask);

        assertNotNull(completable);
    }
}
