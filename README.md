# OOOGG Opus Plugin
This library is a plugin for OOOGG that adds support for the Opus codec to it, allowing playback and recording of OGG Opus files (which have the .opus extension) through Java Sound API.

OOOGG Opus Plugin has two dependencies: OOOGG, an object-oriented OGG container implementation, and Concentus, a Java implementation of the Opus audio codec. OOOGG Opus Plugin connects to OOOGG's service provider interface and uses Concentus to encode and decode Opus audio packets.

OOOGG source code can be obtained in: https://github.com/allantaborda/ooogg

Concentus sorce code can be obtained in: https://github.com/lostromb/concentus

To include OOOGG Opus Plugin as a dependency in the pom.xml file (in the case of using Maven), simply include the following excerpt inside the "dependencies" tag of the file:

```
<dependency>
	<groupId>com.allantaborda</groupId>
	<artifactId>ooogg-opus-plugin</artifactId>
	<version>0.9.7-SNAPSHOT</version>
</dependency>
```
