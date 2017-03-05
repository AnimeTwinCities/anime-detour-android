package org.animetwincities.animedetour.framework.auth;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest
{
    @Test
    public void getters() throws Exception
    {
        User test = new User("test-id");

        assertEquals("test-id", test.getId());
    }
}
