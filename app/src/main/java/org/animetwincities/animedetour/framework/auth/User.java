package org.animetwincities.animedetour.framework.auth;

/**
 * Local model for users of the application.
 *
 * @author Renee Vandervelde <Renee@ReneeVandervelde.com>
 */
public class User
{
    final private String id;

    /**
     * @param id A Unique Identifier for the User.
     */
    public User(String id)
    {
        this.id = id;
    }

    /**
     * @return A Unique Identifier for the User.
     */
    public String getId()
    {
        return this.id;
    }

    @Override
    public String toString()
    {
        return "User{id='" + id + "'}";
    }
}
