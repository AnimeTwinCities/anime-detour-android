package org.animetwincities.animedetour.schedule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

/**
 * Displays Fragments for each day in a list to display and entire schedule.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class MultiDayPagerAdapter extends FragmentStatePagerAdapter
{
    private List<LocalDate> days = Collections.emptyList();

    public MultiDayPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return new DayFragment(days.get(position));
    }

    @Override
    public int getCount()
    {
        return days.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return days.get(position).format(DateTimeFormatter.ofPattern("EEEE"));
    }

    public void setDays(List<LocalDate> days)
    {
        this.days = days;
        this.notifyDataSetChanged();
    }
}
