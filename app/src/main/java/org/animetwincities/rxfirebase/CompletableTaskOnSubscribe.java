package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Adds an Rx Completable listener onto a Void Task.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class CompletableTaskOnSubscribe implements CompletableOnSubscribe
{
    final private CompositeDisposable disposables;
    final private Task<Void> task;

    CompletableTaskOnSubscribe(CompositeDisposable disposables, Task<Void> task)
    {
        this.disposables = disposables;
        this.task = task;
    }

    @Override
    public void subscribe(CompletableEmitter e) throws Exception
    {
        RxCompleteAdapter listener = new RxCompleteAdapter(e);
        task.addOnCompleteListener(listener);
        disposables.add(listener);
    }
}
