package org.animetwincities.animedetour.framework.dependencyinjection;

/**
 * Enforces self-injection on an Activity.
 *
 * This is implemented by base classes in order to enforce that the child class
 * runs an injection on itself for services, since that cannot be done
 * automatically by the parent.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public interface DaggerActivityComponentAware
{
    /**
     * Boilerplate required by Dagger2 for injecting the current object.
     *
     * This method is just boilerplate and should always be implemented exactly as:
     *
     *      public void injectSelf(ActivityComponent component) {
     *          component.inject(this);
     *      }
     *
     * Make sure the class is added as an explicit method in the
     * `ActivityComponent` class.
     *
     * @see ActivityComponent
     * @param component Activity-level Dependency Component for doing injections.
     */
    void injectSelf(ActivityComponent component);
}
