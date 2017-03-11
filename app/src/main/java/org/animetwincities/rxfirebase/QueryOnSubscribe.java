package org.animetwincities.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Subscribes a listener to a firebase Query.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class QueryOnSubscribe implements ObservableOnSubscribe<DataSnapshot>
{
    final private Query query;

    public QueryOnSubscribe(Query query)
    {
        this.query = query;
    }

    @Override
    public void subscribe(ObservableEmitter<DataSnapshot> e) throws Exception
    {
        query.addValueEventListener(new RxValueEventAdapter(e));
    }
}
