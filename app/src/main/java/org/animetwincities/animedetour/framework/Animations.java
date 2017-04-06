package org.animetwincities.animedetour.framework;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import io.reactivex.functions.Action;

public class Animations
{
    static public Animator createCircularReveal(View view, int centerX, int centerY, float startRadius, float endRadius)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        }

        return new AnimatorSet();
    }

    static public void reveal(View target, int duration, int delay)
    {
        target.setVisibility(View.INVISIBLE);
        target.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int cx = target.getWidth() / 2;
            int cy = target.getHeight() / 2;
            Animator anim = Animations.createCircularReveal(target, cx, cy, 0, (float) Math.hypot(cx, cy));
            anim.addListener(Animations.actionOnStart(() -> target.setVisibility(View.VISIBLE)));
            anim.setStartDelay(delay);
            anim.setDuration(duration);
            anim.start();
        });
    }

    static public void fadeIn(View target, int duration, int offset)
    {
        AlphaAnimation fade = new AlphaAnimation(0f, 1f);
        fade.setDuration(duration);
        fade.setStartOffset(offset);
        target.startAnimation(fade);
        target.setVisibility(View.VISIBLE);
    }

    static public void fadeIn(View target)
    {
        fadeIn(target, 300, 0);
    }

    static public AnimatorListenerAdapter actionOnStart(Action action)
    {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                doAction(action);
            }
        };
    }

    static public AnimatorListenerAdapter actionOnEnd(Action action)
    {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationStart(animation);
                doAction(action);
            }
        };
    }

    static private void doAction(Action action)
    {
        try {
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
