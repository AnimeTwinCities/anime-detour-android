package org.animetwincities.rxfirebase;

import com.google.android.gms.tasks.Task;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Adds an Rx Completable listener onto a Void Task.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class ResultTaskOnSubscribe<RESULT> implements ObservableOnSubscribe<RESULT>
{
    final private Task<RESULT> task;

    ResultTaskOnSubscribe(Task<RESULT> task)
    {
        this.task = task;
    }

    @Override
    public void subscribe(ObservableEmitter<RESULT> emitter) throws Exception
    {
        this.task.addOnCompleteListener(new RxResultAdapter<>(emitter));
    }
}
