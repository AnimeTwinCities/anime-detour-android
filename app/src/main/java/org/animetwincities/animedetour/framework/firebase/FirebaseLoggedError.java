package org.animetwincities.animedetour.framework.firebase;

/**
 * Throwable that represents a plain error string that has been logged by the application.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class FirebaseLoggedError extends Throwable
{
    public FirebaseLoggedError(String message) {
        super(message);
    }
}
