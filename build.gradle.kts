import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `maven-publish`
    id("org.jetbrains.dokka")
}

group = "dev.sublab"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
}

val commonVersion: String by project
val dokkaVersion: String by project

dependencies {
    testImplementation(kotlin("test"))
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:$dokkaVersion")
    implementation("dev.sublab:common-kotlin:$commonVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.dokkaHtml.configure {
    outputDirectory.set(projectDir.resolve("reference"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}