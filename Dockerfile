FROM openjdk:21

WORKDIR /app

# jar 파일 앞에 기본적으로 build/libs/가 붙어야 함.
COPY build/libs/homeListener-0.0.1-SNAPSHOT.jar /app

CMD ["java", "-jar", "homeListener-0.0.1-SNAPSHOT.jar"]