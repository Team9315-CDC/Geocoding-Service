# Team 9315

Git repository for Junior Design Project

## Building

Make sure you create a build directory:

``mkdir bin``

Or use the Makefile:

``make init``

You can then build the classes using `javac`:

``javac -g -d bin/ -cp src/ src/main/java/<name of class>.java``

or the Makefile

``make``

The default make will compile all the classes (if they were added to the make file).
If you want to use the Makefile, be sure to add any new classes to the ALL macro, so
they can be compiled.

## Running

Like `javac`, you need to specify the class path of the compiled files (using -cp):

``java -cp bin/ main.java.Server``

Again, you can also use the Makefile

``make run``

## Cleaning

To clean up all the built files, simply delete everything in the `bin/` directory. Or use

``make clean``

which will only remove all the .class files from `bin/`.