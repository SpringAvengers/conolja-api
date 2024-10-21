# 빌드 이미지로 Gradle 8.1 & jDK 17 지정
FROM gradle:8.10-jdk17-alpine AS build

# 소스코드를 복사할 작업 디렉토리를 생성
WORKDIR /app

# 라이브러리 설치에 필요한 파일만 복사
COPY ./build.gradle ./settings.gradle ./

# 의존성 다운로드
RUN gradle dependencies --no-daemon

# 나머지 소스 코드 및 리소스 복사
COPY ./ ./

# Gradle 빌드 실행
RUN gradle clean build --no-daemon

# 런타임 이미지(공식 OpenJDK 배포판, 기존 OpenJDK 이미지는 deprecated 됨)
FROM eclipse-temurin:17-jre-alpine

# 애플리케이션을 실행할 작업 디렉토리를 생성
WORKDIR /app

# 빌드 이미지에서 생성된 JAR 파일을 런타임 이미지로 복사
COPY --from=build /app/build/libs/conolja.jar /app/conolja.jar

EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "conolja.jar"]
