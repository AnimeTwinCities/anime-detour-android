package org.animetwincities.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import io.reactivex.ObservableEmitter;

/**
 * Adapts firebase Value events to an Rx emitter.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class RxValueEventAdapter implements ValueEventListener
{
    final private ObservableEmitter<DataSnapshot> emitter;

    public RxValueEventAdapter(ObservableEmitter<DataSnapshot> emitter)
    {
        this.emitter = emitter;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot)
    {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onNext(dataSnapshot);
    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {
        if (emitter.isDisposed()) {
            return;
        }
        emitter.onError(new ThrowableDatabaseError(databaseError));
    }
}
