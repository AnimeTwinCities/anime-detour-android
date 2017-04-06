package org.animetwincities.rxfirebase;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;
import io.reactivex.disposables.Disposable;

/**
 * Adapts a GMS void onComplete listener to an Rx Completable.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class RxCompleteAdapter implements OnCompleteListener<Void>, Disposable
{
    private CompletableEmitter emitter;

    public RxCompleteAdapter(CompletableEmitter emitter)
    {
        this.emitter = emitter;
    }

    @Override
    public void onComplete(@NonNull Task<Void> task)
    {
        if(emitter.isDisposed()) {
            return;
        }

        if (task.isSuccessful()) {
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
