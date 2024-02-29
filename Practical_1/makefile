all:
	make clean && make main && java Main ${task}

main:
	javac *.java

clean:
	rm -f *.class

coverage:
	rm -r org*/ jacoco-resources default com*/ -f
	rm *.class *.html *.exec *.xml *.csv -f
	javac *.java
	rm -Rf cov
	mkdir ./cov
	java -javaagent:jacocoagent.jar=excludes=org.jacoco.*,destfile=./cov/output.exec -cp ./ Main
	mv *.class ./cov
	java -jar ./jacococli.jar report ./cov/output.exec --classfiles ./cov --sourcefiles ./  --html ./cov/report