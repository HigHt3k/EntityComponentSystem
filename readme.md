# SKYLOGIC
SkyLogic is a puzzle game developed for and with the ILS (Institute for Aircraft Systems) at the University of Stuttgart
to present redundancy concepts in system design to people with no knowledge of 
the topic.

## Build
To build the *.jar file, following steps are required:

1. install [maven](https://maven.apache.org/download.cgi)
2. install [jdk 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or lower
3. clone the repository
4. set the java-compiler to use byte-version 17 in the pom.xml (should already be configured)
5. use maven to build the project, this may differ depending on IDE.
   1. Jetbrains IntelliJ: Open Maven Tool Window, click Lifecycle -> package
   2. Eclipse: Right click project -> Maven Install / Build