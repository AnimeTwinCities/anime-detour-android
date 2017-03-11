package org.animetwincities.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import io.reactivex.ObservableEmitter;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QueryOnSubscribeTest {
    @Test
    public void subscribe() throws Exception {
        Query spyQuery = mock(Query.class);
        ObservableEmitter<DataSnapshot> fakeEmitter = mock(ObservableEmitter.class);

        QueryOnSubscribe onSubscribe = new QueryOnSubscribe(spyQuery);

        onSubscribe.subscribe(fakeEmitter);

        verify(spyQuery, times(1)).addValueEventListener(any());
    }
}
