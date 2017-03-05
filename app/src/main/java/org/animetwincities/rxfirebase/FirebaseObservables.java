package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.Completable;
import io.reactivex.Observable;

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
        return Completable.create(new CompletableTask(task));
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
        return Observable.create(new ResultTask<>(authResultTask));
    }
}
