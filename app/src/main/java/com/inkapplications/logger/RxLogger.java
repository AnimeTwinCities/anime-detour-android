package com.inkapplications.logger;

import inkapplicaitons.android.logger.Logger;
import io.reactivex.functions.Consumer;

public interface RxLogger extends Logger
{
    Consumer<Throwable> debugHandler(String operation);
    Consumer<Throwable> errorHandler(String operation);
    Consumer<Throwable> infoHandler(String operation);
    Consumer<Throwable> traceHandler(String operation);
    Consumer<Throwable> warnHandler(String operation);
}
