IMPACT=target/impact-2.0.0-SNAPSHOT.jar

impact: ${IMPACT}
	java -classpath ${IMPACT} impact.App

cell: ${IMPACT}
	java -classpath ${IMPACT} impact.Cell

${IMPACT}:
	/c/Users/coderextreme/apache-maven-3.6.3/bin/mvn install
