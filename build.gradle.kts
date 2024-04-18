plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok") version "8.4"
}

group = "me.cyberproton"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.2.4")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.data:spring-data-elasticsearch:4.2.0")
    implementation("org.springframework.boot:spring-boot-starter-parent:3.2.4")
    implementation(platform("software.amazon.awssdk:bom:2.20.56"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:s3-transfer-manager:2.25.16")
    implementation("software.amazon.awssdk.crt:aws-crt:0.29.13")
    implementation("org.apache.tika:tika-parsers:2.9.1")
    implementation("net.datafaker:datafaker:2.1.0")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

springBoot {
    mainClass = "me.cyberproton.ocean.OceanApplication"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
