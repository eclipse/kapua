#!/bin/sh
export KAPUA_GIT_ROOT="$(pwd)"
export ACTIVEMQ_VERSION=5.14.0
export TOMCAT_VERSION=8.0.38

if [ -z "${KAPUA_GIT_ROOT}" ]; then
	KAPUA_GIT_ROOT=`git rev-parse --show-toplevel`
fi
VAGRANT=`which vagrant`
DOCKER=`which docker`

if [ -n "${VAGRANT}" ] ; then
	cd ../vagrant

	echo
	echo
	echo 'Creating/updating the base box.'
	echo 'If a base box with the same name will be found in the local'
	echo 'vagrant repository, the procedure will ask to confirm or deny'
	echo 'its replacement with a newly created version.'
	echo 'If the previous version is kept, then it will be subsequently'
	echo 'used to startup the demo vagrant machine.'
	echo

	./start.sh base-box

	echo 'creating demo vagrant machine'

	./start.sh demo

	cd $KAPUA_GIT_ROOT/../../../../

	mvn clean install -Psql -Pdeploy

	cd dev-tools/src/main/vagrant

	vagrant ssh -c "nohup /usr/local/kapua/apache-activemq-${ACTIVEMQ_VERSION}/bin/activemq start xbean:/usr/local/kapua/apache-activemq-5.14.0/conf/activemq.xml >/dev/null 2>&1 &
nohup /usr/local/kapua/apache-tomcat-${TOMCAT_VERSION}/bin/startup.sh >/dev/null 2>&1 &
ps -ef | grep active
ps -ef | grep tomcat"
else if [ -n "${DOCKER}"] ; then
	# no vagrant install found on the system, try docker
	cd $KAPUA_GIT_ROOT
	mvn -Pdocker
	mvn -Pdocker-push
	docker run -td --name kapua-sql -p 8181:8181 -p 3306:3306 kapua/kapua-sql
	docker run -td --name kapua-elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch:2.4 -Dcluster.name=kapua-datastore -Ddiscovery.zen.minimum_master_nodes=1
	docker run -td --name kapua-broker --link kapua-sql:db --link kapua-elasticsearch:es -p 1883:1883 -p 61614:61614 kapua/kapua-broker
	docker run -td --name kapua-console --link kapua-sql:db --link kapua-broker:broker -p 8080:8080 kapua/kapua-console-jetty
	docker run -td --name kapua-api --link kapua-sql:db --link kapua-broker:broker -p 8081:8080 kapua/kapua-api-jetty

else
	echo
	echo 'No supported virtualization tool found, please install either'
	echo 'vagrant or docker before proceeding.'
	echo
fi
