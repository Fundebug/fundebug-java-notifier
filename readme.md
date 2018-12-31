# Fundebug：Java应用实时BUG监控插件


### 模块介绍

- fundebug-java：监控普通Java应用BUG
- fundebug-spring：监控Spring应用BUG
- examples/hello-world：普通Java应用，使用fundebug-java监控
- examples/spring-rest-api：使用Spring实现REST API，使用fundebug-spring监控

### 测试spring-rest-api

```bash
curl -X POST http://localhost:8080/greeting
```