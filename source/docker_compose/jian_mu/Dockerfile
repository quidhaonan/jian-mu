# 基础镜像
FROM openjdk:17
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 拷贝 jar 包
COPY jian_mu.jar /jian_mu/jian_mu.jar
# 入口
ENTRYPOINT ["java", "-jar", "/jian_mu/jian_mu.jar"]