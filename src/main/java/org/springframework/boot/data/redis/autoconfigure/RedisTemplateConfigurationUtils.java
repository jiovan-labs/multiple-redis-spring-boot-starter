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
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * Utilities for redis templates created by Spring Boot autoconfiguration
 *
 * @author David Hsing
 *
 * @see DataRedisAutoConfiguration
 * @see DataRedisReactiveAutoConfiguration
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class RedisTemplateConfigurationUtils {
    /**
     * Returns a classic object redis template created from the given connection factory
     *
     * @param factory the Redis connection factory used by the template
     *
     * @return the created classic object Redis template
     */
    @Nonnull
    public static RedisTemplate<Object, Object> redisTemplate(@Nonnull RedisConnectionFactory factory) {
        return new DataRedisAutoConfiguration().redisTemplate(factory);
    }

    /**
     * Returns a classic string redis template created from the given connection factory
     *
     * @param factory the Redis connection factory used by the template
     *
     * @return the created classic string Redis template
     */
    @Nonnull
    public static StringRedisTemplate stringRedisTemplate(@Nonnull RedisConnectionFactory factory) {
        return new DataRedisAutoConfiguration().stringRedisTemplate(factory);
    }

    /**
     * Returns a reactive object redis template created from the given connection factory and resource loader
     *
     * @param factory the reactive Redis connection factory used by the template
     * @param loader the resource loader used to resolve serializer resources
     *
     * @return the created reactive object Redis template
     */
    @Nonnull
    public static ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate(@Nonnull ReactiveRedisConnectionFactory factory, @Nonnull ResourceLoader loader) {
        return new DataRedisReactiveAutoConfiguration().reactiveRedisTemplate(factory, loader);
    }

    /**
     * Returns a reactive string redis template created from the given connection factory
     *
     * @param factory the reactive Redis connection factory used by the template
     *
     * @return the created reactive string Redis template
     */
    @Nonnull
    public static ReactiveStringRedisTemplate reactiveStringRedisTemplate(@Nonnull ReactiveRedisConnectionFactory factory) {
        return new DataRedisReactiveAutoConfiguration().reactiveStringRedisTemplate(factory);
    }
}
