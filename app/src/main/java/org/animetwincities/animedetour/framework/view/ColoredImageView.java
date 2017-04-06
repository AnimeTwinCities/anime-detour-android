/*
 * Copyright (c) 2014 Ink Applications, LLC.
 * Distributed under the MIT License (http://opensource.org/licenses/MIT)
 */
package org.animetwincities.animedetour.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.animetwincities.animedetour.R;

/**
 * Image view that is overlayed with a color.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class ColoredImageView extends ImageView
{
    public ColoredImageView(Context context)
    {
        super(context);
    }

    public ColoredImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(context, attrs);
    }

    public ColoredImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    /** Shared Constructor Logic */
    private void init(Context context, AttributeSet attrs)
    {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.ColoredImageView,
            0,
            0
        );

        int color = attributes.getColor(R.styleable.ColoredImageView_colorOverlay, 0);
        this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Overlay a color on top of the source image.
     *
     * @param color The color to fill the image with.
     */
    public void setColorOverlay(int color)
    {
        this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
}
