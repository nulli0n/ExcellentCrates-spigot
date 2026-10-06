repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation(project(":crates-api"))
    compileOnly("me.clip:placeholderapi:2.11.6")
}