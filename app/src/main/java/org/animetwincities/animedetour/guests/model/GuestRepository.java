package org.animetwincities.animedetour.guests.model;

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
 * Repository class wrapping business logic to fetch {@link Guest} objects from Firebase. All methods
 * handle transforming from {@link FirebaseGuest} to Guest, and return their results in an Rx
 * {@link Observable}
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


    /**
     * Returns an Observable wrapping a specific Guest, as filtered by the provided ID string.
     *
     * NOTE: This method performs a fresh query on all guests, due to some Firebase weirdness. This
     * may result in some delay on larger guest data sets, since filtering occurs client side.
     *
     * @param id
     *      Guest ID to filter on
     * @return
     *          {@link Guest} matching provided ID.
     */
    public Observable<Guest> observeGuest(String id)
    {
        DatabaseReference databaseReference = this.firebase.getReference("guests").child(YEAR_CHILD_KEY);
        GenericTypeIndicator<HashMap<String, FirebaseGuest>> typeIndicator =
                new GenericTypeIndicator<HashMap<String, FirebaseGuest>>() {};

        //Takes data snapshot from DatabaseReference, and puts it into a GenericTypeIndicator
        //which allows us access to the Firebase objects key/ID. Results are then filtered to find
        //a match for the provided ID, and then the key/object pair is mapped into a Guest DTO.
        return FirebaseObservables.fromQuery(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(snapshot -> snapshot.getValue(typeIndicator))
                .filter(stringFirebaseGuestHashMap -> stringFirebaseGuestHashMap != null)
                .flatMap(map -> Observable.fromIterable(map.entrySet())
                        .filter(stringFirebaseGuestEntry -> stringFirebaseGuestEntry.getKey().equals(id))
                        .map(stringFirebaseGuestEntry ->  Guest.from(stringFirebaseGuestEntry
                                .getKey(), stringFirebaseGuestEntry.getValue()))
                        .observeOn(AndroidSchedulers.mainThread()));
    }

    /**
     * @return
     *          Observable containing all Guests found in Firebase (matching the key set
     *          on this class).
     */
    public Observable<List<Guest>> observeGuests()
    {
        DatabaseReference databaseReference = this.firebase.getReference("guests").child(YEAR_CHILD_KEY);
        GenericTypeIndicator<HashMap<String, FirebaseGuest>> typeIndicator =
                new GenericTypeIndicator<HashMap<String, FirebaseGuest>>() {};

        //Takes data snapshot from DatabaseReference, and puts it into a GenericTypeIndicator
        //which allows us access to the Firebase objects key/ID. Each key/object pair
        //is then mapped into a Guest DTO, and then returned as a List and wrapped in an Observable.
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
