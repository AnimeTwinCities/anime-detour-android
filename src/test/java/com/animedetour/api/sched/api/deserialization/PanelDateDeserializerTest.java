/*
 * This file is part of the Anime Detour Android application
 *
 * Copyright (c) 2015 Anime Twin Cities, Inc.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.animedetour.api.sched.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;

@RunWith(JUnit4.class)
public class PanelDateDeserializerTest extends TestCase
{
    @Test
    public void deserialization() throws IOException
    {
        PanelDateDeserializer deserializer = new PanelDateDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getText()).thenReturn("1991-09-04 13:06:02");

        ReadableInstant deserialized = deserializer.deserialize(parser, null);
        DateTime dateTime = new DateTime(deserialized);

        assertEquals(1991, dateTime.getYear());
        assertEquals(9, dateTime.getMonthOfYear());
        assertEquals(4, dateTime.getDayOfMonth());
        assertEquals(13, dateTime.getHourOfDay());
        assertEquals(6, dateTime.getMinuteOfHour());
        assertEquals(2, dateTime.getSecondOfMinute());
    }

    @Test(expected = JsonMappingException.class)
    public void deserializationFailure() throws IOException
    {
        PanelDateDeserializer deserializer = new PanelDateDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_FALSE);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Mockito.when(context.mappingException(Mockito.anyString())).thenReturn(new JsonMappingException("Mapping failure"));

        deserializer.deserialize(parser, context);
    }
}
