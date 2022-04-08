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
     *     {@code "profile"}, {@code "properties"}, {@code "anonymous"}, {@code "session"},
     *     {@code "role"}, and {@code "default"}.
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
     * Gets the AWS Account ID configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @return The AWS Account ID configured
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/console_account-alias.html">Account ID</a>
     */
    Optional<String> getAccountId();

    /**
     * Sets an AWS Account ID to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @param value  An AWS Account ID to configure. (For example, {@code 0123456789})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/console_account-alias.html">Account ID</a>
     */
    void setAccountId(Optional<String> value);

    /**
     * Gets the AWS Role Name configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @return The AWS Role Name configured
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_terms-and-concepts.html">Role</a>
     */
    Optional<String> getRoleName();

    /**
     * Sets an AWS Role Name to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @param value  An AWS Role Name to configure. (For example, {@code exampleRole})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_terms-and-concepts.html">Role</a>
     */
    void setRoleName(Optional<String> value);

    /**
     * Gets the External ID configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @return The External ID configured
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-user_externalid.html">External ID</a>
     */
    Optional<String> getExternalId();

    /**
     * Sets an External ID to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @param value  An External ID to configure. (For example, {@code 9a59e793-355f-4729-abbc-ab612ee419e5})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-user_externalid.html">External ID</a>
     */
    void setExternalId(Optional<String> value);

    /**
     * Gets the Duration configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @return The External ID configured
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use.html#id_roles_use_view-role-max-session">Duration</a>
     */
    int getDuration();

    /**
     * Sets a Duration to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @param value  A Duration to configure. (For example, {@code 900})
     * @see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_use.html#id_roles_use_view-role-max-session">Duration</a>
     */
    void setDuration(int value);

    /**
     * Gets the AWS Partition configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @return The AWS Partition configured
     * @see <a href="https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html">Partition</a>
     */
    String getAwsPartition();

    /**
     * Sets an AWS Partition to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @param value  An AWS Partition to configure. (For example, {@code aws})
     * @see <a href="https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html">Partition</a>
     */
    void setAwsPartition(String value);

    /**
     * Gets the Roel session name configured.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @return The Roel session name configured
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRole.html">RoleSessionName</a>
     */
    String getSessionName();

    /**
     * Sets a Role session nam to configure.
     *
     * <p>It is available only when the authentication method is set to: {@code "role"}.
     *
     * @param value  A Role session name to configure. (For example, {@code treasure-data})
     * @see <a href="https://docs.aws.amazon.com/STS/latest/APIReference/API_AssumeRole.html">RoleSessionName</a>
     */
    void setSessionName(String value);
}
