coverage:
	rm -r org*/ jacoco-resources default com*/ -f
	rm *.class *.html *.exec *.xml *.csv -f
	javac *.java
	java -javaagent:jacocoagent.jar=excludes=org.jacoco.*,destfile=output.exec -cp ./ App
	mkdir ./temp
	mv *.class ./temp
	java -jar ./jacococli.jar report ./output.exec --classfiles ./temp --html report
	