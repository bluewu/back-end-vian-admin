FROM java:openjdk-8-jre-alpine

# 设置apk源
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/' /etc/apk/repositories

ENV JHIPSTER_SLEEP 0

# Install base packages 时区
RUN apk update && apk add curl bash tree tzdata \
    && cp -r -f /usr/share/zoneinfo/Hongkong /etc/localtime \
    && echo -ne "Alpine Linux 3.4 image. (`uname -rsv`)\n" >> /root/.built

# add directly the war
ADD *.war /vian-admin-app.war

RUN sh -c 'touch /vian-admin-app.war'
VOLUME /tmp
EXPOSE 8807
CMD echo "The vian-admin-app will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar ${JAVA_OPTS} /vian-admin-app.war
