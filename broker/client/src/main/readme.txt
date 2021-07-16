IMPORTANT:
the startup script for the vagrant machine creates symbolic links for all the dependencies needed by the broker so PLEASE RUN run the following modules build with the 
	dependency:copy-dependencies
maven goal
1) broker.core 
2) locator.guice

replace the guice locator module implementation with your own (if you re using a different locator implementation)


===========================================
DRAFT (replaced by the vagrant machine)
Broker activeMQ standalone without karaf

download ActiveMQ 5.11.0 from http://activemq.apache.org/activemq-5110-release.html (direct link http://archive.apache.org/dist/activemq/5.11.0/apache-activemq-5.11.0-bin.tar.gz)

unzip the file (under /usr/lib directory for example)
create the kapua directory under lib
cd ${ACTIVEMQ_BASE}
mkdir lib/kapua

add to bin/activemq script the following part
--extdir ${ACTIVEMQ_BASE}/lib/kapua
replacing the ******* in the original file (this is an extract)

# Execute java binary
   if [ -n "$PIDFILE" ] && [ "$PIDFILE" != "stop" ];then
      $EXEC_OPTION $DOIT_PREFIX "\"$JAVACMD\" $ACTIVEMQ_OPTS $ACTIVEMQ_DEBUG_OPTS \
              -Dactivemq.classpath=\"${ACTIVEMQ_CLASSPATH}\" \
              -Dactivemq.home=\"${ACTIVEMQ_HOME}\" \
              -Dactivemq.base=\"${ACTIVEMQ_BASE}\" \
              -Dactivemq.conf=\"${ACTIVEMQ_CONF}\" \
              -Dactivemq.data=\"${ACTIVEMQ_DATA}\" \
              $ACTIVEMQ_CYGWIN \
              -jar \"${ACTIVEMQ_HOME}/bin/activemq.jar\" $COMMANDLINE_ARGS ******* >/dev/null 2>&1 &
              RET=\"\$?\"; APID=\"\$!\";
              echo \$APID > "$PIDFILE";
              echo \"INFO: pidfile created : '$PIDFILE' (pid '\$APID')\";exit \$RET" $DOIT_POSTFIX

copy all the jars needed by the application to the lib/kapua directory (this is the current list):

  358048  4 Ago 16:02 commons-configuration-1.9.jar
 2308517  4 Ago 11:58 guava-27.1.jar
  162116  4 Ago 16:04 javax.persistence-2.1.1.jar
   17750  4 Ago 16:26 jbcrypt-0.3m.jar
    6583  4 Ago 12:29 metrics-annotation-3.1.2.jar
  112558  4 Ago 12:29 metrics-core-3.1.2.jar
 1727542  4 Ago 16:32 org.eclipse.kapua.broker.core-0.1-SNAPSHOT.jar
   16096  4 Ago 17:05 org.eclipse.kapua.message.internal-0.1-SNAPSHOT.jar
   23943  4 Ago 16:22 org.eclipse.kapua.service.account.internal-0.1-SNAPSHOT.jar
   86425  4 Ago 16:23 org.eclipse.kapua.service.auth.shiro-0.1-SNAPSHOT.jar
   47661  4 Ago 17:03 org.eclipse.kapua.service.device.registry.internal-0.1-SNAPSHOT.jar
   16257  4 Ago 16:23 org.eclipse.kapua.service.user.internal-0.1-SNAPSHOT.jar

copy the activemq.xml from the configuration directory inside the broker.core project to the ${ACTIVEMQ_BASE}/conf directory

to start the broker:
bin/activemq start xbean:conf/activemq.xml

to shutdown the broker:
bin/activemq stop


TODO
check the camel dependencies list (the activemq comes with few camel jars already integrated)
