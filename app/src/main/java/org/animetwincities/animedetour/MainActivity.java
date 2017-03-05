package org.animetwincities.animedetour;

import android.widget.TextView;
import butterknife.BindView;
import com.facebook.drawee.view.SimpleDraweeView;
import inkapplicaitons.android.logger.Logger;
import inkapplications.android.layoutinjector.Layout;
import org.animetwincities.animedetour.framework.AppConfig;
import org.animetwincities.animedetour.framework.BaseActivity;
import org.animetwincities.animedetour.framework.auth.User;
import org.animetwincities.animedetour.framework.dependencyinjection.ActivityComponent;

import javax.inject.Inject;

@Layout(R.layout.main)
public class MainActivity extends BaseActivity
{
    @Inject
    Logger logger;

    @BindView(R.id.main_demo_image)
    SimpleDraweeView demoImage;

    @BindView(R.id.main_headline)
    TextView headline;

    @Override
    protected void onStart() {
        super.onStart();

        this.demoImage.setImageURI("https://placeimg.com/200/200/animals#.jpg");
    }

    @Override
    protected void onNewConfig(AppConfig config) {
        super.onNewConfig(config);

        this.headline.setText(config.getTestHeadline());
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.logger.debug("Hello Logging!");
    }

    @Override
    public void injectSelf(ActivityComponent component) {
        component.inject(this);
    }
}
