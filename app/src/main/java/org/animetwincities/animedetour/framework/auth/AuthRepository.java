package org.animetwincities.animedetour.framework.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import org.animetwincities.rxfirebase.FirebaseObservables;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRepository
{
    final private FirebaseAuth firebase;

    @Inject
    public AuthRepository(FirebaseAuth firebase)
    {
        this.firebase = firebase;
    }

    /**
     * Sign into the application as an anonymous user.
     *
     * @return An observable that must be subscribed to for authentication
     *         success and error events.
     */
    public Observable<User> signInAnonymously()
    {
        Task<AuthResult> authResultTask = this.firebase.signInAnonymously();

        return FirebaseObservables.fromTask(authResultTask)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(authResult -> new User(authResult.getUser().getUid()))
            .observeOn(AndroidSchedulers.mainThread());
    }
}
