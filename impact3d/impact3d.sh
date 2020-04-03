#!/bin/bash -x
export JOGAMP_JAR="/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar"
java -Dsun.awt.noerasebackground=true -Xmx256m -classpath ${JOGAMP_JAR}/jogl-all-natives-macosx-universal.jar:${JOGAMP_JAR}/jogl-all.jar:impact3d.jar:${JOGAMP_JAR}/gluegen-rt.jar impact.Impact3D
