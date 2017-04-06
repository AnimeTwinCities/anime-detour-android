package org.animetwincities.animedetour.framework;

import android.app.Activity;
import android.os.Build;
import android.transition.TransitionSet;
import android.view.Window;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;

public class Transitions
{
    static public void finishAfterTransition(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAfterTransition();
        } else {
            activity.finish();
        }
    }

    static public void postponeEnterTransition(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.postponeEnterTransition();
        }
    }

    static public void startPostponedEnterTransition(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startPostponedEnterTransition();
        }
    }

    static public void draweeCropTransition(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            ScalingUtils.ScaleType scaleType = ScalingUtils.ScaleType.CENTER_CROP;
            TransitionSet transitionSet = DraweeTransition.createTransitionSet(scaleType, scaleType);

            window.setSharedElementEnterTransition(transitionSet);
            window.setSharedElementReturnTransition(transitionSet);
        }
    }
}
