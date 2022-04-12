package org.embulk.util.aws.credentials;

import java.util.Optional;
import org.embulk.util.config.Config;
import org.embulk.util.config.ConfigDefault;

/**
 * An {@code interface} for Embulk's task-defining interface, with entries prefixed with {@code "aws_"}.
 */
public interface AwsCredentialsTaskWithPrefix extends AwsCredentialsConfig {
    @Override
    @Config("aws_auth_method")
    @ConfigDefault("\"basic\"")
    String getAuthMethod();

    @Override
    @Config("aws_access_key_id")
    @ConfigDefault("null")
    Optional<String> getAccessKeyId();

    @Override
    @Config("aws_secret_access_key")
    @ConfigDefault("null")
    Optional<String> getSecretAccessKey();

    @Override
    @Config("aws_session_token")
    @ConfigDefault("null")
    Optional<String> getSessionToken();

    @Override
    @Config("aws_profile_file")
    @ConfigDefault("null")
    Optional<String> getProfileFile();

    @Override
    @Config("aws_profile_name")
    @ConfigDefault("null")
    Optional<String> getProfileName();

    @Override
    @Config("aws_account_id")
    @ConfigDefault("null")
    Optional<String> getAccountId();

    @Override
    @Config("aws_role_name")
    @ConfigDefault("null")
    Optional<String> getRoleName();

    @Override
    @Config("aws_external_id")
    @ConfigDefault("null")
    Optional<String> getExternalId();

    @Override
    @Config("aws_duration_in_seconds")
    @ConfigDefault("3600")
    int getDurationInSeconds();

    @Override
    @Config("aws_arn_partition")
    @ConfigDefault("\"aws\"")
    String getArnPartition();

    @Override
    @Config("aws_session_name")
    @ConfigDefault("\"embulk\"")
    String getSessionName();
}
