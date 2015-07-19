/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.api.sched.model;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class EventTest extends TestCase
{
    /**
     * Check that a comma-separated list of tags is set properly.
     */
    @Test
    public void setTagsFromString()
    {
        Event event = new Event();
        event.setTags("Foo,Bar,Baz");

        List<String> actualTags = event.getTags();
        assertEquals("Foo", actualTags.get(0));
        assertEquals("Bar", actualTags.get(1));
        assertEquals("Baz", actualTags.get(2));
    }

    /**
     * Check that a list of tags is set properly.
     */
    @Test
    public void setTagsFromList()
    {
        Event event = new Event();
        event.setTagsList(Arrays.asList("Foo", "Bar", "Baz"));

        List<String> actualTags = event.getTags();
        assertEquals("Foo", actualTags.get(0));
        assertEquals("Bar", actualTags.get(1));
        assertEquals("Baz", actualTags.get(2));
    }

    /**
     * Make sure that the URL gets urlencoded properly if there are spaces in it.
     */
    @Test
    public void setUrlWithSpaces()
    {
        Event event = new Event();
        event.setMediaUrl("http://localhost/this is a test");

        assertEquals("http://localhost/this%20is%20a%20test", event.getMediaUrl());
    }
}
