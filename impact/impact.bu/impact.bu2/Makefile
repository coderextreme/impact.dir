impact: impact.jar
	java -classpath impact.jar:jnlp.jar Impact

cell: impact.jar
	java -classpath impact.jar:jnlp.jar Cell

impact.jar: *.gif *.java Makefile impact.manifest impact.jnlp
	javac -classpath impact.jar:jnlp.jar *.java
	jar -cmf impact.manifest impact.jar *.gif *.class *.java  Makefile
	(jar -cMf impact.war impact.jar impact.jnlp index.html WEB-INF)

install: impact.jar impact.war
	# cp impact.jar /home/carlsonj/WebCream4.4.1/wcapps/lib
	-rm -r "/cygdrive/c/Program Files/Apache Group/Tomcat 4.1/webapps/impact"
	cp impact.war "/cygdrive/c/Program Files/Apache Group/Tomcat 4.1/webapps"

clean:
	rm *.class
