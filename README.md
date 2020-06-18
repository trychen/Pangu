# Pangu
基于内容的 Minecraft Forge Mod 快速开发框架

![JitPack](https://jitpack.io/v/MinecraftPangu/Pangu.svg)

# Usage
Add the following code to your `build.gradle`
```groovy
repositories {
    maven { url 'https://www.jitpack.io' }
}

dependencies {
    implementation "com.github.MinecraftPangu:Pangu:${pangu_version}:dev"
}
```

Don't forget to add JVM Args `-Dfml.coreMods.load=cn.mccraft.pangu.core.asm.PanguPlugin` to load CoreMod if needed.
