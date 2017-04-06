package org.animetwincities.animedetour.framework.fresco;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import io.reactivex.Completable;

public class FrescoObservables
{
    public static Completable prefetch(ImagePipeline pipeline, String url)
    {
        DataSource<Void> data = pipeline.prefetchToDiskCache(ImageRequest.fromUri(url), null);
        return Completable.create(emitter -> data.subscribe(new ImageLoadCompleteAdapter(emitter), CallerThreadExecutor.getInstance()));
    }
}
