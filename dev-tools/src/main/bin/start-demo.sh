export KAPUA_GIT_ROOT="$(pwd)"
export ACTIVEMQ_VERSION=5.14.5
export TOMCAT_VERSION=8.0.41

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

mvn clean install -PdeployVagrant,console -D skipTests

cd dev-tools/src/main/vagrant

vagrant ssh demo -c "nohup /usr/local/kapua/apache-activemq-${ACTIVEMQ_VERSION}/bin/activemq start xbean:/usr/local/kapua/apache-activemq-${ACTIVEMQ_VERSION}/conf/activemq.xml >/dev/null 2>&1 &
nohup /usr/local/kapua/apache-tomcat-${TOMCAT_VERSION}/bin/startup.sh >/dev/null 2>&1 &
ps -ef | grep active
ps -ef | grep tomcat"