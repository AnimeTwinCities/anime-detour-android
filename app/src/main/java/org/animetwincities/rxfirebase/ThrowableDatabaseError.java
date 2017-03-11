package org.animetwincities.rxfirebase;

import com.google.firebase.database.DatabaseError;

/**
 * Wraps a firebase DatabaseError inside a throwable so that it can be passed
 * through Rx's onError methods.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class ThrowableDatabaseError extends Throwable
{
    final private DatabaseError error;

    public ThrowableDatabaseError(DatabaseError error)
    {
        this.error = error;
    }

    public DatabaseError getError()
    {
        return this.error;
    }
}
