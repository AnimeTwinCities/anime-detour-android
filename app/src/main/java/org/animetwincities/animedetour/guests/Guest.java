package org.animetwincities.animedetour.guests;

/**
 * Created by kenton on 3/11/17.
 */

public class Guest {

    private String bio;
    private String image;
    private String name;

    public static Guest from(FirebaseGuest firebaseGuest) {
        Guest newGuest = new Guest();

        newGuest.bio = firebaseGuest.bio;
        newGuest.image = firebaseGuest.image;
        newGuest.name = firebaseGuest.name;

        return newGuest;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
