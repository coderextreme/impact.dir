#!/bin/bash -x
export DYLD_LIBRARY_PATH=/Users/yottzumm/Downloads/jogl-1.1.2-pre-20080523-macosx-universal/lib
java -Xmx256m -classpath /Users/yottzumm/Downloads/jogl-1.1.2-pre-20080523-macosx-universal/lib/jogl.jar:impact3d.jar:/Users/johncarlson/dev/httpunit/lib/httpunit.jar:/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib/gluegen-rt.jar -Djava.library.path=${DYLD_LIBRARY_PATH} impact.Impact3D
