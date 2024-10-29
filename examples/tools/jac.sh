 mvn jacoco:instrument
 
 ./run.sh src/main/java/tools/Main.java

 mvn jacoco:restore-instrumented-classes

 mvn jacoco:report

firefox target/site/jacoco/index.html


