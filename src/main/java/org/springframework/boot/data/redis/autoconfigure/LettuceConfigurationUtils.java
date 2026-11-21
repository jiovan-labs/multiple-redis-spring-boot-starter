/*
 * Copyright (c) 2020 Jiovan Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.data.redis.autoconfigure;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import com.jiovan.plexus.spring.toolkit.support.SingletonObjectProvider;
import io.lettuce.core.resource.ClientResources;


/**
 * Utilities for {@link org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory}
 *
 * @author David Hsing
 *
 * @see org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class LettuceConfigurationUtils {
    /**
     * Returns a {@link org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory} configured from the given Redis properties and connection configuration
     *
     * @param properties the Redis properties used to configure the connection factory
     * @param details the optional Redis connection details, resolved from {@code properties} when absent
     * @param standalone the optional standalone server configuration
     * @param sentinel the optional sentinel configuration
     * @param cluster the optional cluster configuration
     * @param bundles the optional SSL bundles used to configure TLS
     * @param configCustomizer the optional customizer for the Lettuce client configuration builder
     * @param optionCustomizer the optional customizer for the Lettuce client options builder
     * @param resources the Lettuce client resources shared by the connection factory
     *
     * @return the configured Lettuce connection factory
     */
    public static LettuceConnectionFactory redisConnectionFactory(@Nonnull DataRedisProperties properties, @Nullable DataRedisConnectionDetails details, @Nullable RedisStandaloneConfiguration standalone, @Nullable RedisSentinelConfiguration sentinel, @Nullable RedisClusterConfiguration cluster, @Nullable SslBundles bundles, @Nullable LettuceClientConfigurationBuilderCustomizer configCustomizer, @Nullable LettuceClientOptionsBuilderCustomizer optionCustomizer, @Nonnull ClientResources resources) {
        DataRedisConnectionDetails alias = ObjectUtils.getIfNull(details, RedisConfigurationUtils.redisConnectionDetails(properties, bundles));
        LettuceConnectionConfiguration configuration = new LettuceConnectionConfiguration(properties, SingletonObjectProvider.ofNullable(standalone), SingletonObjectProvider.ofNullable(sentinel), SingletonObjectProvider.ofNullable(cluster), SingletonObjectProvider.empty(), alias);
        return configuration.redisConnectionFactory(SingletonObjectProvider.ofNullable(configCustomizer), SingletonObjectProvider.ofNullable(optionCustomizer), resources);
    }
}
