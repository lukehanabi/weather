FROM frolvlad/alpine-oraclejdk8:slim
ADD build/libs/weather*.jar app.jar
CMD [ "/app.jar" ]
