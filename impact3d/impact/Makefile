JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
JAR=/usr/bin/jar
JOGAMP_JAR=/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar
CLASSPATH=$(JOGAMP_JAR)/jogl-all-natives-macosx-universal.jar:$(JOGAMP_JAR)/jogl-all.jar:..:.:$(JOGAMP_JAR)/gluegen-rt.jar


all: compile
	(cd ..; ./impact3d.sh)

compile: ../impact3d.jar
	$(JAVAC) -classpath $(CLASSPATH) *.java
	(cd ..; $(JAR) -cmf impact3d.manifest impact3d.jar impact/*.class impact/*.java)

picking:
	$(JAVAC) -classpath ../jogl.jar:.:.. Picking.java
	(export LD_LIBRARY_PATH=../obj; $(JAVA) -classpath $(CLASSPATH) impact.Picking) 
