Anime Detour Android App
========================

The official anime detour application.

Requirements
------------

This project requires Java 1.7 and the Android SDK including:
 * Android 22 SDK
 * Build Tools 22.0.1
 * Android Support Repository
 * Android Support Library
 * Google Repository
 * Google Play Services

Installing????????
----------

The release version of the application requires you to set a keystore password
in the `gradle.properties` file. A stub of this file can be found in
`gradle.properties.dist` You should copy the dist file to `gradle.properties`
and change the password inside the copied file.
If you are not creating a release, you can leave the temporary password in place.

### Development / Debug ###

The debug / development version of the application can be installed by just 
running the command:

    ./gradlew installDebug

### Release ###

You can create the release apk by running:

    ./gradlew clean assembleRelease
    
After running, the APK file can be found in: 
`./build/outputs/apk/base-rewrite-release.apk`
