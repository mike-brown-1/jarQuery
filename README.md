# jarQuery

[Kotlin](https://kotlinlang.org/) Command line (CLI) application that displays information about a Java 
Archive (JAR) file.

## Building
The project is built with [Gradle](https://gradle.org/).


# Goals and Ideas
* Use Picocli to parse command line options
* Display manifest file, if available
* Ensure the jar file has the "magic number"
* Display information about jar file
  * File name
  * File size
  * File date
  * Display minimum and maximum target Java version
  * Display number of classes
* Display information about classes in jar
  * File name
  * File size
  * File date
  * Target Java version
* Specify jar file on the command line
* Specify directory on the command line, all jar file in that directory will be processed
* Search single or multiple jar files to only report jar files containing classes
whose target java version exceeds a specified value
* Provide summary of jar: number of classes min and max target Java version

# License
The terms for software licensing are detailed in the LICENSE.txt file,
located in the working directory.

