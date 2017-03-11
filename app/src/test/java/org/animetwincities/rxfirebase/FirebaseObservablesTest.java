package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
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
        Query fakeQuery = mock(Query.class);

        Completable completable = FirebaseObservables.fromVoidTask(fakeTask);
        Observable observableTask = FirebaseObservables.fromTask(fakeTask);
        Observable<DataSnapshot> query = FirebaseObservables.fromQuery(fakeQuery);

        assertNotNull(completable);
        assertNotNull(observableTask);
        assertNotNull(query);
    }
}
