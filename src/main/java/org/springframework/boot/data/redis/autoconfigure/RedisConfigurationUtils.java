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
import org.springframework.boot.ssl.SslBundles;


/**
 * Utilities for {@link DataRedisConnectionConfiguration}
 *
 * @author David Hsing
 *
 * @see DataRedisConnectionConfiguration
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class RedisConfigurationUtils {
    /**
     * Returns {@link org.springframework.boot.data.redis.autoconfigure.DataRedisConnectionDetails} resolved from the given Redis properties and SSL bundles
     *
     * @param properties the Redis properties used to resolve connection details
     * @param bundles the optional SSL bundles used to configure TLS
     *
     * @return the connection details resolved from the given properties and SSL bundles
     */
    @Nonnull
    public static DataRedisConnectionDetails redisConnectionDetails(@Nonnull DataRedisProperties properties, @Nullable SslBundles bundles) {
        return new PropertiesDataRedisConnectionDetails(properties, bundles);
    }
}
