package org.animetwincities.animedetour.framework.dependencyinjection;

import javax.inject.Qualifier;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Dependency that will be defined in debug builds and stubbed in release builds.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
@Qualifier
@Retention(RUNTIME)
public @interface AvailableInDebug {}
