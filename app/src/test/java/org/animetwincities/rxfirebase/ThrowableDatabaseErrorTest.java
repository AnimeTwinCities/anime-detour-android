package org.animetwincities.rxfirebase;

import com.google.firebase.database.DatabaseError;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ThrowableDatabaseErrorTest
{
    @Test
    public void getError() throws Exception
    {
        DatabaseError fakeError = mock(DatabaseError.class);
        ThrowableDatabaseError error = new ThrowableDatabaseError(fakeError);

        assertSame(fakeError, error.getError());
    }
}
