export KAPUA_GIT_ROOT="$(pwd)"
cd ../vagrant
echo 'creating base box'
./start.sh base-box
echo 'creating demo vagrant machine'
./start.sh demo
cd $KAPUA_GIT_ROOT/../../../../
mvn clean install -Psql -Pdeploy
cd dev-tools/src/main/vagrant
vagrant ssh -c "cd /usr/local/kapua/apache-activemq-*
    nohup bin/activemq start xbean:conf/activemq.xml &
    cd /usr/local/kapua/apache-tomcat-*
    nohup ./bin/startup.sh &"
