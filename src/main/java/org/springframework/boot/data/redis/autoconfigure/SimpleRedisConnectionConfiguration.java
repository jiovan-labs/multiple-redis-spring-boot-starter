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
import com.jiovan.plexus.spring.toolkit.support.SingletonObjectProvider;


/**
 * Utilities for {@link org.springframework.data.redis.connection.RedisStandaloneConfiguration} and {@link org.springframework.data.redis.connection.RedisSentinelConfiguration}
 *
 * @author David Hsing
 *
 * @see org.springframework.data.redis.connection.RedisStandaloneConfiguration
 * @see org.springframework.data.redis.connection.RedisSentinelConfiguration
 */
@SuppressWarnings("unused")
public class SimpleRedisConnectionConfiguration extends DataRedisConnectionConfiguration {
    public SimpleRedisConnectionConfiguration(@Nonnull DataRedisProperties properties) {
        this(properties, null, null, null, null, null);
    }

    public SimpleRedisConnectionConfiguration(@Nonnull DataRedisProperties properties, @Nullable DataRedisConnectionDetails details) {
        this(properties, details, null, null, null, null);
    }

    public SimpleRedisConnectionConfiguration(@Nonnull DataRedisProperties properties, @Nullable DataRedisConnectionDetails details, @Nullable RedisStandaloneConfiguration standalone, @Nullable RedisSentinelConfiguration sentinel, @Nullable RedisClusterConfiguration cluster, @Nullable SslBundles bundles) {
        super(properties, ObjectUtils.getIfNull(details, RedisConfigurationUtils.redisConnectionDetails(properties, bundles)), SingletonObjectProvider.ofNullable(standalone), SingletonObjectProvider.ofNullable(sentinel), SingletonObjectProvider.ofNullable(cluster), SingletonObjectProvider.empty());
    }

    /**
     * Returns the Redis properties backing this connection configuration
     *
     * @return the Redis properties backing this connection configuration
     */
    @Nonnull
    public DataRedisProperties getRedisProperties() {
        return super.getProperties();
    }

    /**
     * Returns the standalone server configuration derived from the Redis properties
     *
     * @return the standalone server configuration
     */
    @Nonnull
    public RedisStandaloneConfiguration getRedisStandaloneConfiguration() {
        return super.getStandaloneConfig();
    }

    /**
     * Returns the sentinel configuration derived from the Redis properties
     *
     * @return the sentinel configuration, or {@code null} if sentinel mode is not configured
     */
    @Nullable
    public RedisSentinelConfiguration getRedisSentinelConfiguration() {
        return super.getSentinelConfig();
    }

    /**
     * Returns the cluster configuration derived from the Redis properties
     *
     * @return the cluster configuration, or {@code null} if cluster mode is not configured
     */
    @Nullable
    public RedisClusterConfiguration getRedisClusterConfiguration() {
        return super.getClusterConfiguration();
    }
}
