export KAPUA_GIT_ROOT="$(pwd)"
export ACTIVEMQ_VERSION=5.14.0
export TOMCAT_VERSION=8.0.37
cd ../vagrant
echo 'creating base box'
./start.sh base-box
echo 'creating demo vagrant machine'
./start.sh demo
cd $KAPUA_GIT_ROOT/../../../../
mvn clean install -Psql -Pdeploy
cd dev-tools/src/main/vagrant
vagrant ssh -c "nohup /usr/local/kapua/apache-activemq-5.14.0/bin/activemq start xbean:/usr/local/kapua/apache-activemq-5.14.0/conf/activemq.xml >/dev/null 2>&1 &
nohup /usr/local/kapua/apache-tomcat-8.0.37/bin/startup.sh >/dev/null 2>&1 &
ps -ef | grep active
ps -ef | grep tomcat"