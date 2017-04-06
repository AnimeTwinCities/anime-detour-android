package org.animetwincities.animedetour.framework.fresco;

import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import io.reactivex.CompletableEmitter;

public class ImageLoadCompleteAdapter implements DataSubscriber<Void>
{
    final private CompletableEmitter emitter;

    public ImageLoadCompleteAdapter(CompletableEmitter emitter) {
        this.emitter = emitter;
    }

    @Override
    public void onNewResult(DataSource<Void> dataSource) {
        emitter.onComplete();
    }

    @Override
    public void onFailure(DataSource<Void> dataSource) {
        emitter.onError(dataSource.getFailureCause());
    }
    @Override
    public void onCancellation(DataSource<Void> dataSource) {
        emitter.onComplete();
    }

    @Override public void onProgressUpdate(DataSource<Void> dataSource) {}
}
