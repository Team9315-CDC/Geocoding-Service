JC = javac
JVM = java
JPATH = src/main/java/
GEOCODER = $(JPATH)geocoder/
WEBAPP = $(JPATH)webapp/

.SUFFIXES: .java .class
.java.class:
		$(JC) -g -d bin/ -cp src/  $*.java

# command to build:
# 	javac -g -d bin/ -cp src/ <insert path/names.java of all classes>

ALL = \
	$(GEOCODER)Address.java \
	$(GEOCODER)GeocodeRequest.java \
	$(JPATH)Server.java

default: all

all: $(ALL:.java=.class)

init:
	mkdir bin

run:
	$(JVM) -cp bin/ main.java.Server

# command to run:
#	java -cp bin/ main.java.Server

clean:
	find . -name "*.class" -type f -delete