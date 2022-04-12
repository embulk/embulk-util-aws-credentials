package org.embulk.util.aws.credentials;

import java.util.Optional;
import org.embulk.util.config.Config;
import org.embulk.util.config.ConfigDefault;

/**
 * An {@code interface} for Embulk's task-defining interface.
 */
public interface AwsCredentialsTask extends AwsCredentialsConfig {
    @Override
    @Config("auth_method")
    @ConfigDefault("\"basic\"")
    String getAuthMethod();

    @Override
    @Config("access_key_id")
    @ConfigDefault("null")
    Optional<String> getAccessKeyId();

    @Override
    @Config("secret_access_key")
    @ConfigDefault("null")
    Optional<String> getSecretAccessKey();

    @Override
    @Config("session_token")
    @ConfigDefault("null")
    Optional<String> getSessionToken();

    @Override
    @Config("profile_file")
    @ConfigDefault("null")
    Optional<String> getProfileFile();

    @Override
    @Config("profile_name")
    @ConfigDefault("null")
    Optional<String> getProfileName();

    @Override
    @Config("aws_account_id")
    @ConfigDefault("null")
    Optional<String> getAwsAccountId();

    @Override
    @Config("role_name")
    @ConfigDefault("null")
    Optional<String> getRoleName();

    @Override
    @Config("external_id")
    @ConfigDefault("null")
    Optional<String> getExternalId();

    @Override
    @Config("duration_in_seconds")
    @ConfigDefault("3600")
    int getDurationInSeconds();

    @Override
    @Config("aws_partition")
    @ConfigDefault("\"aws\"")
    String getAwsPartition();

    @Override
    @Config("session_name")
    @ConfigDefault("\"treasure-data\"")
    String getSessionName();
}
