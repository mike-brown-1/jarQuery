# jarquery

[Kotlin](https://kotlinlang.org/) Command line (CLI) application that displays information about a Java 
Archive (JAR) file.

Requires Java 17 to run the program.

## Usage

```shell
Usage: jarquery [<options>]

Options:
  -j, --jar=<path>  Single jar file to process
  -d, --dir=<path>  Directory containing JAR files to process
  -c, --classes     Show information on classes in the jar file
  -m, --manifest    Show the manifest only
  -r, --recurse     With -d, search directory recursively
  --max=<int>       Only display jar files with java version > max
  --version         Show the version and exit
  -h, --help        Show this message and exit

```

## Notes
* Classes in META-INF directory are ignored. This avoids jar files containing module information (Java 9)
from reporting a higher version of Java than the application will actually use.
* In an attempt to handle newer versions of Java released after this tool, the application
assumes the major version number of class files will increment by one for each release. 


## Building
The project is built with [Gradle](https://gradle.org/).  Use `./gradlew shadowJar` to build.  This produces a single jar file
with all required dependencies to run the program.

### GraalVM
I installed the community edition of GraalVM for Java 21.  My development system is Linux Mint, which was missing
libz.  I had to install it:

```shell
sudo apt install libz-dev
```

The native image is built with:

```shell
./gradlew nativeCompile
```
# Goals and Ideas
* When searching multiple jars in a directory, use coroutines to speed up the process


# License
The terms for software licensing are detailed in the LICENSE.txt file,
located in the working directory.
