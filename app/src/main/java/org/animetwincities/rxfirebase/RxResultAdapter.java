package org.animetwincities.rxfirebase;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import io.reactivex.Emitter;

/**
 * Adapts a GMS Complete listener to an Rx Completable.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class RxResultAdapter<RESULT> implements OnCompleteListener<RESULT>
{
    final private Emitter<RESULT> resultEmitter;

    public RxResultAdapter(Emitter<RESULT> resultEmitter)
    {
        this.resultEmitter = resultEmitter;
    }

    @Override
    public void onComplete(@NonNull Task<RESULT> task)
    {
        if (task.isSuccessful()) {
            resultEmitter.onNext(task.getResult());
            resultEmitter.onComplete();
            return;
        }

        if (null == task.getException()) {
            resultEmitter.onError(new UnknownTaskException());
            return;
        }

        resultEmitter.onError(task.getException());
    }
}
