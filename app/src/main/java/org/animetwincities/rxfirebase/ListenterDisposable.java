package org.animetwincities.rxfirebase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import io.reactivex.disposables.Disposable;

/**
 * Holds on to a Firebase ValueEventListener to be unsubscribed on disposal of
 * the Rx chain.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class ListenterDisposable implements Disposable
{
    final private ValueEventListener listener;
    final private Query query;

    private boolean disposed = false;

    public ListenterDisposable(ValueEventListener listener, Query query) {
        this.listener = listener;
        this.query = query;
    }

    @Override
    public void dispose() {
        query.removeEventListener(listener);
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }
}
