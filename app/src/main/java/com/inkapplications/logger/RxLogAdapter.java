package com.inkapplications.logger;

import inkapplicaitons.android.logger.Logger;
import io.reactivex.functions.Consumer;

public class RxLogAdapter implements RxLogger {
    final static private String format = "A problem occurred in operation: %s";

    final private Logger logger;

    public RxLogAdapter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Consumer<Throwable> debugHandler(String operation) {
        return throwable -> this.debug(throwable, String.format(format, operation));
    }

    @Override
    public Consumer<Throwable> errorHandler(String operation) {
        return throwable -> this.error(throwable, String.format(format, operation));
    }

    @Override
    public Consumer<Throwable> infoHandler(String operation) {
        return throwable -> this.debug(throwable, String.format(format, operation));
    }

    @Override
    public Consumer<Throwable> traceHandler(String operation) {
        return throwable -> this.trace(throwable, String.format(format, operation));
    }

    @Override
    public Consumer<Throwable> warnHandler(String operation) {
        return throwable -> this.warn(throwable, String.format(format, operation));
    }

    @Override
    public void debug(String s) {
        this.logger.debug(s);
    }

    @Override
    public void debug(String s, Object... objects) {
        this.logger.debug(s, objects);
    }

    @Override
    public void debug(Throwable throwable, String s) {
        this.logger.debug(throwable, s);
    }

    @Override
    public void error(String s) {
        this.logger.error(s);
    }

    @Override
    public void error(String s, Object... objects) {
        this.logger.error(s, objects);
    }

    @Override
    public void error(Throwable throwable, String s) {
        this.logger.error(throwable, s);
    }

    @Override
    public void info(String s) {
        this.logger.info(s);
    }

    @Override
    public void info(String s, Object... objects) {
        this.logger.info(s, objects);
    }

    @Override
    public void info(Throwable throwable, String s) {
        this.logger.info(throwable, s);
    }

    @Override
    public void trace(String s) {
        this.logger.trace(s);
    }

    @Override
    public void trace(String s, Object... objects) {
        this.logger.trace(s, objects);
    }

    @Override
    public void trace(Throwable throwable, String s) {
        this.logger.trace(throwable, s);
    }

    @Override
    public void warn(String s) {
        this.logger.warn(s);
    }

    @Override
    public void warn(String s, Object... objects) {
        this.logger.warn(s, objects);
    }

    @Override
    public void warn(Throwable throwable, String s) {
        this.logger.warn(throwable, s);
    }
}
