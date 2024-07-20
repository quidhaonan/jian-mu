# jianMu
A spring boot project with all the ideas

## 1. 开始

### ① 服务器

1. 将 `source/docker_compose` 整个文件夹复制进服务器中（本文档放入服务器根目录中），并将项目打包成 jar 包，将其放入至 `source/docker_compose/java` 中，结构如下：

    ```shell
    [root@ser161310378998 java]# pwd
    /docker_compose/java
    [root@ser161310378998 java]# ls
    Dockerfile  jian_mu.jar
    ```

2. 进入 `docker_compose` 目录，进行后续步骤

    ```shell
    [root@ser161310378998 /]# cd docker_compose/
    [root@ser161310378998 docker_compose]# pwd
    /docker_compose
    ```

3. 授予 `.sh` 文件执行权限

    ```shell
    [root@ser161310378998 docker_compose]# sudo chmod 777 first-deployment.sh
    [root@ser161310378998 docker_compose]# sudo chmod 777 jar-version-update.sh
    ```

3. 如服务器无 docker 环境，则运行 `first-deployment.sh` 脚本，如有 docker 环境，则执行 `docker compose up -d` 命令启动

    ```shell
    # 无 docker 环境
    [root@ser161310378998 docker_compose]# ./first-deployment.sh
    # 有 docker 环境
    [root@ser161310378998 docker_compose]# docker compose up -d
    ```

5. 如项目有更改，需要上传最新代码，则将新 jar 包替换原先 jar 包，并在 `docker_compose` 目录中执行 `jar-version-update.sh` 脚本

    ```shell
    [root@ser161310378998 docker_compose]# pwd
    /docker_compose
    [root@ser161310378998 docker_compose]# ./jar-version-update.sh
    ```

### ② 本地

1. 在 `src/main/resources/application-local.yml` 配置文件中加上本地数据库 ip 地址与密码，默认数据库版本为 5.7 以上
    + 本地数据库版本 5.7 以上，只填 ip 地址与密码
    + 本地数据库版本 5.7 及以下，额外修改以下文件
        + 修改 `src/main/resources/application.yml` 文件中 `spring.database.dynamic.datasource.lmyxlf.url` 以及 `spring.database.dynamic.datasource.lmyxlf.driver-class-name` 的对应版本 url 及驱动（仅加上或放开注释即可）
        + 修改 `pom.xml` 文件中 `mysql.connector.java.version` 版本（仅加上或放开注释即可）

2. 在 `src/main/resources/application-local.yml` 配置文件中加上本地 redis 的 ip 地址与密码

## 2. 基本信息

1. 数据库默认密码：`lmyxlf`，默认连接数据库名 `lmyxlf`
2. redis 默认密码：`lmyxlf`
3. 假设 ip 地址为：`127.0.0.1`
    + 如服务器上运行，使用了 nginx，则地址默认加上 `/api/jianmu`
        + 项目默认前缀：`127.0.0.1/api/jianmu`
        + swagger 地址：`127.0.0.1/api/jianmu/doc.html` 或 `127.0.0.1/api/jianmu/swagger-ui.html`
    + 如普通本地运行，则无默认前缀
        + 项目默认前缀：`127.0.0.1:9999`
        + swagger 地址：`127.0.0.1:9999/doc.html` 或 `127.0.0.1:9999/swagger-ui.html`
