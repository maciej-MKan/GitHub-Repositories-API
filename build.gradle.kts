plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.mkan"
version = "0.0.1-SNAPSHOT"

extra["springCloudVersion"] = "2022.0.4"
extra["wiremockVersion"] = "2.27.2"
extra["restAssuredVersion"] = "5.3.0"
extra["wiremockSlf4jVersion"] = "2.0.5"
extra["jsr305Version"] = "3.0.2"
extra["jacocoVersion"] = "0.8.9"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

jacoco {
    toolVersion = "${property("jacocoVersion")}"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //spring
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    //annotationProcesor
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // warning: unknown enum constant When.MAYBE
    // reason: class file for javax.annotation.meta.When not found
    implementation("com.google.code.findbugs:jsr305:${property("jsr305Version")}")

    //test
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.slf4j:slf4j-api:${property("wiremockSlf4jVersion")}")
    testImplementation("com.github.tomakehurst:wiremock-standalone:${property("wiremockVersion")}")
    testImplementation("io.rest-assured:rest-assured:${property("restAssuredVersion")}")

}



tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("skipped", "failed", "passed")
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = 0.80.toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}