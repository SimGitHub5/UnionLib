# UnionLib [![Minecraft Version](https://img.shields.io/badge/minecraft-1.18-blue.svg)](#)

## How to add to your Project:

Insert the following text to your `build.gradle` file:
```
repositories {
    maven {
        url = "https://modmaven.dev/"
    }
}
dependencies {
	//replace ${version} with the version of UnionLib you want to use
    implementation fg.deobf("com.stereowalker.unionlib:UnionLib:${version}")
}
```