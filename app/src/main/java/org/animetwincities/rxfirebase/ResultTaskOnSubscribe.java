package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;

import java.lang.ref.WeakReference;

/**
 * Adds an Rx Completable listener onto a Void Task.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class ResultTaskOnSubscribe<RESULT> implements ObservableOnSubscribe<RESULT>
{
    final private CompositeDisposable disposables;
    final private Task<RESULT> task;

    ResultTaskOnSubscribe(CompositeDisposable disposables, Task<RESULT> task)
    {
        this.disposables = disposables;
        this.task = task;
    }

    @Override
    public void subscribe(ObservableEmitter<RESULT> emitter) throws Exception
    {
        RxResultAdapter<RESULT> listener = new RxResultAdapter<>(emitter);
        task.addOnCompleteListener(listener);
        disposables.add(listener);
    }
}
