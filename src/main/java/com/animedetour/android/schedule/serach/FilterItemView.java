/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.android.schedule.serach;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.animedetour.android.R;

/**
 * A simple list item representing a filter type for the user to select.
 *
 * This just displays a card containing the title of the filter.
 *
 * @author Maxwell Vandervelde (Max@MaxVandervelde.com)
 */
public class FilterItemView extends FrameLayout
{
    final private TextView title;

    public FilterItemView(Context context)
    {
        this(context, null);
    }

    public FilterItemView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public FilterItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_filter_item, this);

        this.title = (TextView) this.findViewById(R.id.view_filter_item_title);
    }

    /**
     * Change the displayed filter name.
     */
    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }
}
