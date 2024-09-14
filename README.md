# jarQuery

[Kotlin](https://kotlinlang.org/) Command line (CLI) application that displays information about a Java 
Archive (JAR) file.

## Notes
* Classes in META-INF are ignored. This avoids jar files containing module information (Java 9)
from reporting a higher version of Java than the application will actually use.
* In an attempt to handle newer versions of Java released after this tool, the application
assumes the major version number of class files will increment by one for each release. This has
been true since version 1.1.


## Building
The project is built with [Gradle](https://gradle.org/).  Use `./gradlew shadowJar` to build.

# Goals and Ideas
* Search single or multiple jar files to only report jar files containing classes
whose target java version exceeds a specified value
* When searching multiple jars in a directory, use coroutines to speed up the process


# License
The terms for software licensing are detailed in the LICENSE.txt file,
located in the working directory.
