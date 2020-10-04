import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(plugin = "java")

plugins {
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.4.10"
	kotlin("plugin.spring") version "1.4.10"
	kotlin("plugin.jpa") version "1.4.10"
}

val ktlint by configurations.creating

group = "com.github.adrian83"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_14

repositories {
	mavenCentral()
	jcenter()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
	implementation("com.h2database:h2:1.4.199")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")

    ktlint("com.pinterest:ktlint:0.39.0")
    // additional 3rd party ruleset(s) can be specified here
    // just add them to the classpath (ktlint 'groupId:artifactId:version') and 
    // ktlint will pick them up
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "14"
	}
}

tasks.register<JavaExec>("ktlint") { 
	group = "verification"
    description = "Check Kotlin code style."
    main = "com.pinterest.ktlint.Main"
    classpath = ktlint
    args = listOf("src/**/*.kt")
    // to generate report in checkstyle format prepend following args:
    // "--reporter=plain", "--reporter=checkstyle,output=${buildDir}/ktlint.xml"
    // see https://github.com/pinterest/ktlint#usage for more
}

tasks.named("check") {
    dependsOn(ktlint)
}

tasks.register<JavaExec>("ktlintFormat") {
    description = "Fix Kotlin code style deviations."
	group = "formatting"
    main = "com.pinterest.ktlint.Main"
    classpath = ktlint
    args = listOf("-F", "src/**/*.kt")
}