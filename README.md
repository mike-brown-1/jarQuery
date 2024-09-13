# jarQuery

[Kotlin](https://kotlinlang.org/) Command line (CLI) application that displays information about a Java 
Archive (JAR) file.

## Building
The project is built with [Gradle](https://gradle.org/).


# Goals and Ideas
* Display information about classes in jar
  * File name
  * File size
  * File date
  * Target Java version
* Search single or multiple jar files to only report jar files containing classes
whose target java version exceeds a specified value
* Provide summary of jar: number of classes min and max target Java version
* Add flag to ignore module-info.class under META-INFO (Java 9 module feature).
Or always ignore classes in META-INFO?

# License
The terms for software licensing are detailed in the LICENSE.txt file,
located in the working directory.
