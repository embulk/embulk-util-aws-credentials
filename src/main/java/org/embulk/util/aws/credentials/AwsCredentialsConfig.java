package org.embulk.util.aws.credentials;

import java.util.Optional;

interface AwsCredentialsConfig {
    /**
     * Gets the authentication method configured.
     *
     * @return The authentication method configured.
     */
    String getAuthMethod();

    /**
     * Sets an authentication method to configure.
     *
     * @param method  One of authentication methods from {@code "basic"}, {@code "env"}, {@code "instance"},
     *     {@code "profile"}, {@code "properties"}, {@code "anonymous"}, {@code "session"}, {@code "web_identity_token"}, and {@code "default"}.
     */
    void setAuthMethod(String method);

    /**
     * Gets the AWS IAM access key ID configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "basic"} or {@code "session"}.
     *
     * @return The AWS IAM access key ID configured. (For example, {@code AKIAIOSFODNN7EXAMPLE})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html">Managing Access Keys for IAM Users</a>
     */
    Optional<String> getAccessKeyId();

    /**
     * Sets an AWS IAM access key ID to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "basic"} or {@code "session"}.
     *
     * @param value  An AWS IAM access key ID to configure. (For example, {@code AKIAIOSFODNN7EXAMPLE})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html">Managing Access Keys for IAM Users</a>
     */
    void setAccessKeyId(Optional<String> value);

    /**
     * Gets the AWS IAM secret access key configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "basic"} or {@code "session"}.
     *
     * @return The AWS IAM secret access key configured. (For example, {@code wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html">Managing Access Keys for IAM Users</a>
     */
    Optional<String> getSecretAccessKey();

    /**
     * Sets an AWS IAM secret access key to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "basic"} or {@code "session"}.
     *
     * @param value  The AWS IAM secret access key to configure. (For example, {@code wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html">Managing Access Keys for IAM Users</a>
     */
    void setSecretAccessKey(Optional<String> value);

    /**
     * Gets the AWS IAM session token configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "session"}.
     *
     * @return The AWS IAM session token configured.
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_use-resources.html">Using Temporary Credentials With AWS Resources</a>
     */
    Optional<String> getSessionToken();

    /**
     * Sets an AWS IAM session token to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "session"}.
     *
     * @param value  The AWS IAM session token to configure
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_use-resources.html">Using Temporary Credentials With AWS Resources</a>
     */
    void setSessionToken(Optional<String> value);

    /**
     * Gets the path to an AWS IAM profile file configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "profile"}.
     *
     * @return The path to an AWS IAM profile file configured
     * @see <a href="https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html">Named profiles</a>
     */
    Optional<String> getProfileFile();

    /**
     * Sets a path to an AWS IAM profile file to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "profile"}.
     *
     * @param value  The path to an AWS IAM profile file to configure
     * @see <a href="https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html">Named profiles</a>
     */
    void setProfileFile(Optional<String> value);

    /**
     * Gets the name in an AWS IAM profile file configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "profile"}.
     *
     * @return The name in an AWS IAM profile file configured
     * @see <a href="https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html">Named profiles</a>
     */
    Optional<String> getProfileName();

    /**
     * Sets a name in an AWS IAM profile file to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "profile"}.
     *
     * @param value  The name in an AWS IAM profile file to configure
     * @see <a href="https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-profiles.html">Named profiles</a>
     */
    void setProfileName(Optional<String> value);

    /**
     * Gets the AWS IAM Role Arn configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "web_identity_token"}.
     *
     * @return The AWS IAM Role Arn configured
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRoleWithWebIdentity.html">AssumeRoleWithWebIdentity</a>
     */
    Optional<String> getRoleArn();

    /**
     * Sets an AWS IAM Role Arn to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "web_identity_token"}.
     *
     * @param value  The AWS IAM Role Arn to configure
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRoleWithWebIdentity.html">AssumeRoleWithWebIdentity</a>
     */
    void setRoleArn(Optional<String> value);

    /**
     * Gets the AWS IAM Role session name configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "web_identity_token"}.
     *
     * @return The AWS IAM Role session name configured
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRoleWithWebIdentity.html">AssumeRoleWithWebIdentity</a>
     */
    Optional<String> getRoleSessionName();

    /**
     * Sets an AWS IAM Role session name to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "web_identity_token"}.
     *
     * @param value  The AWS IAM Role session name to configure
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRoleWithWebIdentity.html">AssumeRoleWithWebIdentity</a>
     */
    void setRoleSessionName(Optional<String> value);

    /**
     * Gets the name in a web identity token file configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "web_identity_token"}.
     *
     * @return The name in a web identity token file configured
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRoleWithWebIdentity.html">AssumeRoleWithWebIdentity</a>
     */
    Optional<String> getWebIdentityTokenFile();

    /**
     * Sets a name in a web identity token file to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "web_identity_token"}.
     *
     * @param value  The name in a web identity token file to configure
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRoleWithWebIdentity.html">AssumeRoleWithWebIdentity</a>
     */
    void setWebIdentityTokenFile(Optional<String> value);

}
