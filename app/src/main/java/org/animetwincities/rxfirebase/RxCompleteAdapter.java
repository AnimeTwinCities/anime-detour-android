package org.animetwincities.rxfirebase;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import io.reactivex.CompletableEmitter;

/**
 * Adapts a GMS void onComplete listener to an Rx Completable.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
class RxCompleteAdapter implements OnCompleteListener<Void>
{
    final private CompletableEmitter emitter;

    public RxCompleteAdapter(CompletableEmitter emitter)
    {
        this.emitter = emitter;
    }

    @Override
    public void onComplete(@NonNull Task<Void> task)
    {
        if (task.isSuccessful()) {
            emitter.onComplete();
            return;
        }

        if (null == task.getException()) {
            this.emitter.onError(new UnknownTaskException());
            return;
        }

        this.emitter.onError(task.getException());
    }
}
