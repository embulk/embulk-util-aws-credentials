package org.embulk.util.aws.credentials;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSSessionCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.embulk.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class to generate {@link com.amazonaws.auth.AWSCredentialsProvider} from Embulk's task-defining interface.
 */
public abstract class AwsCredentials {
    private AwsCredentials() {
        // No instantiation.
    }

    /**
     * Creates {@link com.amazonaws.auth.AWSCredentialsProvider} from entries prefixed with {@code "aws_"} in task definition.
     *
     * @param task  An entry in Embulk's task defining interface
     * @return {@link com.amazonaws.auth.AWSCredentialsProvider} created
     */
    public static AWSCredentialsProvider getAWSCredentialsProvider(AwsCredentialsTaskWithPrefix task) {
        return getAWSCredentialsProvider("aws_", task);
    }

    /**
     * Creates {@link com.amazonaws.auth.AWSCredentialsProvider} from entries in task definition.
     *
     * @param task  An entry in Embulk's task defining interface
     * @return {@link com.amazonaws.auth.AWSCredentialsProvider} created
     */
    public static AWSCredentialsProvider getAWSCredentialsProvider(AwsCredentialsTask task) {
        return getAWSCredentialsProvider("", task);
    }

    private static AWSCredentialsProvider getAWSCredentialsProvider(String prefix, AwsCredentialsConfig task) {
        String authMethodOption = prefix + "auth_method";
        String sessionTokenOption = prefix + "session_token";
        String profileFileOption = prefix + "profile_file";
        String profileNameOption = prefix + "profile_name";
        String accessKeyIdOption = prefix + "access_key_id";
        String secretAccessKeyOption = prefix + "secret_access_key";
        String accountIdOption = prefix + "account_id";
        String roleNameOption = prefix + "role_name";
        String externalIdOption = prefix + "external_id";

        switch (task.getAuthMethod()) {
        case "basic":
            // for backward compatibility
            if (!task.getAccessKeyId().isPresent() && !task.getAccessKeyId().isPresent()) {
                log.warn("Both '{}' and '{}' are not set. Assuming that '{}: anonymous' option is set.",
                        accessKeyIdOption, secretAccessKeyOption, authMethodOption);
                log.warn("If you intentionally use anonymous authentication, please set 'auth_method: anonymous' option.");
                log.warn("This behavior will be removed in a future release.");
                reject(task.getSessionToken(), sessionTokenOption);
                reject(task.getProfileFile(), profileFileOption);
                reject(task.getProfileName(), profileNameOption);
                reject(task.getAccountId(), accountIdOption);
                reject(task.getRoleName(), roleNameOption);
                reject(task.getExternalId(), externalIdOption);
                return new AWSCredentialsProvider() {
                    public AWSCredentials getCredentials() {
                        return new AnonymousAWSCredentials();
                    }

                    public void refresh() {
                    }
                };
            } else {
                reject(task.getSessionToken(), sessionTokenOption);
                reject(task.getProfileFile(), profileFileOption);
                reject(task.getProfileName(), profileNameOption);
                reject(task.getExternalId(), externalIdOption);
                reject(task.getAccountId(), accountIdOption);
                reject(task.getRoleName(), roleNameOption);
                final String accessKeyId = require(task.getAccessKeyId(), "'access_key_id', 'secret_access_key'");
                final String secretAccessKey = require(task.getSecretAccessKey(), "'secret_access_key'");
                final BasicAWSCredentials creds = new BasicAWSCredentials(accessKeyId, secretAccessKey);
                return new AWSCredentialsProvider() {
                    public AWSCredentials getCredentials() {
                        return creds;
                    }

                    public void refresh() {
                    }
                };
            }

        case "env":
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getAccountId(), accountIdOption);
            reject(task.getRoleName(), roleNameOption);
            reject(task.getExternalId(), externalIdOption);
            return overwriteBasicCredentials(task, new EnvironmentVariableCredentialsProvider().getCredentials());

        case "instance":
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getAccountId(), accountIdOption);
            reject(task.getRoleName(), roleNameOption);
            reject(task.getExternalId(), externalIdOption);
            return createInstanceProfileCredentialsProvider();

        case "profile":
        {
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getAccountId(), accountIdOption);
            reject(task.getRoleName(), roleNameOption);
            reject(task.getExternalId(), externalIdOption);

            String profileName = task.getProfileName().orElse("default");
            ProfileCredentialsProvider provider;
            if (task.getProfileFile().isPresent()) {
                final Path profileFilePath = Paths.get(task.getProfileFile().get());
                final ProfilesConfigFile file = new ProfilesConfigFile(profileFilePath.toFile());
                provider = new ProfileCredentialsProvider(file, profileName);
            } else {
                provider = new ProfileCredentialsProvider(profileName);
            }

            return overwriteBasicCredentials(task, provider.getCredentials());
        }

        case "properties":
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getAccountId(), accountIdOption);
            reject(task.getRoleName(), roleNameOption);
            reject(task.getExternalId(), externalIdOption);
            return overwriteBasicCredentials(task, new SystemPropertiesCredentialsProvider().getCredentials());

        case "anonymous":
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getAccountId(), accountIdOption);
            reject(task.getRoleName(), roleNameOption);
            reject(task.getExternalId(), externalIdOption);
            return new AWSCredentialsProvider() {
                public AWSCredentials getCredentials() {
                    return new AnonymousAWSCredentials();
                }

                public void refresh() {
                }
            };

        case "session":
        {
            final String accessKeyId = require(task.getAccessKeyId(),
                    "'" + accessKeyIdOption + "', '" + secretAccessKeyOption + "', '" + sessionTokenOption + "'");
            final String secretAccessKey = require(task.getSecretAccessKey(),
                    "'" + secretAccessKeyOption + "', '" + sessionTokenOption + "'");
            final String sessionToken = require(task.getSessionToken(),
                    "'" + sessionTokenOption + "'");
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getAccountId(), accountIdOption);
            reject(task.getRoleName(), roleNameOption);
            reject(task.getExternalId(), externalIdOption);
            final AWSSessionCredentials creds = new BasicSessionCredentials(accessKeyId, secretAccessKey, sessionToken);
            return new AWSSessionCredentialsProvider() {
                public AWSSessionCredentials getCredentials() {
                    return creds;
                }

                public void refresh() {
                }
            };
        }

        case "assume_role":
        {
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            final String accountId = require(task.getAccountId(),
                    "'" + accountIdOption + "'");
            final String roleName = require(task.getRoleName(),
                    "'" + roleNameOption + "'");
            final String externalId = require(task.getExternalId(),
                    "'" + externalIdOption + "'");
            final String arn = String.format(ARN_PATTERN, task.getArnPartition(), accountId, roleName);

            // use AWSSecurityTokenServiceClient with DefaultAWSCredentialsProviderChain
            // https://javadoc.io/doc/com.amazonaws/aws-java-sdk-sts/1.11.0/com/amazonaws/services/securitytoken/AWSSecurityTokenServiceClient.html#AWSSecurityTokenServiceClient()
            STSAssumeRoleSessionCredentialsProvider.Builder builder
                    = new STSAssumeRoleSessionCredentialsProvider.Builder(arn, task.getSessionName());
            return builder.withExternalId(externalId)
                    .withRoleSessionDurationSeconds(task.getDurationInSeconds())
                    .build();
        }

        case "default":
        {
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getAccountId(), accountIdOption);
            reject(task.getRoleName(), roleNameOption);
            reject(task.getExternalId(), externalIdOption);
            return new DefaultAWSCredentialsProviderChain();
        }

        default:
            throw new ConfigException(String.format("Unknown auth_method '%s'. Supported methods are basic, instance, profile, properties, anonymous, session and default.",
                        task.getAuthMethod()));
        }
    }

    private static AWSCredentialsProvider overwriteBasicCredentials(AwsCredentialsConfig task, final AWSCredentials creds) {
        return new AWSCredentialsProvider() {
            public AWSCredentials getCredentials() {
                return creds;
            }

            public void refresh() {
            }
        };
    }

    private static <T> T require(Optional<T> value, String message) {
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new ConfigException("Required option is not set: " + message);
        }
    }

    private static <T> void reject(Optional<T> value, String message) {
        if (value.isPresent()) {
            throw new ConfigException("Invalid option is set: " + message);
        }
    }

    @SuppressWarnings("deprecation")
    private static InstanceProfileCredentialsProvider createInstanceProfileCredentialsProvider() {
        return new InstanceProfileCredentialsProvider();
    }

    private static final Logger log = LoggerFactory.getLogger(AwsCredentials.class);
    private static final String ARN_PATTERN = "arn:%s:iam::%s:role/%s";
}
