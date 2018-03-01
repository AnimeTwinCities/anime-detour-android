package org.animetwincities.animedetour.guest;

import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import inkapplicaitons.android.logger.Logger;
import io.reactivex.Completable;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.fresco.FrescoObservables;
import org.animetwincities.rxfirebase.FirebaseObservables;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    final private FirebaseDatabase firebase;
    final private AppConfig config;
    final private ImagePipeline pipeline;
    final private Logger logger;

    @Inject
    public GuestRepository(FirebaseDatabase firebase, AppConfig config, ImagePipeline pipeline, Logger logger) {
        this.firebase = firebase;
        this.config = config;
        this.pipeline = pipeline;
        this.logger = logger;
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
        DatabaseReference databaseReference = this.firebase.getReference("guests").child(config.getSchedule()).child(id);

        return FirebaseObservables.fromQuery(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(snapshot -> snapshot.getValue(FirebaseGuest.class))
                .map(firebaseGuest -> Guest.from(id, firebaseGuest))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable preloadImages()
    {
        return observeGuests()
            .observeOn(Schedulers.io())
            .take(1)
            .flatMapCompletable(guests -> Observable.fromIterable(guests)
                .map(Guest::getImage)
                .flatMapCompletable(url -> FrescoObservables.prefetch(pipeline, url)
                    .doOnError(error -> logger.error(error, "Problem Fetching Image"))
                    .onErrorComplete()))
            .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return
     *          Observable containing all Guests found in Firebase (matching the key set
     *          on this class).
     */
    public Observable<List<Guest>> observeGuests()
    {
        DatabaseReference databaseReference = this.firebase.getReference("guests").child(config.getSchedule());
        GenericTypeIndicator<HashMap<String, FirebaseGuest>> typeIndicator =
                new GenericTypeIndicator<HashMap<String, FirebaseGuest>>() {};

        //Takes data snapshot from DatabaseReference, and puts it into a GenericTypeIndicator
        //which allows us access to the Firebase objects key/ID. Each key/object pair
        //is then mapped into a Guest DTO, and then returned as a List and wrapped in an Observable.
        return FirebaseObservables.fromQuery(databaseReference)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(snapshot -> snapshot.getValue(typeIndicator))
                .flatMap(map -> Observable.fromIterable(map.entrySet())
                        .map(guestPair ->  Guest.from(guestPair
                                .getKey(), guestPair.getValue()))
                        .toList()
                        .toObservable())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
