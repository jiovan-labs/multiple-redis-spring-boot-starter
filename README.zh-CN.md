[English](README.md) | [简体中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# 多 Redis Spring Boot Starter

让 Spring Boot 应用快速集成多个 `redis`。

## 快速开始

- 导入依赖

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>multiple-redis-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 默认情况下，此 starter 会自动生效，你可以通过 `jiovan.multiple-redis.enabled = false` 关闭它。

- 使用前缀 `jiovan.multiple-redis` 配置 Spring Boot 的 `application.yml`

```yml
jiovan:
    multiple-redis:
        primary:
            host: '192.168.0.1'
            timeout: 10s
        secondary:
            host: '192.168.0.2'
            timeout: 10s
        tertiary:
            host: '192.168.0.3'
            timeout: 10s
```

> 此 starter 最多支持 3 个 `RedisConnectionFactory`。（三振出局）

- **可选功能**：如果要使用 Redis 仓储，请将仓储类放在以下包中（以 `primary` 为例）

    - 仓储类：`**.repository.primary.redis`

- **可选功能**：如果要使用 Redis Session，请将原有的 `@EnableRedisHttpSession` 注解替换为 `@EnableMultipleRedisHttpSession` 注解。

> 别忘了指定 `redisConnection` 属性。

- 通过 `@Autowired`/`@Resource` 注解并配合 `@Qualifier` 注解注入以下 Bean（以 `primary` 为例）

| Bean 类型 | 限定符 |
|---------------------------------------|------------------------------------------------------------------|
| RedisConnectionFactory | PrimaryRedisAutoConfiguration.CONNECTION_FACTORY |
| RedisTemplate<Object, Object> | PrimaryRedisAutoConfiguration.OBJECT_REDIS_TEMPLATE |
| RedisTemplate<String, Object> | PrimaryRedisAutoConfiguration.JSON_REDIS_TEMPLATE |
| StringRedisTemplate | PrimaryRedisAutoConfiguration.STRING_REDIS_TEMPLATE |
| ReactiveRedisTemplate<Object, Object> | PrimaryRedisReactiveConfiguration.REACTIVE_REDIS_TEMPLATE |
| ReactiveStringRedisTemplate | PrimaryRedisReactiveConfiguration.REACTIVE_STRING_REDIS_TEMPLATE |
| CacheManager | PrimaryRedisCacheConfiguration.CACHE_MANAGER |
| CacheResolver | PrimaryRedisCacheConfiguration.CACHE_RESOLVER |
| RedisIndexedSessionRepository | PrimaryRedisSessionConfiguration.SESSION_REPOSITORY |

- 此 starter 支持全球主流 Redis 连接工厂，包括
  - lettuce
  - jedis

## 文档

- GitHub：https://github.com/jiovan-labs/multiple-redis-spring-boot-starter
- Redis 主页：https://redis.io

## 环境要求

- jdk 17+

## 许可证

本项目采用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可证。

请参阅 `NOTICE.txt` 文件了解所需的声明与归属信息。

## 捐赠

喜欢这个工具包吗？欢迎[向我们捐赠](https://jiovan.com/donation)以支持开发。

## 版权

北京玖漫科技有限公司

## 网站

- 玖漫：https://jiovan.com
