# Manually Running the Kapua on Development Machine
This module provides script and configuration files to create a Vagrant machine able to run the following services:

* ActiveMQ
* Elasticsearch
* H2 Database
* Jetty

The machine provided (through a creation script) runs the Kapua components (_Message broker_, _REST API application_ and _Admin web console_) linking the compiled sources and the war files on the guest machine.
The machine is based on the Kapua `base-box`, so before creating it you need to create the `base-box` (see following instructions).

**Note:** 
Before proceeding, check that Vagrant is installed in your computer, otherwise install it. 
In order to run Vagrant machines, VirtualBox or other virtualization software supported by Vagrant 
needs also to be installed. 
`vagrant-vbguest` plugin is required. If missing, it will be installed without prompt for confirmation.

## Creating the Kapua base-box
To create the Kapua base box run the script with `base-box` as first parameter from the vagrant directory of the dev-tools module

```
$ cd $KAPUA_GITHUB_HOME_DIR/dev-tools/vagrant
$ ./start.sh base-box
```

After a while a new box, called ***kapua-dev-box/0.x***, will be created on the system. 
To check it please refer to the command showed in the ***Helpful Vagrant commands*** section.

**Note:** this step may take few minutes due to the components to be downloaded.

## Creating the Kapua dev-box

Once the `base-box` is created, from the same directory, it's possible to invoke the same script with a different value (as the first parameter) to create and start the development Vagrant machine.

```
$ cd $KAPUA_GITHUB_HOME_DIR
$ cd dev-tools/vagrant
$ ./start.sh develop
```

The machine can be created every time you need (running the same commands) or, alternatively, it's possible to freeze it and start it again later (see ***Helpful Vagrant commands*** section).
If you choose to start it again instead of recreating, you should enter the Vagrant machine and start manually the H2 service (please replace ${H2DB_VERSION} with the correct value):
```
$ cd $KAPUA_GITHUB_HOME_DIR
$ cd dev-tools/vagrant
$ vagrant up
$ vagrant ssh
$ java -cp /usr/local/h2/h2*.jar org.h2.tools.Server -baseDir /home/vagrant/H2/kapua -webAllowOthers -tcpAllowOthers -tcpPort 3306 &
```

## Running the Kapua dev-box

Run a full _Kapua_ project build from the project root directory outside of the Vagrant box.
```
$ mvn clean install -DskipTests -P console
```

Once the build has completed the development machine can be accessed.
```
$ vagrant ssh
```


Move under the _Message broker_ directory:
```
$ cd /usr/local/activemq/
```

And start it.

```
$ ./start-broker.sh
```
The script recreates the link between all the libraries and Kapua jars (inside Kapua workspace) needed by the _Message broker_ at runtime, recreate configuration links and then start the broker.
To stop the broker type:
```
bin/activemq stop
```

The servlet container (Jetty) directory is:
```
$ /usr/local/jetty/
```
There is a script to start the Jetty container:
```
$ ./start-jetty.sh
```

The script recreates the link between api and console wars inside the Kapua workspace and the webapps directory of Jetty. To stop Jetty call the `stop-jetty.sh` script in the same directory.

## Verifying and accessing Kapua components

Once the machine is created and started correctly there are several services running.
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

## Helpful Vagrant commands
You can check if there's already a Kapua `base-box` or `dev-box` created with the command:

```
$ vagrant box list
```

To connect to a running Vagrant machine:

```
$ vagrant ssh
```

To start a Vagrant machine:

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

For more please check [Vagrant documentation page](https://www.vagrantup.com/docs/).
