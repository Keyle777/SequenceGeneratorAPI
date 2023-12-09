# 项目名称

Java项目 - 流水号生成器

## 介绍

这是一个用Java语言开发的流水号生成器项目，旨在提供灵活、高效的流水号生成解决方案。该项目支持三种不同的流水号生成方式，分别基于Redis缓存、MySQL数据库和雪花算法。此外，项目还通过有效的高并发处理机制，确保在面对大量请求时仍能稳定可靠地生成唯一流水号。

## 特性

- **流水号生成方式**
  1. Redis缓存： 利用Redis缓存实现的流水号生成方式，适用于需要快速获取流水号的场景。
  2. MySQL数据库： 基于MySQL数据库的流水号生成方式，确保了流水号的持久性，适用于对数据持久性有较高要求的场景。
  3. 雪花算法： 使用雪花算法生成分布式环境下的唯一ID，适用于需要在分布式系统中生成唯一ID的场景。
  
- **高并发处理**
  - 通过采用有效的高并发处理机制，确保在多个请求同时访问时，仍能正确生成唯一的流水号，避免重复和冲突。

## 技术栈

- Java
- Spring Boot
- Redis
- MySQL

## 依赖

- Java 8+
- Maven
- Redis
- MySQL5

## 安装

1. **克隆项目**

    ```bash
    git clone https://github.com/Keyle777/SequenceGeneratorAPI.git
    cd SequenceGeneratorAPI
    ```

2. **配置数据库**

    在 `application.yml` 文件中配置数据库连接信息。

3. **配置Redis**

    如果使用了Redis，提供相应的Redis配置。

4. **运行项目**

    使用Maven或其他构建工具运行项目。

    ```bash
    mvn spring-boot:run
    ```
