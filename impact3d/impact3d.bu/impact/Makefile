all:
	javac -target 1.5 -classpath ~/Applications/jogl-1_0_0-macosx-universal/lib/jogl.jar:.:.. *.java
	(cd ..; jar -cmf impact3d.manifest impact3d.jar impact/*.class impact/*.java; ./impact3d.sh)

picking:
	javac -classpath ~/Applications/jogl/build/jogl.jar:.:.. Picking.java
	(export DYLD_LIBRARY_PATH=~/Applications/jogl/build/obj; java -classpath ~/Applications/jogl/build/jogl.jar:.. impact.Picking)

