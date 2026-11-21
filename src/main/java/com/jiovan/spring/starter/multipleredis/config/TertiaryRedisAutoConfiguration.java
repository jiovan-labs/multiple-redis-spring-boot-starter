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
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisConnectionDetails;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisReactiveAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisRepositoriesAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.data.redis.autoconfigure.JedisConfigurationUtils;
import org.springframework.boot.data.redis.autoconfigure.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.data.redis.autoconfigure.LettuceClientOptionsBuilderCustomizer;
import org.springframework.boot.data.redis.autoconfigure.LettuceConfigurationUtils;
import org.springframework.boot.data.redis.autoconfigure.RedisConfigurationUtils;
import org.springframework.boot.data.redis.autoconfigure.RedisTemplateConfigurationUtils;
import org.springframework.boot.data.redis.autoconfigure.SimpleRedisConnectionConfiguration;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import com.jiovan.plexus.spring.condition.annotation.ConditionalOnAnyProperties;
import com.jiovan.plexus.spring.toolkit.util.JacksonRedisWraps;
import com.jiovan.plexus.spring.toolkit.util.RedisConfigWraps;
import com.jiovan.spring.starter.multipleredis.facade.RedisTemplateCustomizer;
import com.jiovan.spring.starter.multipleredis.facade.StringRedisTemplateCustomizer;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import tools.jackson.databind.ObjectMapper;


/**
 * Tertiary configuration for classic redis
 *
 * @author David Hsing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBooleanProperty(prefix = "jiovan.multiple-redis", name = "enabled", matchIfMissing = true)
@ConditionalOnAnyProperties(value = {
    @ConditionalOnProperty(prefix = TertiaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "url"),
    @ConditionalOnProperty(prefix = TertiaryRedisAutoConfiguration.PROPERTIES_PREFIX, name = "host")
})
@ConditionalOnClass(value = RedisOperations.class)
@AutoConfigureAfter(value = SecondaryRedisAutoConfiguration.class)
@AutoConfigureBefore(value = {DataRedisAutoConfiguration.class, DataRedisReactiveAutoConfiguration.class, DataRedisRepositoriesAutoConfiguration.class})
@Import(value = {TertiaryRedisAutoConfiguration.Entry.class, TertiaryRedisAutoConfiguration.Lettuce.class, TertiaryRedisAutoConfiguration.Jedis.class, TertiaryRedisAutoConfiguration.Stage.class})
public class TertiaryRedisAutoConfiguration {
    public static final String PROPERTIES_PREFIX = "jiovan.multiple-redis.tertiary";    // $NON-NLS-1$
    public static final String REDIS_PROPERTIES = "tertiaryRedisProperties";    // $NON-NLS-1$
    public static final String CONNECTION_DETAILS = "tertiaryRedisConnectionDetails";    // $NON-NLS-1$
    public static final String STANDALONE_CONFIGURATION = "tertiaryRedisStandaloneConfiguration";    // $NON-NLS-1$
    public static final String SENTINEL_CONFIGURATION = "tertiaryRedisSentinelConfiguration";    // $NON-NLS-1$
    public static final String CLUSTER_CONFIGURATION = "tertiaryRedisClusterConfiguration";    // $NON-NLS-1$
    public static final String SSL_BUNDLES = "tertiaryRedisSslBundles";    // $NON-NLS-1$
    public static final String LETTUCE_CLIENT_CONFIGURATION_CUSTOMIZER = "tertiaryLettuceClientConfigurationCustomizer";    // $NON-NLS-1$
    public static final String LETTUCE_CLIENT_OPTION_CUSTOMIZER = "tertiaryLettuceClientOptionCustomizer";    // $NON-NLS-1$
    public static final String LETTUCE_CLIENT_RESOURCES = "tertiaryLettuceClientResources";    // $NON-NLS-1$
    public static final String JEDIS_CLIENT_CUSTOMIZER = "tertiaryJedisClientCustomizer";    // $NON-NLS-1$
    public static final String CONNECTION_FACTORY = "tertiaryRedisConnectionFactory";    // $NON-NLS-1$
    public static final String OBJECT_REDIS_TEMPLATE = "tertiaryObjectRedisTemplate";    // $NON-NLS-1$
    public static final String SESSION_REDIS_TEMPLATE = "tertiarySessionRedisTemplate";    // $NON-NLS-1$
    public static final String STRING_REDIS_TEMPLATE = "tertiaryStringRedisTemplate";    // $NON-NLS-1$


    /**
     * Tertiary configuration for classic redis
     *
     * @author David Hsing
     */
    @Order(value = 0)
    static class Entry {
        /**
         * Returns the Redis properties bean bound from the configured properties prefix
         *
         * @return the bound Redis properties bean
         */
        @Bean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = REDIS_PROPERTIES)
        @ConfigurationProperties(prefix = PROPERTIES_PREFIX)
        public DataRedisProperties redisProperties() {
            return new DataRedisProperties();
        }

        /**
         * Returns {@link org.springframework.boot.data.redis.autoconfigure.DataRedisConnectionDetails} resolved from the given Redis properties and SSL bundles
         *
         * @param properties the Redis properties used to resolve connection details
         * @param bundles the optional SSL bundles used to configure TLS
         *
         * @return the connection details resolved from the given properties and SSL bundles
         */
        @Bean(name = CONNECTION_DETAILS)
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = CONNECTION_DETAILS)
        public DataRedisConnectionDetails redisConnectionDetails(@Qualifier(value = REDIS_PROPERTIES) @Nonnull DataRedisProperties properties, @Autowired(required = false) @Qualifier(value = SSL_BUNDLES) @Nullable SslBundles bundles) {
            return RedisConfigurationUtils.redisConnectionDetails(properties, bundles);
        }

        /**
         * Returns the standalone server configuration bean derived from the given Redis properties
         *
         * @param properties the Redis properties used to build the standalone configuration
         *
         * @return the standalone server configuration
         */
        @Bean(name = STANDALONE_CONFIGURATION)
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "standalone", matchIfMissing = true)
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = STANDALONE_CONFIGURATION)
        public RedisStandaloneConfiguration redisStandaloneConfiguration(@Qualifier(value = REDIS_PROPERTIES) @Nonnull DataRedisProperties properties) {
            return new SimpleRedisConnectionConfiguration(properties).getRedisStandaloneConfiguration();
        }

        /**
         * Returns the sentinel configuration bean derived from the given Redis properties
         *
         * @param properties the Redis properties used to build the sentinel configuration
         *
         * @return the sentinel configuration
         */
        @Bean(name = SENTINEL_CONFIGURATION)
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "sentinel")
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = SENTINEL_CONFIGURATION)
        public RedisSentinelConfiguration redisSentinelConfiguration(@Qualifier(value = REDIS_PROPERTIES) @Nonnull DataRedisProperties properties) {
            return new SimpleRedisConnectionConfiguration(properties).getRedisSentinelConfiguration();
        }

        /**
         * Returns the cluster configuration bean derived from the given Redis properties
         *
         * @param properties the Redis properties used to build the cluster configuration
         *
         * @return the cluster configuration
         */
        @Bean(name = CLUSTER_CONFIGURATION)
        @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "cluster")
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = CLUSTER_CONFIGURATION)
        public RedisClusterConfiguration redisClusterConfiguration(@Qualifier(value = REDIS_PROPERTIES) @Nonnull DataRedisProperties properties) {
            return new SimpleRedisConnectionConfiguration(properties).getRedisClusterConfiguration();
        }
    }


    /**
     * Tertiary configuration for classic redis of lettuce
     *
     * @author David Hsing
     */
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "client-type", havingValue = "lettuce", matchIfMissing = true)
    @ConditionalOnClass(value = io.lettuce.core.RedisClient.class)
    @Order(value = 1)
    static class Lettuce {
        /**
         * Returns the default Lettuce client resources bean shared by the connection factory
         *
         * @return the default Lettuce client resources
         */
        @Bean(name = LETTUCE_CLIENT_RESOURCES, destroyMethod = "shutdown")
        @ConditionalOnMissingBean(name = LETTUCE_CLIENT_RESOURCES, value = ClientResources.class)
        public DefaultClientResources lettuceClientResources() {
            return DefaultClientResources.create();
        }

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
        @Bean(name = CONNECTION_FACTORY)
        @ConditionalOnBean(name = {REDIS_PROPERTIES, LETTUCE_CLIENT_RESOURCES})
        @ConditionalOnMissingBean(name = CONNECTION_FACTORY, value = RedisConnectionFactory.class)
        public LettuceConnectionFactory redisConnectionFactory(@Qualifier(value = REDIS_PROPERTIES) @Nonnull DataRedisProperties properties,
            @Autowired(required = false) @Qualifier(value = CONNECTION_DETAILS) @Nullable DataRedisConnectionDetails details,
            @Autowired(required = false) @Qualifier(value = STANDALONE_CONFIGURATION) @Nullable RedisStandaloneConfiguration standalone,
            @Autowired(required = false) @Qualifier(value = SENTINEL_CONFIGURATION) @Nullable RedisSentinelConfiguration sentinel,
            @Autowired(required = false) @Qualifier(value = CLUSTER_CONFIGURATION) @Nullable RedisClusterConfiguration cluster,
            @Autowired(required = false) @Qualifier(value = SSL_BUNDLES) @Nullable SslBundles bundles,
            @Autowired(required = false) @Qualifier(value = LETTUCE_CLIENT_CONFIGURATION_CUSTOMIZER) @Nullable LettuceClientConfigurationBuilderCustomizer configCustomizer,
            @Autowired(required = false) @Qualifier(value = LETTUCE_CLIENT_OPTION_CUSTOMIZER) @Nullable LettuceClientOptionsBuilderCustomizer optionCustomizer,
            @Qualifier(value = LETTUCE_CLIENT_RESOURCES) @Nonnull ClientResources resources) {
            return LettuceConfigurationUtils.redisConnectionFactory(properties, details, standalone, sentinel, cluster, bundles, configCustomizer, optionCustomizer, resources);
        }
    }


    /**
     * Tertiary configuration for classic redis of jedis
     *
     * @author David Hsing
     */
    @ConditionalOnProperty(prefix = PROPERTIES_PREFIX, name = "client-type", havingValue = "jedis", matchIfMissing = true)
    @ConditionalOnClass(value = {org.apache.commons.pool2.ObjectPool.class, redis.clients.jedis.Jedis.class})
    @Order(value = 2)
    static class Jedis {
        /**
         * Returns a {@link org.springframework.data.redis.connection.jedis.JedisConnectionFactory} configured from the given Redis properties and connection configuration
         *
         * @param properties the Redis properties used to configure the connection factory
         * @param details the optional Redis connection details, resolved from {@code properties} when absent
         * @param standalone the optional standalone server configuration
         * @param sentinel the optional sentinel configuration
         * @param cluster the optional cluster configuration
         * @param bundles the optional SSL bundles used to configure TLS
         * @param customizer the optional customizer for the Jedis client configuration builder
         *
         * @return the configured Jedis connection factory
         */
        @Bean(name = CONNECTION_FACTORY)
        @ConditionalOnBean(name = REDIS_PROPERTIES)
        @ConditionalOnMissingBean(name = CONNECTION_FACTORY, value = RedisConnectionFactory.class)
        public JedisConnectionFactory redisConnectionFactory(@Qualifier(value = REDIS_PROPERTIES) @Nonnull DataRedisProperties properties,
            @Autowired(required = false) @Qualifier(value = CONNECTION_DETAILS) @Nullable DataRedisConnectionDetails details,
            @Autowired(required = false) @Qualifier(value = STANDALONE_CONFIGURATION) @Nullable RedisStandaloneConfiguration standalone,
            @Autowired(required = false) @Qualifier(value = SENTINEL_CONFIGURATION) @Nullable RedisSentinelConfiguration sentinel,
            @Autowired(required = false) @Qualifier(value = CLUSTER_CONFIGURATION) @Nullable RedisClusterConfiguration cluster,
            @Autowired(required = false) @Qualifier(value = SSL_BUNDLES) @Nullable SslBundles bundles,
            @Autowired(required = false) @Qualifier(value = JEDIS_CLIENT_CUSTOMIZER) @Nullable JedisClientConfigurationBuilderCustomizer customizer) {
            return JedisConfigurationUtils.redisConnectionFactory(properties, details, standalone, sentinel, cluster, bundles, customizer);
        }
    }


    /**
     * Tertiary configuration for classic redis of stage
     *
     * @author David Hsing
     */
    @Order(value = 3)
    static class Stage {
        /**
         * Returns a classic {@link org.springframework.data.redis.core.RedisTemplate} bean using JDK serializers
         *
         * @param factory the Redis connection factory used by the template
         * @param customizers the ordered object template customizers applied to the template
         *
         * @return the configured classic object Redis template
         */
        @Bean(name = OBJECT_REDIS_TEMPLATE)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnMissingBean(name = OBJECT_REDIS_TEMPLATE)
        @ConditionalOnMissingClass(value = "tools.jackson.databind.ObjectMapper")
        public RedisTemplate<Object, Object> classicObjectRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<RedisTemplateCustomizer> customizers) {
            RedisTemplate<Object, Object> template = RedisTemplateConfigurationUtils.redisTemplate(factory);
            customizers.orderedStream().forEach(customizer -> customizer.customize(template, OBJECT_REDIS_TEMPLATE));
            template.afterPropertiesSet();
            return template;
        }

        /**
         * Returns a {@link org.springframework.data.redis.core.RedisTemplate} bean using Jackson JSON serializers
         *
         * @param factory the Redis connection factory used by the template
         * @param customizers the ordered object template customizers applied to the template
         * @param properties the Jackson properties used to build the JSON value serializer
         *
         * @return the configured Jackson object Redis template
         */
        @Bean(name = OBJECT_REDIS_TEMPLATE)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnClass(value = ObjectMapper.class)
        @ConditionalOnMissingBean(name = OBJECT_REDIS_TEMPLATE)
        public RedisTemplate<Object, Object> jacksonObjectRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<RedisTemplateCustomizer> customizers, @Nonnull ObjectProvider<JacksonProperties> properties) {
            RedisTemplate<Object, Object> template = JacksonRedisWraps.objectObjectRedisTemplate(factory, properties.getIfAvailable());
            customizers.orderedStream().forEach(customizer -> customizer.customize(template, OBJECT_REDIS_TEMPLATE));
            template.afterPropertiesSet();
            return template;
        }

        /**
         * Returns a {@link org.springframework.data.redis.core.StringRedisTemplate} bean for the given connection factory
         *
         * @param factory the Redis connection factory used by the template
         * @param customizers the ordered string template customizers applied to the template
         *
         * @return the configured string Redis template
         */
        @Bean(name = STRING_REDIS_TEMPLATE)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnMissingBean(name = STRING_REDIS_TEMPLATE)
        public StringRedisTemplate stringRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<StringRedisTemplateCustomizer> customizers) {
            StringRedisTemplate template = RedisTemplateConfigurationUtils.stringRedisTemplate(factory);
            customizers.orderedStream().forEach(customizer -> customizer.customize(template, STRING_REDIS_TEMPLATE));
            template.afterPropertiesSet();
            return template;
        }

        /**
         * Returns a classic {@link org.springframework.data.redis.core.RedisTemplate} bean for session storage using JDK value serialization
         *
         * @param factory the Redis connection factory used by the template
         * @param customizers the ordered template customizers applied to the template
         *
         * @return the configured session Redis template
         */
        @Bean(name = SESSION_REDIS_TEMPLATE)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnMissingBean(name = SESSION_REDIS_TEMPLATE)
        @ConditionalOnMissingClass(value = "tools.jackson.databind.ObjectMapper")
        @SuppressWarnings("DataFlowIssue")
        public RedisTemplate<String, Object> classicSessionRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<RedisTemplateCustomizer> customizers) {
            RedisTemplate<String, Object> template = RedisConfigWraps.redisTemplate(factory, RedisSerializer.string(), RedisSerializer.java());
            customizers.orderedStream().forEach(customizer -> customizer.customize(template, SESSION_REDIS_TEMPLATE));
            return template;
        }

        /**
         * Returns a {@link org.springframework.data.redis.core.RedisTemplate} bean for session storage using Jackson JSON value serialization
         *
         * @param factory the Redis connection factory used by the template
         * @param customizers the ordered template customizers applied to the template
         * @param properties the Jackson properties used to build the JSON value serializer
         *
         * @return the configured session Redis template
         */
        @Bean(name = SESSION_REDIS_TEMPLATE)
        @ConditionalOnBean(name = CONNECTION_FACTORY)
        @ConditionalOnClass(value = ObjectMapper.class)
        @ConditionalOnMissingBean(name = SESSION_REDIS_TEMPLATE)
        @SuppressWarnings({"DataFlowIssue", "DuplicatedCode"})
        public RedisTemplate<String, Object> jasksonSessionRedisTemplate(@Qualifier(value = CONNECTION_FACTORY) @Nonnull RedisConnectionFactory factory, @Nonnull ObjectProvider<RedisTemplateCustomizer> customizers, @Nonnull ObjectProvider<JacksonProperties> properties) {
            RedisSerializer<Object> valueSerializer = JacksonRedisWraps.jsonObjectSerializer(properties.getIfAvailable());
            RedisTemplate<String, Object> template = RedisConfigWraps.redisTemplate(factory, RedisSerializer.string(), valueSerializer);
            customizers.orderedStream().forEach(customizer -> customizer.customize(template, SESSION_REDIS_TEMPLATE));
            return template;
        }
    }
}
