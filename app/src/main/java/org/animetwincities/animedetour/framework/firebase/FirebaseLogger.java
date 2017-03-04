package org.animetwincities.animedetour.framework.firebase;

import com.google.firebase.crash.FirebaseCrash;
import inkapplicaitons.android.logger.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Sends Log messages to Firebase Crash/Error Reporting.
 *
 * This reports errors of level Info and higher to Firebase.
 * A report will not be sent until an ERROR is logged. Until then, it just logs
 * messages that will be attached to any subsequent report.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Singleton
public class FirebaseLogger implements Logger
{
    /** Writer used for capturing stack traces. */
    final private StringWriter stringWriter = new StringWriter();
    /** Writer used for capturing stack traces. */
    final private PrintWriter printWriter = new PrintWriter(stringWriter);

    @Inject
    public FirebaseLogger() {}

    @Override
    public void error(String message)
    {
        FirebaseCrash.report(new FirebaseLoggedError(message));
    }

    @Override
    public void error(String message, Object... objects)
    {
        FirebaseCrash.report(new FirebaseLoggedError(String.format(message, objects)));
    }

    @Override
    public void error(Throwable throwable, String message)
    {
        FirebaseCrash.log(message);
        FirebaseCrash.report(throwable);
    }

    @Override
    public void info(String message)
    {
        FirebaseCrash.log(message);
    }

    @Override
    public void info(String message, Object... objects)
    {
        FirebaseCrash.log(String.format(message, objects));
    }

    @Override
    public void info(Throwable throwable, String message)
    {
        FirebaseCrash.log(message);
        this.logThrowable(throwable);
    }

    @Override public void warn(String message)
    {
        FirebaseCrash.log(message);
    }

    @Override public void warn(String message, Object... objects)
    {
        FirebaseCrash.log(String.format(message, objects));
    }

    @Override public void warn(Throwable throwable, String message)
    {
        FirebaseCrash.log(message);
        this.logThrowable(throwable);
    }

    /**
     * Converts a throwable into a stack trace and logs it to Firebase.
     */
    private void logThrowable(Throwable throwable)
    {
        throwable.printStackTrace(this.printWriter);
        FirebaseCrash.log(this.stringWriter.toString());

        this.printWriter.flush();
        this.stringWriter.getBuffer().setLength(0);
    }

    @Override public void debug(String message) {}
    @Override public void debug(String message, Object... objects) {}
    @Override public void debug(Throwable throwable, String message) {}
    @Override public void trace(String message) {}
    @Override public void trace(String message, Object... objects) {}
    @Override public void trace(Throwable throwable, String message) {}
}
