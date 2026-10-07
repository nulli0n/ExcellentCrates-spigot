plugins {
    id("java-library")
    id("maven-publish")
}

allprojects {
    group = "su.nightexpress.excellentcrates"
    version = "7.0.0"

    repositories {
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.nightexpressdev.com/releases")
    }
}

val publishedModules = setOf("crates-api", "crates-core")

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
        withSourcesJar()
        // withJavadocJar()
    }

    // Set the project encoding.
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    dependencies {
        compileOnly("su.nightexpress.nightcore:main:2.16.7")
        compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    }

    if (publishedModules.contains(project.name)) {
        apply(plugin = "maven-publish")

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    artifact(tasks.named("jar"))

                    artifact(tasks.named("sourcesJar")) {
                        classifier = "sources"
                    }

                    /* artifact(tasks.named("javadocJar")) {
                        classifier = "javadoc"
                    } */

                    artifactId = project.name
                }
            }
            repositories {
                maven {
                    name = "nightexpress"
                    url = uri("https://repo.nightexpressdev.com/releases")
                    credentials {
                        username = System.getenv("REPOSILITE_USER")
                        password = System.getenv("REPOSILITE_PASSWORD")
                    }
                }
            }
        }
    }
}