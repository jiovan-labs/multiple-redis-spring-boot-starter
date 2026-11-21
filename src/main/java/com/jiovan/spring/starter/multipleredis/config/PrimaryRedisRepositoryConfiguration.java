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


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisReactiveAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;
import com.jiovan.plexus.spring.condition.annotation.ConditionalOnAllBooleanProperties;
import com.jiovan.plexus.spring.condition.annotation.ConditionalOnAnnotation;


/**
 * Primary configuration for redis repository
 *
 * @author David Hsing
 *
 * @see org.springframework.boot.data.redis.autoconfigure.DataRedisRepositoriesAutoConfiguration
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAllBooleanProperties(value = {
    @ConditionalOnBooleanProperty(prefix = "jiovan.multiple-redis", name = "enabled", matchIfMissing = true),
    @ConditionalOnBooleanProperty(prefix = PrimaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "repository.enabled")
})
@ConditionalOnClass(value = RedisOperations.class)
@ConditionalOnBean(name = PrimaryRedisAutoConfiguration.CONNECTION_FACTORY)
@ConditionalOnAnnotation(includeFilter = Repository.class, basePackage = PrimaryRedisRepositoryConfiguration.REPOSITORY_PACKAGE)
@AutoConfigureAfter(value = PrimaryRedisAutoConfiguration.class)
@AutoConfigureBefore(value = {DataRedisAutoConfiguration.class, DataRedisReactiveAutoConfiguration.class, DataRedisRepositoriesAutoConfiguration.class})
@EnableRedisRepositories(basePackages = PrimaryRedisRepositoryConfiguration.REPOSITORY_PACKAGE, redisTemplateRef = PrimaryRedisAutoConfiguration.OBJECT_REDIS_TEMPLATE)
public class PrimaryRedisRepositoryConfiguration {
    public static final String REPOSITORY_PACKAGE = "**.repository.primary.redis";    // $NON-NLS-1$
}
