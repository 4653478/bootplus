FROM java
MAINTAINER age 4653478@qq.com
WORKDIR /ROOT
CMD ["java", "-version"]
ENTRYPOINT ["java", "-jar", "${project.build.finalName}.jar"]