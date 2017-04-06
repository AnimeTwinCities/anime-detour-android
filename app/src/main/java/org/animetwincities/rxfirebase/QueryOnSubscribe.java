package org.animetwincities.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Subscribes a listener to a firebase Query.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class QueryOnSubscribe implements ObservableOnSubscribe<DataSnapshot>
{
    final private CompositeDisposable disposables;
    final private Query query;

    public QueryOnSubscribe(CompositeDisposable disposables, Query query)
    {
        this.disposables = disposables;
        this.query = query;
    }

    @Override
    public void subscribe(ObservableEmitter<DataSnapshot> e) throws Exception
    {
        RxValueEventAdapter listener = new RxValueEventAdapter(e);
        query.addValueEventListener(listener);
        disposables.add(new ListenterDisposable(listener, query));
    }
}
