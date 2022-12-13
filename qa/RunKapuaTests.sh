#!/usr/bin/env bash

#RUNS BOTH UNIT AND INTEGRATION TESTS AND FINALLY BUILDS THE PROJECT
#FIRT, JUNIT TESTS ARE LAUNCHED, IF THEY PASS CUCUMBER TESTS ARE THEN EXECUTED

cucumberTags=('@env_none' '@env_docker_base' '@env_docker')
testDescriptions=('minimal cucumber tests with embedded services and no containers' 'cucumber tests with a minimal set of containers' 'cucumber tests with a full deployment of containers')
exitCodesTests=() #will contain exit code for each batch of tests

#checks if the last build command exited normally and exits if necessary
checkErrorLastBuild() {
	if [ "$?" -ne 0 ]; then #retrieve exit code of last command
		echo "$1";
		exit "$?";
	fi
}

#print in tabular form the building phases for each batch of tests
printTestResults() {
  printf "junit tests passed, build launched with cucumber tests results:\n";
  printf "%s\t\t\t%s\t\t\t%s\n\n" "OUTCOME" "CUCUMBER_TAG" "BRIEF DESCRIPTION";
  for i in "${!cucumberTags[@]}"
  do
    if [ "${exitCodesTests[$i]}" -ne 0 ]; then #error on this test
      printf "%s\t\t%s\t\t\t%s\n" "BUILD FAIL" "${cucumberTags[$i]}" "${testDescriptions[$i]}";
    else
      printf "%s\t\t%s\t\t\t%s\n" "BUILD SUCCESS" "${cucumberTags[$i]}" "${testDescriptions[$i]}";
    fi
  done
  printf "Be aware that the build may have been failed for various reasons and not just test failures!\n";
  printf "For this reason, inspect the logs of each individual batch of tests: see files under <%s>\n" "qa/integration/target/surefire-reports/";
  printf "Also, tests could be affected by random errors as well as resources limitations of your machine\n";
  printf "For this reason, consider the option to run the GitHub continuous integration process of Kapua, which executes these tests on powerful remote machines\n";
}

#this line verifies existence of the lookup for the message-broker alias used in integration tests
grep -q "127.0.0.1 message-broker" /etc/hosts || { echo "password required to change hosts file" && { echo "127.0.0.1 message-broker" | sudo tee -a /etc/hosts;} }

echo "Started junit tests";
mvn clean verify -Dgroups='org.eclipse.kapua.qa.markers.junit.JUnitTests' -DskipITs=true;
checkErrorLastBuild "error on junit tests. Execution terminated because it's pointless to proceed with cucumber tests";
for tag in "${cucumberTags[@]}"
do
	echo "Started cucumber tests tagged as $tag";
	bash -c 'mvn verify -Dgroups="!org.eclipse.kapua.qa.markers.junit.JUnitTests" -Dcucumber.filter.tags=$0' $tag;
	checkErrorLastBuild "error while building with cucumber tests tagged as <$tag>...building has been terminated.
	You can view logs under qa/integration/target/surefire-reports/ for results of the precise tests";
done
echo "Started final, complete build"
mvn install -DskipTests=true #finally, build completely the project
