repositories {
    maven("https://repo.nexomc.com/releases")
}

dependencies {
    implementation(project(":crates-api"))
    compileOnly("com.nexomc:nexo:1.27.0")
}