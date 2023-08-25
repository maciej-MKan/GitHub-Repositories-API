plugins {
	java
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.mkan"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2022.0.4"
extra["wiremockVersion"] = "2.27.2"
extra["restAssuredVersion"] = "5.3.0"
extra["wiremockSlf4jVersion"] = "2.0.5"
extra["mapstructVersion"]= "1.5.3.Final"
extra["lombokMapstructBindingVersion"]= "0.2.0"

dependencies {
	//spring
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	//annotationProcesor
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation ("org.mapstruct:mapstruct:${property("mapstructVersion")}")
	annotationProcessor ("org.mapstruct:mapstruct-processor:${property("mapstructVersion")}")
	annotationProcessor ("org.projectlombok:lombok-mapstruct-binding:${property("lombokMapstructBindingVersion")}")

	//test
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("org.slf4j:slf4j-api:${property("wiremockSlf4jVersion")}")
	testImplementation("com.github.tomakehurst:wiremock-standalone:${property("wiremockVersion")}")
	testImplementation("io.rest-assured:rest-assured:${property("restAssuredVersion")}")
}



tasks.withType<Test> {
	useJUnitPlatform()
}
