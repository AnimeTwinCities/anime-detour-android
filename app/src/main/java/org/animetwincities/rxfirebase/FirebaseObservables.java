package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Creates Rx Observable objects from Firebase/Google callbacks.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class FirebaseObservables
{
    /**
     * Create a Completable from a void task.
     *
     * We can't go passing nulls around for void tasks, so this is necessary
     * to observe some of the more basic events in firebase such as config
     * updates.
     *
     * @param task A Task to listen to.
     * @return A Completable that must be subscribed to to receive success events.
     */
    public static Completable fromVoidTask(Task<Void> task)
    {
        CompositeDisposable disposables = new CompositeDisposable();
        return Completable.create(new CompletableTaskOnSubscribe(disposables, task))
            .doFinally(disposables::dispose);
    }

    /**
     * Create an Observable from a Task.
     *
     * Creates an observable that will emit the result of the task or any errors
     * that were returned.
     *
     * @param authResultTask A task to listen to for results.
     * @param <RESULT> The type of the result to receive from the task.
     * @return An Observable that emits results of the task.
     */
    public static <RESULT> Observable<RESULT> fromTask(Task<RESULT> authResultTask)
    {
        CompositeDisposable disposables = new CompositeDisposable();
        return Observable.create(new ResultTaskOnSubscribe<>(disposables, authResultTask))
            .doFinally(disposables::dispose);
    }

    /**
     * Create an Observable from a Firebase Query or Reference.
     *
     * @param query The Firebase Query to be observed. This can also be a
     *              Database reference, which is a Query.
     * @return An Observable that emits database snapshots for the query/ref.
     */
    public static Observable<DataSnapshot> fromQuery(Query query)
    {
        CompositeDisposable disposables = new CompositeDisposable();
        return Observable.create(new QueryOnSubscribe(disposables, query))
            .doFinally(disposables::dispose);
    }
}
