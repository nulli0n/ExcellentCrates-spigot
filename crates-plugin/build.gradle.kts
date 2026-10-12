import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "9.2.2"
}

repositories {

}

dependencies {
    implementation(project(":crates-api"))
    implementation(project(":crates-core"))
    implementation(project(":integration-itemsadder"))
    implementation(project(":integration-nexo"))
    implementation(project(":integration-packetevents"))
    implementation(project(":integration-papi"))

    testImplementation("io.papermc.paper:paper-api:26.1.2.build.+")
    testImplementation("su.nightexpress.nightcore:main:2.16.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testImplementation("org.mockito:mockito-core:5.20.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    archiveClassifier.set("thin") 
}

// Configure the shadowJar task to bundle the necessary dependencies.
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("ExcellentCrates")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("")
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    processResources {
        // Replicates maven <filtering>true</filtering> for plugin.yml
        // Replaces ${version} with the project version.
        val currentVersion = project.version.toString()

        filesMatching("*plugin.yml") {
            expand(mapOf("version" to currentVersion))
        }
    }
}

// Equivalent of the maven-source-plugin.
//java {
//    withSourcesJar()
//    withJavadocJar()
//}

// Ensure that building the project runs the shadowJar task.
tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}
