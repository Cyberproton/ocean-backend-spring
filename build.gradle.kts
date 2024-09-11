plugins {
    java
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.freefair.lombok") version "8.4"
    id("com.diffplug.spotless") version "7.0.0.BETA1"
//    id("org.jooq.jooq-codegen-gradle") version "3.19.11"
}

group = "me.cyberproton"
version = "1.0"
val springBootVersion = "3.3.3"

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
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.data:spring-data-elasticsearch:4.2.0")
    implementation("org.springframework.boot:spring-boot-starter-parent:3.3.3")
    implementation("com.cosium.spring.data:spring-data-jpa-entity-graph:3.2.2")
    implementation(platform("software.amazon.awssdk:bom:2.20.56"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:s3-transfer-manager:2.25.16")
    implementation("software.amazon.awssdk.crt:aws-crt:0.29.13")
    implementation("org.apache.tika:tika-parsers:2.9.1")
    implementation("net.datafaker:datafaker:2.1.0")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    implementation("com.blazebit:blaze-persistence-core-api-jakarta:1.6.12")
    runtimeOnly("com.blazebit:blaze-persistence-core-impl-jakarta:1.6.12")
    runtimeOnly("com.blazebit:blaze-persistence-integration-hibernate-6.2:1.6.12")
    implementation("com.blazebit:blaze-persistence-entity-view-api-jakarta:1.6.12")
    runtimeOnly("com.blazebit:blaze-persistence-entity-view-impl-jakarta:1.6.12")
    implementation("com.blazebit:blaze-persistence-entity-view-processor-jakarta:1.6.12")
    implementation("com.blazebit:blaze-persistence-integration-spring-data-3.3:1.6.12")

//    implementation("org.jooq:jooq:3.19.10")
//    implementation("org.jooq:jooq-jackson-extensions:3.19.10")

    runtimeOnly("org.postgresql:postgresql")
//    jooqCodegen("org.postgresql:postgresql")

    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.6.0.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

springBoot {
    mainClass = "me.cyberproton.ocean.OceanApplication"
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated-sources/jooq"))
        }
    }
}

//jooq {
//    configuration {
//        jdbc {
//            url = System.getenv("DB_URL")
//            user = System.getenv("DB_USER")
//            password = System.getenv("DB_PASSWORD")
//        }
//
//        generator {
//            database {
//                name = "org.jooq.meta.postgres.PostgresDatabase"
//                inputSchema = "public"
//            }
//            target {
//                packageName = "me.cyberproton.ocean.jooq"
//            }
//            generate {
//                isPojos = true
//
//                isNullableAnnotation = true
//                nullableAnnotationType = "jakarta.annotation.Nullable"
//                isNonnullAnnotation = true
//                nonnullAnnotationType = "jakarta.annotation.Nonnull"
//            }
//        }
//    }
//}

spotless {
    java {
        importOrder()
        googleJavaFormat().aosp()
        indentWithSpaces(4)
        formatAnnotations()
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
