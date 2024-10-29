# #! /bin/bash

# # Determine the location of the pom.xml file
# POM="$(dirname "$0")"/../../../pom.xml
# echo "Using POM file at: $POM"

# # Change to the root of the Maven project if not already there
# if ! [ -r pom.xml ]; then
#     cd "$(dirname "$0")"/../../../
# fi

# # Collect additional arguments if provided
# if [ "$#" -gt 0 ]; then
#     args="$*"
# else
#     args=""
# fi

# mvn clean
# mvn compile

# mvn jacoco:instrument
 
# #  src/test/script/tests_runners/run_all_tests.sh

# mvn -q -f $POM -Djacoco.skip=false jacoco:restore-instrumented-classes
# mvn -f $POM -Djacoco.skip=false jacoco:report $args


mvn clean 
mvn compile 
mvn -Djacoco.skip=false verify
mvn -Djacoco.skip=false jacoco:restore-instrumented-classes

firefox target/site/jacoco/index.html &




