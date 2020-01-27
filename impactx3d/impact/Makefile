JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
JAR=/usr/bin/jar
# CLASSPATH=..:.:/Users/yottzumm/dev/jogl-demos/www/webstart/jogl-demos-data-jsr-231.jar:/Users/yottzumm/dev/jogl-demos/www/webstart/jogl-demos-jsr-231.jar:/Users/yottzumm/dev/jogl-demos/www/webstart/jogl-demos-util-jsr-231.jar:/Users/yottzumm/dev/jogl/www/webstart/jogl-jsr-231.jar:/Users/yottzumm/dev/jogl/www/webstart/jogl-natives-macosx-jsr-231.jar:/Users/yottzumm/dev/joglutils/www/webstart/joglutils.jar:/Users/yottzumm/dev/jogl/src/classes
# CLASSPATH=..:.:/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib/jogl.jar:/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib/gluegen-rt.jar
# CLASSPATH=..:.:/Users/yottzumm/Downloads/jogl-1.1.2-pre-20080523-macosx-universal/lib/jogl.jar
CLASSPATH=..:.:/Users/johncarlson/Downloads/jogl/build/jogl/jogl.all.jar
# CLASSPATH=..:.:/Users/yottzumm/Library/Caches/Java/cache/javaws/http/Ddownload.java.net/P80/DMmedia/DMjogl/DMbuilds/DMarchive/DMjsr-231-webstart-current/RTjogl-natives-macosx-universal.jar:/Users/yottzumm/Library/Caches/Java/cache/javaws/http/Ddownload.java.net/P80/DMmedia/DMjogl/DMbuilds/DMarchive/DMjsr-231-webstart-current/RTjogl.jar:/Users/yottzumm/dev/httpunit/lib/httpunit.jar


all: compile
	(cd ..; ./impact3d.sh)

compile: ../impact3d.jar
	$(JAVAC) -target 1.5 -classpath $(CLASSPATH) *.java
	(cd ..; $(JAR) -cmf impact3d.manifest impact3d.jar impact/*.class impact/*.java)

picking:
	$(JAVAC) -classpath ../jogl.jar:.:.. Picking.java
	(export LD_LIBRARY_PATH=../obj; $(JAVA) -classpath $(CLASSPATH) impact.Picking) 
