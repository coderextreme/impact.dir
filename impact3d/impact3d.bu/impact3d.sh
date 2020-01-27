#!/bin/bash
export DYLD_LIBRARY_PATH=~/Applications/jogl/build/obj
java -Xmx256m -classpath ~/Applications/jogl/build/jogl.jar:impact3d.jar impact.Impact3D yottzumm
