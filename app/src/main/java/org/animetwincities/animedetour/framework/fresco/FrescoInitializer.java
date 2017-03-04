package org.animetwincities.animedetour.framework.fresco;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.inkapplications.android.applicationlifecycle.Initializer;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Initialize the Fresco library on application create.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Singleton
public class FrescoInitializer extends Initializer
{
    final private OkHttpClient httpClient;

    @Inject
    public FrescoInitializer(OkHttpClient httpClient)
    {
        this.httpClient = httpClient;
    }

    @Override
    public void onCreate(Application application)
    {
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
            .newBuilder(application, this.httpClient)
            .build();
        Fresco.initialize(application, config);
    }
}
