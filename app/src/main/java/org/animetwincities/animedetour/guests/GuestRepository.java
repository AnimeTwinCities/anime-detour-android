package org.animetwincities.animedetour.guests;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.animetwincities.rxfirebase.FirebaseObservables;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kenton on 3/11/17.
 */
@Singleton
public class GuestRepository
{

    final private static String YEAR_CHILD_KEY = "ad-2017";

    final private FirebaseDatabase firebase;

    @Inject
    public GuestRepository() {
        this.firebase = FirebaseDatabase.getInstance();
    }

    public Observable<List<Guest>> observeGuests() {
        DatabaseReference databaseReference = this.firebase.getReference("guests").child(YEAR_CHILD_KEY);

        return FirebaseObservables.fromQuery(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(DataSnapshot::getChildren)
                .flatMap(dataSnapshots -> Observable.fromIterable(dataSnapshots)
                        .map(data -> data.getValue(FirebaseGuest.class))
                        .map(Guest::from)
                        .toList()
                        .toObservable())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
