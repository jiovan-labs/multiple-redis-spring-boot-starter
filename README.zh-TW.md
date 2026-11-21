[English](README.md) | [簡體中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# 多 Redis Spring Boot Starter

讓 Spring Boot 應用快速整合多個 `redis`。

## 快速開始

- 匯入相依性

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>multiple-redis-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 預設情況下，此 starter 會自動生效，你可以透過 `jiovan.multiple-redis.enabled = false` 關閉它。

- 使用字首 `jiovan.multiple-redis` 設定 Spring Boot 的 `application.yml`

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

> 此 starter 最多支援 3 個 `RedisConnectionFactory`。（三振出局）

- **可選功能**：如果要使用 Redis 倉儲，請將倉儲類放在以下包中（以 `primary` 為例）

    - 倉儲類：`**.repository.primary.redis`

- **可選功能**：如果要使用 Redis Session，請將原有的 `@EnableRedisHttpSession` 註解替換為 `@EnableMultipleRedisHttpSession` 註解。

> 別忘了指定 `redisConnection` 屬性。

- 透過 `@Autowired`/`@Resource` 註解並配合 `@Qualifier` 註解注入以下 Bean（以 `primary` 為例）

| Bean 型別 | 限定符 |
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

- 此 starter 支援全球主流 Redis 連線工廠，包括
  - lettuce
  - jedis

## 文件

- GitHub：https://github.com/jiovan-labs/multiple-redis-spring-boot-starter
- Redis 主頁：https://redis.io

## 環境要求

- jdk 17+

## 許可證

本專案採用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 許可證。

請參閱 `NOTICE.txt` 檔案瞭解所需的宣告與歸屬資訊。

## 捐贈

喜歡這個工具包嗎？歡迎[向我們捐贈](https://jiovan.com/donation)以支援開發。

## 版權

北京玖漫科技有限公司

## 網站

- 玖漫：https://jiovan.com
