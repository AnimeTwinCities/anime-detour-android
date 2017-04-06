package org.animetwincities.animedetour.schedule;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inkapplications.logger.RxLogger;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.rxfirebase.FirebaseObservables;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class ScheduleRepository
{
    final private RxLogger logger;
    final private EventTransformer transformer;
    final private FirebaseDatabase firebase;
    final private AppConfig appConfig;

    @Inject
    public ScheduleRepository(RxLogger logger, EventTransformer transformer, FirebaseDatabase firebase, AppConfig appConfig)
    {
        this.logger = logger;
        this.transformer = transformer;
        this.firebase = firebase;
        this.appConfig = appConfig;
    }

    public Observable<List<LocalDate>> observeDays()
    {
        logger.trace("Observing Days in Schedule");

        return this.observeEvents()
            .observeOn(Schedulers.computation())
            .flatMap(events -> Observable.fromIterable(events)
                .groupBy(event -> event.getStart().toLocalDate())
                .toList()
                .toObservable()
            )
            .flatMap(groupedObservables -> {
                List<LocalDate> days = new ArrayList<>();
                for (GroupedObservable<LocalDate, Event> observable : groupedObservables) {
                    days.add(observable.getKey());
                }
                Collections.sort(days);
                return Observable.just(days);
            })
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Event>> observeEvents()
    {
        logger.trace("Observing ALL Events in Schedule");
        DatabaseReference reference = this.firebase.getReference("events").child(appConfig.getSchedule());

        return FirebaseObservables.fromQuery(reference)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(transformer::snapshotToEventList)
            .flatMap(events -> Observable.fromIterable(events).toSortedList(Event.TIME_SORT).toObservable())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Event> observeEvent(String id)
    {
        DatabaseReference reference = this.firebase.getReference("events").child(appConfig.getSchedule()).child(id);

        return FirebaseObservables.fromQuery(reference)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(snapshot -> snapshot.getValue(FirebaseEvent.class))
            .map(eventData -> new Event(id, eventData))
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<Event>> observeEventsOnDay(LocalDate day) {
        logger.trace("Observing Events on: %s", day.format(DateTimeFormatter.ISO_LOCAL_DATE));
        DatabaseReference reference = this.firebase.getReference("events").child(appConfig.getSchedule());

        return FirebaseObservables.fromQuery(reference)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(transformer::snapshotToEventList)
            .flatMap(events -> Observable.fromIterable(events)
                .filter(event -> event.getStart().toLocalDate().equals(day) || (event.getStart().toLocalDate().isBefore(day) && (event.getEnd().toLocalDate().isAfter(day) || event.getEnd().toLocalDate().equals(day))))
                .toSortedList(Event.TIME_SORT)
                .toObservable()
            )
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<String>> observeFavorites(String user)
    {
        logger.trace("Observing Favorited events for user: %s", user);
        DatabaseReference reference = this.firebase.getReference("favorites").child(appConfig.getSchedule()).child(user);

        return FirebaseObservables.fromQuery(reference)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(DataSnapshot::getChildren)
            .flatMap(dataSnapshots -> Observable.fromIterable(dataSnapshots)
                .filter(snapshot -> snapshot.getValue(Boolean.class))
                .map(DataSnapshot::getKey)
                .toList()
                .toObservable()
            )
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable favoriteEvent(String user, String event)
    {
        DatabaseReference reference = firebase.getReference("favorites").child(appConfig.getSchedule()).child(user).child(event);

        Task<Void> task = reference.setValue(true);

        return FirebaseObservables.fromVoidTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable unfavoriteEvent(String user, String event)
    {
        DatabaseReference reference = firebase.getReference("favorites").child(appConfig.getSchedule()).child(user).child(event);

        Task<Void> task = reference.removeValue();

        return FirebaseObservables.fromVoidTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}
