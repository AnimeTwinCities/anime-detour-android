/*
 * Copyright (c) 2015 Ink Applications, LLC.
 * Distributed under the MIT License (http://opensource.org/licenses/MIT)
 */
package org.animetwincities.animedetour.schedule;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.inkapplications.android.widget.recyclerview.ItemViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Binds items into views in a type-safe and simple way.
 *
 * Was this so god damn hard, Google?
 * This utilizes the item view binder provided in the ink simple recycler view
 * to do its bindings.
 *
 * @param <VIEW> The view type that is used to display the item.
 * @param <ITEM> The data model for the items in the list.
 */
public class ItemAdapter<VIEW extends View, ITEM> extends BaseAdapter
{
    /**
     * Binder used to update and create views.
     */
    final private ItemViewBinder<VIEW, ITEM> itemBinder;

    /**
     * Collection of items to be displayed in the list.
     */
    private List<ITEM> items;

    public ItemAdapter(ItemViewBinder<VIEW, ITEM> itemBinder)
    {
        this.items = new ArrayList<>();
        this.itemBinder = itemBinder;
    }

    @Override
    final public int getCount()
    {
        return this.items.size();
    }

    @Override
    final public ITEM getItem(int i)
    {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    final public View getView(int i, View view, ViewGroup viewGroup)
    {
        VIEW realView = this.getRealView(i, view, viewGroup);
        this.itemBinder.bindView(this.getItem(i), realView);

        return realView;
    }

    /**
     * Create a for-real view out of the potentially recycled view provided.
     *
     * @return Either a recycled or new view.
     */
    private VIEW getRealView(int i, View recycled, ViewGroup viewGroup)
    {
        if (null == recycled) {
            return this.itemBinder.createView(viewGroup, i);
        }
        return (VIEW) recycled;
    }

    /**
     * Set a new list of items to display.
     *
     * @param items Items to be displayed in the list.
     */
    public void setItems(List<ITEM> items)
    {
        this.items = items;
        this.notifyDataSetChanged();
    }
}
