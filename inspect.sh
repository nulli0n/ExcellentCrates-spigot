#!/usr/bin/env bash
set -uo pipefail

cd "$(dirname "$0")" || exit 1

java_sources="src/main/java"
errors=0
warnings=0

error() {
    echo "error: $*"
    errors=$((errors + 1))
}

warning() {
    echo "warning: $*"
    warnings=$((warnings + 1))
}

plugin_descriptor=""
for candidate in src/main/resources/plugin.yml src/main/resources/paper-plugin.yml src/main/templates/plugin.yml src/main/templates/paper-plugin.yml; do
    if [[ -f "$candidate" ]]; then
        plugin_descriptor="$candidate"
        break
    fi
done
descriptor_name="$(basename "${plugin_descriptor:-plugin.yml}")"

if [[ -f pom.xml ]]; then
    build_tool="maven"
elif [[ -f build.gradle || -f build.gradle.kts || -f settings.gradle || -f settings.gradle.kts ]]; then
    build_tool="gradle"
else
    echo "error: no pom.xml or Gradle build script found"
    exit 1
fi

if [[ -x ./gradlew ]]; then
    gradle_command="./gradlew"
else
    gradle_command="gradle"
fi

build_log="$(mktemp)"
trap 'rm -f "$build_log"' EXIT

echo "==> Building with $build_tool"
if [[ "$build_tool" == "maven" ]]; then
    if ! mvn -B clean package -Dmaven.compiler.showWarnings=true -Dmaven.compiler.maxwarns=10000 >"$build_log" 2>&1; then
        error "maven build failed: $(grep -m1 -E '\[ERROR\]' "$build_log" || echo 'see maven output')"
    fi
    while IFS= read -r line; do
        warning "javac: $line"
    done < <(grep -E '^\[WARNING\] /.*\.java:\[[0-9]+,[0-9]+\]' "$build_log" | sed 's/^\[WARNING\] //')

    version="$(mvn -B -q help:evaluate -Dexpression=project.version -DforceStdout 2>/dev/null)"
    jar_dir="target"
else
    if ! "$gradle_command" clean build --console=plain >"$build_log" 2>&1; then
        error "gradle build failed: $(grep -m1 -E 'error:|FAILURE|What went wrong' "$build_log" || echo 'see gradle output')"
    fi
    while IFS= read -r line; do
        warning "javac: $line"
    done < <(grep -E '\.java:[0-9]+: warning:' "$build_log")

    version="$("$gradle_command" -q properties --console=plain 2>/dev/null | sed -n 's/^version: //p' | head -n1)"
    if [[ -z "$version" || "$version" == "unspecified" ]]; then
        version="$(sed -n 's/^version *= *//p' gradle.properties 2>/dev/null | head -n1)"
    fi
    jar_dir="build/libs"
fi

echo "==> Checking jar"
jar=""
if [[ -z "$version" || "$version" == "unspecified" ]]; then
    error "could not read project version from the $build_tool build"
else
    jar="$(find "$jar_dir" -maxdepth 1 -type f -name "*${version}*.jar" ! -name '*-sources.jar' ! -name '*-javadoc.jar' ! -name 'original-*.jar' -printf '%s %p\n' 2>/dev/null | sort -rn | head -n1 | cut -d' ' -f2-)"
fi
if [[ -n "$version" && -z "$jar" ]]; then
    error "no plugin jar for version $version was built in $jar_dir"
elif [[ -n "$jar" ]]; then
    descriptor="$(unzip -p "$jar" "$descriptor_name" 2>/dev/null)"
    jar_version="$(sed -n "s/^version: *['\"]\{0,1\}\([^'\"]*\)['\"]\{0,1\} *$/\1/p" <<<"$descriptor")"
    if [[ -z "$descriptor" ]]; then
        error "$jar has no $descriptor_name"
    elif [[ "$jar_version" != "$version" ]]; then
        error "$descriptor_name version '$jar_version' does not match $build_tool version '$version'"
    fi
    if [[ -n "$descriptor" ]] && ! grep -qx 'folia-supported: true' <<<"$descriptor"; then
        error "$descriptor_name in $jar is missing 'folia-supported: true'"
    fi
fi

echo "==> Checking descriptor"
if [[ -z "$plugin_descriptor" ]]; then
    error "no plugin.yml or paper-plugin.yml found in src/main/resources or src/main/templates"
elif ! grep -qx 'folia-supported: true' "$plugin_descriptor"; then
    error "$plugin_descriptor is missing 'folia-supported: true'"
fi

echo "==> Checking Folia scheduler usage"
while IFS= read -r line; do
    error "legacy Bukkit scheduler: $line"
done < <(grep -rnE 'Bukkit\.getScheduler\(|getServer\(\)\.getScheduler\(|BukkitRunnable|BukkitTask|BukkitScheduler' "$java_sources")
while IFS= read -r line; do
    error "nightcore Consumer<BukkitTask> overload (Bukkit scheduler): $line"
done < <(grep -rnE 'runTask(Async|Later|LaterAsync|Timer|TimerAsync)?\(\s*[a-zA-Z_]+\s*->' "$java_sources")
while IFS= read -r line; do
    error "global-thread menu tick (use the viewer's entity scheduler): $line"
done < <(grep -rnE '\brunNextTick\(' "$java_sources")

echo "==> $errors error(s), $warnings warning(s)"
[[ $errors -eq 0 && $warnings -eq 0 ]]
