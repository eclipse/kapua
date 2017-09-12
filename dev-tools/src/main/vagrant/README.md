# Manually Running the Kapua on Dev/Demo Machine
This module provides script and configuration files to create 2 different Vagrant machine able to run the following services:

* ActiveMQ
* Elasticsearch
* H2 Database
* Tomcat

The 2 machine provided (through creation script) are:

- **Develop machine:** it's a machine that run the Kapua components (broker, api and console) linking the compiled sources (for the broker) and the war files (for the api and console) on the guest machine
- **Demo machine:** it's a machine that contains all the Kapua components deployed inside the broker and servlet container

Both of them are based on the Kapua base-box so before creating one of them (or both) you need to create the base-box (see following instructions).

The machines assigned IP is:
* 192.168.33.10

The ports exposed are documented per service within the Vagrant files.

**Note:** Before proceeding, check that Vagrant is installed in your PC, otherwise install it. In order to run Vagrant machines, VirtualBox or other virtualization software supported by Vagrant needs also to be installed. When using VirtualBox vagrant vagrant-vbguest plugin should also be installed.

## Demo machine quick start
It is possible to create the **demo machine in only one step** (so create the base box, the demo vagrant machine, compile the Kapua project and start the services inside the vagrant demo machine) just using this script

```
$ cd $KAPUA_GITHUB_HOME_DIR/dev-tools/src/main/bin
$ ./start-demo.sh
```

## Creating the Kapua base-box
To create the Kapua base box run the script with 'base-box' as first parameter from the vagrant directory of the dev-tools module

```
$ cd $KAPUA_GITHUB_HOME_DIR/dev-tools/src/main/vagrant
$ ./start.sh base-box
```

After a while a new box, called ***kapua-dev-box/0.x***, will be created on the system. To check it please refer to the command showed in the ***Helpful Vagrant commands*** section

**Note 1:** this step may take few minutes due to the components to be downloaded.
**Note 2:** the script will delete a previous created kapua-box, so due to Note 1, be careful before run this step.

## Creating the Kapua dev-box

Once the base-box is created, from the same directory, it's possible to invoke the same script with a different value (as the first parameter) to create and start the development Vagrant machine.

```
$ cd $KAPUA_GITHUB_HOME_DIR
$ cd dev-tools/src/main/vagrant
$ ./start.sh develop
```

The machine can be recreated every time you need (running the same commands) or, alternatively, it's possible to freeze it and start it again later (see ***Helpful Vagrant commands*** section).
If you choose to start it again instead of recreating, you should enter the Vagrant machine and start manually the H2 service (please replace ${H2DB_VERSION} with the correct value):
```
$ cd $KAPUA_GITHUB_HOME_DIR
$ cd dev-tools/src/main/vagrant
$ vagrant up
$ vagrant ssh
$ java -cp /usr/local/h2database/h2database-${H2DB_VERSION}/h2*.jar org.h2.tools.Server -baseDir /home/vagrant/H2/kapua -webAllowOthers -tcpAllowOthers -tcpPort 3306 &
```

## Running the Kapua dev-box

Once the development machine has been created (or started manually as described in the previous chapter), you can enter the machine by executing the command:
```
$ vagrant ssh
```

***Note:*** to allow the database creation and seeding the broker and the Tomcat. Please run a full Kapua project build from the project root directory outside of the vagrant box (see the command below)
```
$ mvn clean install -DskipTests
```

The broker installation directory is:
```
$ /usr/local/activemq/apache-activemq-${ACTIVEMQ_VERSION}
```

There is script to start the broker

```
$ ./start-broker.sh
```
The script recreates the link between all the libraries and Kapua jars (inside Kapua workspace) needed by the broker at runtime, recreate configuration links and then start the broker.
To stop the broker type:
```
bin/activemq stop
```

The servlet container (Tomcat) directory is:
```
$ /usr/local/tomcat/apache-tomcat-${TOMCAT_VERSION}
```
There is a script to start the Tomcat
```
$ ./start-tomcat.sh
```

The script recreates the link between api and console wars inside the Kapua workspace and the webapps directory of Tomcat. To stop Tomcat call the standard stop script inside bin directory.

## Verifying and accessing Kapua components

Once one of the 2 machines is created and started correctly (both of them have the same services at the same ports) there are several services running.
The list of available services is:

#### H2 Database
```
Web console url: 192.168.33.10:8082
Driver Class:    org.h2.Driver
JDBC url:        jdbc:h2:tcp://192.168.33.10:3306/kapuadb
Username:        kapua
Password:        kapua
```

#### Kapua Web Console
```
Url:      http://192.168.33.10:8080/admin/
Username: kapua-sys
Password: kapua-password
```

#### Kapua RESTful APIs:
```
Url:      http://192.168.33.10:8080/api/doc
Username: kapua-sys
Password: kapua-password
```

#### Kapua Broker:
```
MQTT url: mqtt://192.168.33.10:1883
Username: kapua-broker
Password: kapua-password
```

## Creating the Kapua demo-box
As for the development machine, once the base-box is created, from the same directory, it's possible to invoke the same script with a different value (as the first parameter) to create and start the demo Vagrant machine.

```
$ cd $KAPUA_GITHUB_HOME_DIR
$ cd dev-tools/src/main/vagrant
$ ./start.sh demo
```

The machine can be recreated every time you need (running the same commands) or, alternatively, it's possible to freeze it and start it again later (see ***Helpful Vagrant commands*** section).
If you choose to start it again instead of recreating, you should enter the Vagrant machine and start manually the H2 service (please replace ${H2DB_VERSION} with the correct value):
```
$ cd $KAPUA_GITHUB_HOME_DIR
$ cd dev-tools/src/main/vagrant
$ vagrant up demo
$ vagrant ssh demo
$ java -cp /usr/local/h2database/h2database-${H2DB_VERSION}/h2*.jar org.h2.tools.Server -baseDir /home/vagrant/H2/kapua -webAllowOthers -tcpAllowOthers -tcpPort 3306 &
```

## Running the Kapua demo-box

As for the development machine, once the demo machine has been created (or started manually as described in the previous chapter), you can enter the machine by executing the command:
```
$ vagrant ssh demo
```

The fresh machine has both the ActiveMQ and Tomcat installed, but please don't use them, it's just due to the reuse of the base-box.
To start ***using properly the demo machine*** run a full Kapua build as follow:

```
$ mvn clean install -PdeployVagrant -DskipTests
```

Once the full build is done, the machine has the broker with all the proper library and configuration installed in the directory
```
$ /usr/local/kapua/apache-activemq-${ACTIVEMQ_VERSION}
```

To start the broker type:
```
$ bin/activemq start
```

To stop it follow the instructions for the develop machine.

The api and console are properly installed in a Tomcat container in the directory
```
$ /usr/local/kapua/apache-tomcat-${TOMCAT_VERSION}/webapps
```

To start and stop the Tomcat please use the standard scripts under the bin directory of the Tomcat installation.
```
$ bin/startup.sh
```

## Helpful Vagrant commands
You can check if there's already a Kapua Dev-Box installed with the command:

```
$ vagrant box list
```

To connect to a running Vagrant machine:

```
$ vagrant ssh
```

To start a Vagrant machine (the machine may be already created or not):

```
$ vagrant up
```

To stop a running Vagrant machine:

```
$ vagrant halt
```

To destroy a Vagrant machine:

```
$ vagrant destroy
```
