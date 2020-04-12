import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {

    // Spring
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"

    // Kotlin
    val kotlinVersion = "1.3.61"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    groovy

    // Avro
    id("com.commercehub.gradle.plugin.avro") version "0.9.1"
}

// Change these to make sense for your project. rootProject.name can be set in settings.gradle.kts
group = "com.lamarjs"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

springBoot {
    mainClassName = "com.lamarjs.bankingplayground.orchestrator.BankingPlaygroundApplication"
}

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    jcenter()
}

extra["springCloudVersion"] = "Hoxton.SR3"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.kafka:spring-kafka-test")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // Arrow Functional Library
    implementation("io.arrow-kt:arrow-core:0.10.4")

    // Liquibase
    implementation("org.liquibase:liquibase-core")

    // Database
    implementation("org.mariadb.jdbc:mariadb-java-client:2.5.2")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")



    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Guava
    implementation("com.google.guava:guava:28.2-jre")
    
    // Graph library
    implementation("org.jgrapht:jgrapht-core:1.4.0")

    // mandatory dependencies for using Spock
    implementation("org.codehaus.groovy:groovy-all:2.5.7")
    implementation("org.spockframework:spock-core:1.3-groovy-2.5")
    implementation("org.spockframework:spock-spring:1.3-groovy-2.5")

    // optional dependencies for using Spock
    testImplementation("org.hamcrest:hamcrest-core:1.3")    // only necessary if Hamcrest matchers are used
    testRuntimeOnly("net.bytebuddy:byte-buddy:1.9.3")       // allows mocking of classes (in addition to interfaces)
    testRuntimeOnly("org.objenesis:objenesis:2.6")          // allows mocking of classes without default constructor (together with CGLIB)

    // logging
    implementation("io.github.microutils:kotlin-logging:1.7.8")
}

// Force the groovy resolution across all transitive dependencies to the version required by spock.
configurations.all {
    resolutionStrategy {
        force("org.codehaus.groovy:groovy-all:2.5.7")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<GroovyCompile> {
    dependsOn("compileKotlin")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

