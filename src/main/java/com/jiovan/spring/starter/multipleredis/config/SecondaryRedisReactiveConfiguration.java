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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisReactiveAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisRepositoriesAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.RedisTemplateConfigurationUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;
import reactor.core.publisher.Flux;


/**
 * Secondary configuration for reactive redis
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBooleanProperty(prefix = "jiovan.multiple-redis", name = "enabled", matchIfMissing = true)
@ConditionalOnClass(value = {RedisOperations.class, Flux.class})
@ConditionalOnBean(name = SecondaryRedisAutoConfiguration.CONNECTION_FACTORY, value = ReactiveRedisConnectionFactory.class)
@AutoConfigureAfter(value = {PrimaryRedisReactiveConfiguration.class, SecondaryRedisAutoConfiguration.class})
@AutoConfigureBefore(value = {DataRedisAutoConfiguration.class, DataRedisReactiveAutoConfiguration.class, DataRedisRepositoriesAutoConfiguration.class})
public class SecondaryRedisReactiveConfiguration {
    public static final String REACTIVE_REDIS_TEMPLATE = "secondaryReactiveRedisTemplate";    // $NON-NLS-1$
    public static final String REACTIVE_STRING_REDIS_TEMPLATE = "secondaryReactiveStringRedisTemplate";    // $NON-NLS-1$

    /**
     * Returns a {@link org.springframework.data.redis.core.ReactiveRedisTemplate} for the given reactive Redis connection factory
     *
     * @param factory the reactive Redis connection factory used by the template
     * @param loader the resource loader used to resolve serializer resources
     *
     * @return the configured reactive Redis template
     */
    @Bean(name = REACTIVE_REDIS_TEMPLATE)
    @ConditionalOnMissingBean(name = REACTIVE_REDIS_TEMPLATE)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ReactiveRedisTemplate<Object, Object> redisTemplate(@Qualifier(value = SecondaryRedisAutoConfiguration.CONNECTION_FACTORY) @Nonnull ReactiveRedisConnectionFactory factory, @Nonnull ResourceLoader loader) {
        return RedisTemplateConfigurationUtils.reactiveRedisTemplate(factory, loader);
    }

    /**
     * Returns a {@link org.springframework.data.redis.core.ReactiveStringRedisTemplate} for the given reactive Redis connection factory
     *
     * @param factory the reactive Redis connection factory used by the template
     *
     * @return the configured reactive string Redis template
     */
    @Bean(name = REACTIVE_STRING_REDIS_TEMPLATE)
    @ConditionalOnMissingBean(name = REACTIVE_STRING_REDIS_TEMPLATE)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ReactiveStringRedisTemplate stringRedisTemplate(@Qualifier(value = SecondaryRedisAutoConfiguration.CONNECTION_FACTORY) @Nonnull ReactiveRedisConnectionFactory factory) {
        return RedisTemplateConfigurationUtils.reactiveStringRedisTemplate(factory);
    }
}
