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
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;
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
        String roleArnOption = prefix + "role_arn";
        String roleSessionNameOption = prefix + "role_session_name";
        String webIdentityTokenFileOption = prefix + "web_identity_token_file";

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
                reject(task.getRoleArn(), roleArnOption);
                reject(task.getRoleSessionName(), roleSessionNameOption);
                reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
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
                reject(task.getRoleArn(), roleArnOption);
                reject(task.getRoleSessionName(), roleSessionNameOption);
                reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
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
            reject(task.getRoleArn(), roleArnOption);
            reject(task.getRoleSessionName(), roleSessionNameOption);
            reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
            return overwriteBasicCredentials(task, new EnvironmentVariableCredentialsProvider().getCredentials());

        case "instance":
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getRoleArn(), roleArnOption);
            reject(task.getRoleSessionName(), roleSessionNameOption);
            reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
            return createInstanceProfileCredentialsProvider();

        case "profile":
        {
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getRoleArn(), roleArnOption);
            reject(task.getRoleSessionName(), roleSessionNameOption);
            reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);

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
            reject(task.getRoleArn(), roleArnOption);
            reject(task.getRoleSessionName(), roleSessionNameOption);
            reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
            return overwriteBasicCredentials(task, new SystemPropertiesCredentialsProvider().getCredentials());

        case "anonymous":
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getRoleArn(), roleArnOption);
            reject(task.getRoleSessionName(), roleSessionNameOption);
            reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
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
            reject(task.getRoleArn(), roleArnOption);
            reject(task.getRoleSessionName(), roleSessionNameOption);
            reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
            final AWSSessionCredentials creds = new BasicSessionCredentials(accessKeyId, secretAccessKey, sessionToken);
            return new AWSSessionCredentialsProvider() {
                public AWSSessionCredentials getCredentials() {
                    return creds;
                }

                public void refresh() {
                }
            };
        }

        case "web_identity_token":
        {
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            WebIdentityTokenCredentialsProvider.Builder builder = WebIdentityTokenCredentialsProvider.builder();
            if (task.getRoleArn().isPresent()) {
                builder = builder.roleArn(task.getRoleArn().get());
            }
            if (task.getRoleSessionName().isPresent()) {
                builder = builder.roleArn(task.getRoleSessionName().get());
            }
            if (task.getWebIdentityTokenFile().isPresent()) {
                builder = builder.roleArn(task.getWebIdentityTokenFile().get());
            }
            return builder.build();
        }

        case "default":
        {
            reject(task.getAccessKeyId(), accessKeyIdOption);
            reject(task.getSecretAccessKey(), secretAccessKeyOption);
            reject(task.getSessionToken(), sessionTokenOption);
            reject(task.getProfileFile(), profileFileOption);
            reject(task.getProfileName(), profileNameOption);
            reject(task.getRoleArn(), roleArnOption);
            reject(task.getRoleSessionName(), roleSessionNameOption);
            reject(task.getWebIdentityTokenFile(), webIdentityTokenFileOption);
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
}
