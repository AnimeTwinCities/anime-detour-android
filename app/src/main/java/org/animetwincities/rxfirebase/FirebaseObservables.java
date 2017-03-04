package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.Completable;

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
}
