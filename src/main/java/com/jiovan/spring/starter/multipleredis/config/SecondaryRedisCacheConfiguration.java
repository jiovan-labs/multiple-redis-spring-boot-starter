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

package com.jiovan.spring.starter.multipleredis.config;


import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration;
import org.springframework.boot.cache.autoconfigure.CacheProperties;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import com.jiovan.plexus.spring.condition.annotation.ConditionalOnAllBooleanProperties;
import com.jiovan.plexus.spring.toolkit.util.RedisConfigWraps;
import com.jiovan.spring.starter.cacheexpiry.resolver.impl.RedisExpiryCacheResolver;


/**
 * Secondary configuration for redis cache
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllBooleanProperties(value = {
    @ConditionalOnBooleanProperty(prefix = "jiovan.multiple-redis", name = "enabled", matchIfMissing = true),
    @ConditionalOnBooleanProperty(prefix = SecondaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "cache-enabled", matchIfMissing = true)
})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis", matchIfMissing = true)
@ConditionalOnBean(value = {CacheAspectSupport.class, CacheProperties.class})
@ConditionalOnClass(value = CacheManager.class)
@AutoConfigureAfter(value = {CacheAutoConfiguration.class, PrimaryRedisCacheConfiguration.class, SecondaryRedisAutoConfiguration.class})
@AutoConfigureBefore(name = "com.jiovan.spring.starter.cacheexpiry.config.RedisExpiryCacheConfiguration")
public class SecondaryRedisCacheConfiguration {
    public static final String CACHE_MANAGER = "secondaryRedisCacheManager";    // $NON-NLS-1$
    public static final String CACHE_RESOLVER = "secondaryRedisCacheResolver";    // $NON-NLS-1$

    /**
     * Returns the Redis-backed {@link org.springframework.cache.CacheManager} bean configured from the given cache and Jackson properties
     *
     * @param factory the Redis connection factory used by the cache manager
     * @param cacheProperties the cache properties used to configure cache behaviour
     * @param jacksonProperties the optional Jackson properties used to configure value serialization
     *
     * @return the configured Redis cache manager
     */
    @Bean(name = CACHE_MANAGER)
    @ConditionalOnBean(name = SecondaryRedisAutoConfiguration.CONNECTION_FACTORY)
    @ConditionalOnMissingBean(name = CACHE_MANAGER)
    public CacheManager cacheManager(@Qualifier(value = SecondaryRedisAutoConfiguration.CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull CacheProperties cacheProperties, @Nullable JacksonProperties jacksonProperties) {
        return RedisConfigWraps.cacheManager(factory, cacheProperties, jacksonProperties);
    }

    /**
     * Returns a {@link org.springframework.cache.interceptor.CacheResolver} that resolves expiry-aware caches from the given cache manager
     *
     * @param cacheManager the cache manager used to resolve caches
     *
     * @return the expiry-aware cache resolver
     */
    @Bean(name = CACHE_RESOLVER)
    @ConditionalOnClass(value = RedisExpiryCacheResolver.class)
    @ConditionalOnBean(name = CACHE_MANAGER)
    @ConditionalOnMissingBean(name = CACHE_RESOLVER)
    public CacheResolver cacheResolver(@Qualifier(value = CACHE_MANAGER) @Nonnull CacheManager cacheManager) {
        return new RedisExpiryCacheResolver(cacheManager);
    }
}
