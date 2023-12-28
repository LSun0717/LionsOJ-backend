# Lions OJ

Lions OJ是一个Java生态开发的在线判题（Online Judge）系统，本项目为其后端工程，持续开发中。

### 主流框架 & 特性

- Spring Boot 2.7.x
- Spring MVC
- MyBatis + MyBatis Plus 数据访问（开启分页）
- Spring Boot 调试工具和项目处理器
- Spring AOP 切面编程
- Spring Scheduler 定时任务
- Spring 事务注解

### 数据存储

- MySQL 数据库
- Redis 内存数据库
- Elasticsearch 搜索引擎
- 腾讯云 COS 对象存储

### 工具类

- Easy Excel 表格处理
- Hutool 工具库
- Gson 解析库
- Apache Commons Lang3 工具类
- Lombok 注解
- Swagger+Knife4j api接口文档

### 业务特性

- Spring Session Redis 分布式登录
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决
- 多环境配置


## 业务功能

- 提供示例 SQL（用户、题目、题目提交、题解、判题配置、判题信息...）
- 用户登录、注册、注销、更新、检索、权限管理
- 题目模块：创建、修改、题解创建、多语言支持...
- 题目提交：判题、题目校验、代码沙盒...
- updating
### 单元测试

- JUnit5 单元测试
- 示例单元测试类