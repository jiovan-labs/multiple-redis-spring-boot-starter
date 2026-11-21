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

package org.springframework.boot.cache.autoconfigure;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import com.jiovan.plexus.spring.toolkit.support.SingletonObjectProvider;


/**
 * Utilities for {@link org.springframework.data.redis.cache.RedisCacheManager}
 *
 * @author David Hsing
 *
 * @see org.springframework.data.redis.cache.RedisCacheManager
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class RedisCacheManagerUtils {
    /**
     * Returns a {@link org.springframework.data.redis.cache.RedisCacheManager} configured from the given cache properties, customizers and connection factory
     *
     * @param properties the cache properties used to configure default cache behaviour
     * @param customizers the cache manager customizers applied to the manager
     * @param configuration the optional default Redis cache configuration
     * @param customizer the optional customizer for the Redis cache manager builder
     * @param factory the Redis connection factory used by the cache manager
     * @param loader the resource loader used to resolve cache configuration resources
     *
     * @return the configured Redis cache manager
     */
    public static RedisCacheManager cacheManager(@Nonnull CacheProperties properties, @Nonnull CacheManagerCustomizers customizers, @Nullable RedisCacheConfiguration configuration,
            @Nullable RedisCacheManagerBuilderCustomizer customizer, @Nonnull RedisConnectionFactory factory, @Nonnull ResourceLoader loader) {
        org.springframework.boot.cache.autoconfigure.RedisCacheConfiguration cacheConfiguration = new org.springframework.boot.cache.autoconfigure.RedisCacheConfiguration();
        return cacheConfiguration.cacheManager(properties, customizers, SingletonObjectProvider.ofNullable(configuration), SingletonObjectProvider.ofNullable(customizer), factory, loader);
    }
}
