#!/usr/bin/env bash

#runs integration tests and junit tests. Can take up to 2 hours

checkErrorLastBuild() {
	if [ "$?" -ne 0 ]; then #retrieve exit code of last command
		echo "$1";
		exit "$?";
	fi
}

cucumberTags=('@brokerAcl' '@tag' '@broker' '@device' '@deviceManagement' '@connection' '@datastore' '@user' '@userIntegrationBase' '@userIntegration' '@security' '@jobs' '@scheduler' '@jobsIntegrationBase' '@jobsIntegration' '@triggerService' '@triggerServiceIntegrationBase' '@triggerServiceIntegration' '@account' '@translator' '@jobEngineStepDefinitions' '@jobEngineStartOfflineDevice' '@jobEngineStartOnlineDevice' '@jobEngineRestartOfflineDevice' '@jobEngineRestartOnlineDevice' '@jobEngineRestartOnlineDeviceSecondPart' '@jobEngineServiceStop')
cucumberTagsEnvBroker=('@env_none' '@env_docker_base' '@broker' '@brokerAcl' '@connection' '@datastore' '@deviceManagement')
#2 hours long

#this line verifies existence of the lookup for the message-broker alias used in integration tests
grep -q "127.0.0.1 message-broker" /etc/hosts || { echo "password required to change hosts file" && { echo "127.0.0.1 message-broker" | sudo tee -a /etc/hosts;} }

for tag in "${cucumberTagsEnvBroker[@]}"
do
	echo "Started integration tests tagged as $tag";
	#bash -c 'echo "started tests tagged $0"' $tag;
	#bash -c 'echo " "!org.eclipse.kapua.qa.markers.junit.JUnitTests"" ';
	bash -c 'mvn verify -Dcommons.db.schema=kapuadb -Dcommons.settings.hotswap=true -Dbroker.host=localhost -Dgroups="!org.eclipse.kapua.qa.markers.junit.JUnitTests" -Dcrypto.secret.key=kapuaTestsKey!!! -Dcucumber.filter.tags=$0' $tag;
	checkErrorLastBuild "error on $tag tegged tests";
done
echo "Started junit tests";
mvn clean install -Dcommons.settings.hotswap=true -Dgroups='org.eclipse.kapua.qa.markers.junit.JUnitTests' -DskipITs=true;
checkErrorLastBuild "error on junit tests";
echo "tests finished without errors!" #if arrived till here tests are good

