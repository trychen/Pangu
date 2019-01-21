# Pangu
基于内容的 Minecraft Forge Mod 快速开发框架

# Usage
Add the following code to your `build.gradle`
```groove
repositories {
    maven { url 'https://www.jitpack.io' }
}

dependencies {
    implementation "com.github.MinecraftPangu:Pangu:1.5.0:dev"
}

// IDE Runner Settings from https://blog.ustc-zzzz.net/add-jvm-arguments-to-ide-in-forge-mod-projects/
ext.jvmArguments = [
        '-Dfml.coreMods.load=cn.mccraft.pangu.core.asm.PanguPlugin'
]

makeEclipseCleanRunClient.doFirst {
    def jvmArg = String.join(' ', jvmArguments)
    it.setJvmArguments(jvmArg)
}

makeEclipseCleanRunServer.doFirst {
    def jvmArg = String.join(' ', jvmArguments)
    it.setJvmArguments(jvmArg)
}

idea.workspace.iws.withXml {
    def runManager = it.asNode().component.find({ it.@name == 'RunManager' })
    runManager.configuration.findAll({ it.@type == 'Application' }).each {
        def mainClass = it.option.find({ it.@name == 'MAIN_CLASS_NAME' }).@value
        if (mainClass == 'GradleStart' || mainClass == 'GradleStartServer') {
            def jvmArg = String.join(' ', jvmArguments)
            it.option.find({ it.@name == 'VM_PARAMETERS' }).@value = jvmArg
        }
    }
}
```
