embulk-util-aws-credentials
============================

How to release
---------------

See also: https://central.sonatype.org/pages/gradle.html

### Sonatype

1. Create your account on Sonatype OSSRH.
2. Set your account information in your `gradle.properties` (usually in `~/.gradle/gradle.properties`).
    * ```
      ossrhUsername=<your Sonatype OSSRH username>
      ossrhPassword=<your Sonatype OSSRH password>
      ```

### OpenPGP

1. Create your own OpenPGP key pair.
2. Set your OpenPGP key information in your `gradle.properties`.
    * ```
      # For example: signing.keyId=1234ABCD
      signing.keyId=<your OpenPGP key ID>
      signing.password=<your OpenPGP key password>
      # For example: signing.secretKeyRingFile=/home/you/.gnupg/1234567890ABCDEF1234567890ABCDEF12345678.secring.gpg
      signing.secretKeyRingFile=<path to your OpenPGP secret key ring file>
      ```

### Release

1. Create a detached `HEAD` from the `master` branch.
    * ```
      git checkout master
      git checkout --detach
      ```
2. Remove `-SNAPSHOT` from the version number in `build.gradle`.
3. Commit it and tag.
    * ```
      git add build.gradle
      git commit -m "Release vX.Y.Z"
      git tag vX.Y.Z
      ```
4. Release.
    * ```
      ./gradlew publishMavenPublicationToMavenCentralRepository
      ```
5. Push the tag.
    * ```
      git push -u origin vX.Y.Z
      ```
6. Back to `master`.
    * ```
      git checkout master
      ```
7. Continue to the next `-SNAPSHOT` version.
