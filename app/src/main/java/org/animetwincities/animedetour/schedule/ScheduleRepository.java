package org.animetwincities.animedetour.schedule;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.animetwincities.rxfirebase.FirebaseObservables;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ScheduleRepository
{
    final private FirebaseDatabase firebase;

    @Inject
    public ScheduleRepository()
    {
        this.firebase = FirebaseDatabase.getInstance();
    }

    public Observable<List<Event>> observeEvents(String schedule)
    {
        DatabaseReference reference = this.firebase.getReference("events").child(schedule);

        return FirebaseObservables.fromQuery(reference)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(DataSnapshot::getChildren)
            .flatMap(dataSnapshots -> Observable.fromIterable(dataSnapshots)
                .map(data -> data.getValue(FirebaseEvent.class))
                .map(event -> new Event(event.name))
                .toList()
                .toObservable())
            .observeOn(AndroidSchedulers.mainThread());
    }
}
