repositories {
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":crates-api"))
    implementation(project(":crates-core"))
    compileOnly("com.github.retrooper:packetevents-spigot:2.12.0")
}