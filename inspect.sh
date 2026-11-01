#!/usr/bin/env bash
set -uo pipefail

cd "$(dirname "$0")" || exit 1

java_sources="src/main/java"
plugin_descriptor="src/main/resources/plugin.yml"
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

build_log="$(mktemp)"
trap 'rm -f "$build_log"' EXIT

echo "==> Building"
if ! mvn -B clean package -Dmaven.compiler.showWarnings=true -Dmaven.compiler.maxwarns=10000 >"$build_log" 2>&1; then
    error "maven build failed: $(grep -m1 -E '\[ERROR\]' "$build_log" || echo 'see maven output')"
fi
while IFS= read -r line; do
    warning "javac: $line"
done < <(grep -E '^\[WARNING\] /.*\.java:\[[0-9]+,[0-9]+\]' "$build_log" | sed 's/^\[WARNING\] //')

echo "==> Checking jar"
version="$(mvn -B -q help:evaluate -Dexpression=project.version -DforceStdout 2>/dev/null)"
artifact="$(mvn -B -q help:evaluate -Dexpression=project.artifactId -DforceStdout 2>/dev/null)"
jar="target/${artifact}-${version}.jar"
if [[ -z "$version" || -z "$artifact" ]]; then
    error "could not read project version or artifactId from pom.xml"
elif [[ ! -f "$jar" ]]; then
    error "$jar was not built"
else
    descriptor="$(unzip -p "$jar" plugin.yml 2>/dev/null)"
    jar_version="$(sed -n "s/^version: '\(.*\)'$/\1/p" <<<"$descriptor")"
    if [[ "$jar_version" != "$version" ]]; then
        error "plugin.yml version '$jar_version' does not match pom.xml version '$version'"
    fi
    if ! grep -qx 'folia-supported: true' <<<"$descriptor"; then
        error "plugin.yml in $jar is missing 'folia-supported: true'"
    fi
fi

echo "==> Checking descriptor"
if ! grep -qx 'folia-supported: true' "$plugin_descriptor"; then
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
