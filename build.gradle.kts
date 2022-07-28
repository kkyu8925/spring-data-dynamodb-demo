import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.boostchicken:spring-data-dynamodb:5.2.5")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // localstack
    implementation("org.testcontainers:localstack:1.17.2")
    testImplementation("org.testcontainers:junit-jupiter:1.17.2")

    /**
     * default library
     * - jackson-module-kotlin adds support for serialization/deserialization of Kotlin classes and data classes
     * - kotlin-reflect is Kotlin reflection library
     * - kotlin-stdlib-jdk8 is the Java 8 variant of Kotlin standard library
     */
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
