JAVAC=/c/Program\ Files/Java/jdk13.0.1/jdk-13.0.1/bin/javac
JAVA=/c/Program\ Files/Java/jdk13.0.1/jdk-13.0.1/bin/java
JAR=/c/Program\ Files/Java/jdk13.0.1/jdk-13.0.1/bin/jar
JOGAMP_JAR=/c/Users/coderextreme/Downloads/jogamp-all-platforms/jar
CLASSPATH="${JOGAMP_JAR}/jogl-all.jar;${JOGAMP_JAR}/jogl-all-natives-windows-amd64.jar;..;.;${JOGAMP_JAR}/gluegen-rt.jar"


all: compile
	(cd ..; ./impact3d.sh)

compile:
	$(JAVAC) -classpath $(CLASSPATH) *.java
	(cd ..; $(JAR) -cmf impact3d.manifest impact3d.jar impact/*.class impact/*.java)

picking:
	$(JAVAC) -classpath "../jogl.jar;.;.. Picking.java
	(export LD_LIBRARY_PATH=../obj; $(JAVA) -classpath $(CLASSPATH) impact.Picking) 
