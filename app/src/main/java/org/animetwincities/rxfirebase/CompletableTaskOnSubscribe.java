package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Adds an Rx Completable listener onto a Void Task.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class CompletableTaskOnSubscribe implements CompletableOnSubscribe
{
    final private Task<Void> task;

    CompletableTaskOnSubscribe(Task<Void> task)
    {
        this.task = task;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception
    {
        this.task.addOnCompleteListener(new RxCompleteAdapter(e));
    }
}
