Anime Detour Android App
========================

The official mobile App for Anime Detour.

Building
--------

### Debug Builds:

For development, the project can be built with the following command:

    ./gradlew --daemon installDebug
    
#### Debugging API 16-20

Proguard is not run on debug builds by default to improve build times. Because
of the Dex limit on devices below API 21, this will not be able to be debugged
unless proguard is run. You can enable it by adding a `-PsupportLevel` flag on
your command:

    ./gradlew --daemon installDebug -PsupportLevel

### Release Builds

    ./gradlew clean assembleRelease \
        -PversionCode=64 \
        -PversionName="2.0.0" \
        -PkeystoreFile="./releaseKeys.jks" \
        -PkeystorePassword="my-password" \
        -PkeystoreAlias="prod-signing" \
        -PkeyPassword="my-key-password"
    
#### Build arguments

For production builds, you will want to specify the following parameters using
gradle's `-P` arguments:

 - `versionCode`: The build number that will be used as the Android versionCode 
   configuration (default: 3000)
 - `versionName`: The user-readable name that will be used for the Android
   versionName configuration (default: 3.0-SNAPSHOT)
 - `keystoreFile`: The location of the keystore to use when signing, relative to the
   project root. If no file is specified, signing will not be attempted.
 - `keystorePassword`: the password of the keystore to use for signing.
 - `keystoreAlias`: The alias inside the specified keystore to use for signing.
 - `keyPassword`: The additional password for the specific alias inside the keystore.
