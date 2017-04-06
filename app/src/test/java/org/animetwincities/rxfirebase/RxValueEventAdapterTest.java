package org.animetwincities.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import io.reactivex.ObservableEmitter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RxValueEventAdapterTest
{
    @Mock
    ObservableEmitter<DataSnapshot> emitter;

    @Test
    public void newValue() throws Exception
    {
        RxValueEventAdapter adapter = new RxValueEventAdapter(emitter);

        adapter.onDataChange(null);

        verify(emitter, times(1)).onNext(null);
        verify(emitter, never()).onError(any());
        verify(emitter, never()).onComplete();
    }

    @Test
    public void error() throws Exception
    {
        RxValueEventAdapter adapter = new RxValueEventAdapter(emitter);

        adapter.onCancelled(mock(DatabaseError.class));

        verify(emitter, never()).onNext(any());
        verify(emitter, times(1)).onError(isA(ThrowableDatabaseError.class));
        verify(emitter, never()).onComplete();
    }
}
