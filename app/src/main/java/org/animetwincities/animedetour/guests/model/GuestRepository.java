package org.animetwincities.animedetour.guests.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import org.animetwincities.rxfirebase.FirebaseObservables;

import java.util.HashMap;
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


    public Observable<Guest> observeGuest(String id) {
        DatabaseReference databaseReference = this.firebase.getReference("guests").child(YEAR_CHILD_KEY);
        GenericTypeIndicator<HashMap<String, FirebaseGuest>> typeIndicator =
                new GenericTypeIndicator<HashMap<String, FirebaseGuest>>() {};

        return FirebaseObservables.fromQuery(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(snapshot -> snapshot.getValue(typeIndicator))
                .filter(stringFirebaseGuestHashMap -> stringFirebaseGuestHashMap != null)
                .flatMap(map -> Observable.fromIterable(map.entrySet())
                        .map(stringFirebaseGuestEntry ->  Guest.from(stringFirebaseGuestEntry
                                .getKey(), stringFirebaseGuestEntry.getValue()))
                        .filter(guest -> guest.getId().equals(id)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Guest>> observeGuests() {
        DatabaseReference databaseReference = this.firebase.getReference("guests").child(YEAR_CHILD_KEY);
        GenericTypeIndicator<HashMap<String, FirebaseGuest>> typeIndicator =
                new GenericTypeIndicator<HashMap<String, FirebaseGuest>>() {};

        return FirebaseObservables.fromQuery(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(snapshot -> snapshot.getValue(typeIndicator))
                .filter(stringFirebaseGuestHashMap -> stringFirebaseGuestHashMap != null)
                .flatMap(map -> Observable.fromIterable(map.entrySet())
                        .map(guestPair ->  Guest.from(guestPair
                                .getKey(), guestPair.getValue()))
                        .toList()
                        .toObservable())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
