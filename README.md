# Pangu
基于内容的 Minecraft Forge Mod 快速开发框架

# Usage
Add the following code to your `build.gradle`
```groovy
repositories {
    maven { url 'https://repo.trychen.com' }
}

dependencies {
    implementation "cn.mccraft.pangu:Pangu:3.9.10:dev"
}
```

Don't forget to add JVM Args `-Dfml.coreMods.load=cn.mccraft.pangu.core.asm.PanguPlugin` to load CoreMod if needed.
