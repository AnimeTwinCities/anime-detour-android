package org.animetwincities.rxfirebase;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;

/**
 * Adapts a GMS Complete listener to an Rx Completable.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class RxResultAdapter<RESULT> implements OnCompleteListener<RESULT>, Disposable
{
    private ObservableEmitter<RESULT> emitter;

    public RxResultAdapter(ObservableEmitter<RESULT> emitter)
    {
        this.emitter = emitter;
    }

    @Override
    public void onComplete(@NonNull Task<RESULT> task)
    {
        if (null == emitter || emitter.isDisposed()) {
            return;
        }

        if (task.isSuccessful()) {
            emitter.onNext(task.getResult());
            emitter.onComplete();
            return;
        }

        if (null == task.getException()) {
            emitter.onError(new UnknownTaskException());
            return;
        }

        emitter.onError(task.getException());
    }

    @Override
    public void dispose() {
        emitter = null;
    }

    @Override
    public boolean isDisposed() {
        return emitter == null;
    }
}
