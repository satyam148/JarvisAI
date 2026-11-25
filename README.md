# JARVIS AI - Phase 1

Package: com.jarvis.ai | minSdk 26 | target/compileSdk 34 | Kotlin 1.9.24 | AGP 8.5.2 | Gradle 8.7 | JDK 17

## Recommended: build in the cloud (GitHub Actions) - works from Termux
Android SDK tools (aapt2, d8, platform-tools) are x86_64 builds from Google; they do not
run reliably in Termux on ARM64. So build on GitHub and download the APK on the phone.

1. In Termux:  pkg update && pkg install git openssh
2. cd ~/storage/shared (or any folder), unzip the project:  unzip jarvis-ai-phase1.zip -d JarvisAI && cd JarvisAI
3. Make an empty repo on github.com, then:
     git init && git add . && git commit -m "phase 1"
     git branch -M main
     git remote add origin https://github.com/<user>/<repo>.git
     git push -u origin main      (use a Personal Access Token as the password)
4. GitHub > Actions > "Build APK" finishes in ~5-8 min. Download artifact "jarvis-ai-debug-apk".
5. Unzip it and install app-debug.apk (allow "Install unknown apps" for your file manager/browser).

## Local build in Termux (experimental, may fail)
  pkg install openjdk-17 gradle
  # needs an ARM64 Android SDK + ARM64 aapt2 (community builds, e.g. lzhiyong/termux-ndk, android-sdk-tools)
  export ANDROID_HOME=$HOME/android-sdk
  echo "sdk.dir=$ANDROID_HOME" > local.properties
  # then uncomment android.aapt2FromMavenOverride in gradle.properties
  gradle assembleDebug
APK path: app/build/outputs/apk/debug/app-debug.apk
Install:  termux-open app/build/outputs/apk/debug/app-debug.apk

## Troubleshooting
- "aapt2 ... Syntax error / not found": ARM64 aapt2 missing -> use the GitHub build.
- "SDK location not found": create local.properties with sdk.dir=...
- Out of memory: lower org.gradle.jvmargs in gradle.properties (e.g. -Xmx1g).
- "Unsupported class file major version": use JDK 17.

## Phase 1 status
Working: splash, dashboard (live battery + internet status, orb, mic permission, text input,
quick commands), chat UI (local only), bottom navigation, Settings (real permission status,
theme + animation toggle, stored locally).
Not yet (honestly marked in the UI): Gemini, Bunny voice, automation, Coding Studio.


## Phase 2 - Gemini chat
- Open Settings > AI SETTINGS, paste a Gemini API key and tap Save API key.
- Open Chat and send a message. The app calls Gemini 3.8 Flash over HTTPS.
- Create a key in Google AI Studio: https://aistudio.google.com/app/apikey
- Treat the key as a password. This prototype stores it in app-private preferences; do not distribute this APK or commit/share your key. A public release should call a backend that protects the key.
- Rebuild with GitHub Actions by pushing the updated project to the `main` branch.
