# Kura - Kapua connection

This is a short introduction into how locally connect Kura and Kapua. Although some of the info bellow can already be found on Kapua github project and Kura guides, this document contains everything needed for the connection. If you have any questions post them on [Eclipse Community Forums](https://www.eclipse.org/forums/index.php/f/340/) or join our [mailing list](https://dev.eclipse.org/mailman/listinfo/kapua-dev). In case you find any issues, [report them](https://github.com/eclipse/kapua/issues). Also if you want to contribute, [this is the place to start!](https://github.com/eclipse/kapua).

## Usefull links

 - [Kapua quick start guide](https://github.com/eclipse/kapua/blob/develop/README.md)
 - [Kapua User Manual](http://download.eclipse.org/kapua/docs/develop/user-manual/en/)
 - [Kapua Developer guide](http://download.eclipse.org/kapua/docs/develop/developer-guide/en/)
 - [Eclipse Kura Documentaion](http://eclipse.github.io/kura/)


## Hardware Requirements

 - Raspberry Pi
   - HDMI cable and monitor*
   - Keyboard*
   - Ethernet cable
 - PC (Windows/MAC/Linux)
 * External monitor and keyboard not mandatory, if you can determine Raspberry's IP without them and then establish ssh connection from local PC. 

 ## Software Requirements (for PC)
  - 64 bit architecture 
  - Java VM Version 8 
  - Docker Version 1.2+
  - Internet Access


## Downloading and configuring Kapua

This part of the tutorial consists of several pieces. First you need to download Java VM, Docker and Kapua. You can get Java [here](https://java.com/en/download/) and Docker [here](https://docs.docker.com/engine/installation/#supported-platforms). After that follow steps bellow for downloading and building Kapua.

1. Open OS Shell (Terminal) and go to home directory.
2. Download Kapua project from [Github repository](https://github.com/eclipse/kapua.git) with command `git clone https://github.com/eclipse/kapua.git`
3. Go to Kapua folder and run command `mvn clean install -f external/pom.xml` which will build some additional resources required by Kapua.
4. Go to Kapua folder and run command `mvn clean install -DskipTests -Pconsole` which will build the project with the Web Admin console.
5
6. After build finishes run Docker containers one by one (execute in Terminal): 
```
docker run -td --name kapua-sql -p 8181:8181 -p 3306:3306 kapua/kapua-sql
docker run -td --name kapua-elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch:5.4.0 -Ecluster.name=kapua-datastore -Ediscovery.type=single-node -Etransport.host=_site_ -Etransport.ping_schedule=-1 -Etransport.tcp.connect_timeout=30s
docker run -td --name kapua-broker --link kapua-sql:db --link kapua-elasticsearch:es --env commons.db.schema.update=true --env broker.ip=127.0.0.1 -p 1883:1883 -p 61614:61614 kapua/kapua-broker
docker run -td --name kapua-console --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es --env commons.db.schema.update=true -p 8080:8080 kapua/kapua-console
docker run -td --name kapua-api --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es --env commons.db.schema.update=true -p 8081:8080 kapua/kapua-api
```

Each line above starts a Docker container for each service. 

If the tag is not specified then the image tagged as latest will be used by default.

The images will be downloaded from Docker Hub and all the containers will be started.

You can check if every container is ok by typing the following command:

  docker ps -as

The output should be similar to this: 

```
CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS                     PORTS                                                                  NAMES                 SIZE
505d32a0bb9e        kapua/kapua-api       "/home/kapua/run-j..."   4 hours ago         Created                                                                                           kapua-api             0B (virtual 180MB)
dd9852845105        kapua/kapua-console   "/home/kapua/run-c..."   4 hours ago         Up 4 hours                 0.0.0.0:8080->8080/tcp, 8778/tcp                                       kapua-console         83kB (virtual 206MB)
65c8e15ab7e9        kapua/kapua-broker    "/maven/bin/active..."   4 hours ago         Up 4 hours                 8778/tcp, 0.0.0.0:1883->1883/tcp, 0.0.0.0:61614->61614/tcp, 8883/tcp   kapua-broker          94.4kB (virtual 212MB)
b3309e145e84        elasticsearch:5.4.0   "/docker-entrypoin..."   4 hours ago         Exited (137) 4 hours ago                                                                          kapua-elasticsearch   32.8kB (virtual 352MB)
e097f77f1758        kapua/kapua-sql       "/home/kapua/run-h2"     4 hours ago         Up 4 hours                 0.0.0.0:3306->3306/tcp, 0.0.0.0:8181->8181/tcp, 8778/tcp               kapua-sql             32.8kB (virtual 86MB)
```


5. Open web browser (Firefox or Chrome) and open the admin console at [http://localhost:8080/.](http://localhost:8080/)

If everything is OK, you should now see the login prompt. Enter username and password. 
<dl>
<dt>
<code>Username:</code>
<dd> 
kapua-sys
</dd>
</dt>
<dt>
<code>Password:</code>
<dd> 
kapua-password
</dd>
</dt>
</dl>

### Adding Account and User

Now you can login into Kapua, but we still need to create an Account and a User. This section has for steps. First one is creating a child account. 

While logged in as kapua-sys click **Child Accounts** and **Add** a user named User123.

<dl>
<dt> 
<code>Account Name: </code>
<dd>
Account123
</dd>
</dt>
<dt>
<code>Organization Name: </code>
<dd>
Account123org
</dd>
</dt>
<dt>
<code>Email: </code>
<dd>
Account123@user123.com
</dd>
</dt>
</dl>

Save the credentials with click on button **Submit**. 

Before we create a user under this account, we have to change some settings under **Settings** tab below. 
Basically every boolean setting has to be set on **true** except `TagService`. Every other setting should not be changed. 

Now click on **Users** tab and create new User with credentials: 

<dl>
<dt> 
<code>UserName: </code>
<dd>
User123
</dd>
</dt>
<dt>
<code>Password: </code>
<dd>
Kapu@12345678
</dd>
</dt>
<dt>
<code>Confirm Password: </code>
<dd>
Kapu@12345678
</dd>
</dt>
</dl>

User should be created successfully and we have to give this user some permissions. In upper right corner click **Switch Account** and select Account123. 
Go to **Users** and select User123. In lower part you have **Roles** and **Permissions** tabs. Click **Roles** and **Add**, select **admin** and submit changes. 

After that go to **Permissions** tab and click **Add**, select **ALL** in every checkbox and submit changes. 

Now we have everything we need to connect Kura to Kapua.  

## Downloading and cofiguring Kura

Second part of this guide describes how to configure Raspberry Pi, but you can also use BBB (BeagleBone Black) or Intel Edison [as described on Kura download page](http://www.eclipse.org/kura/downloads.php?).
Because there already exists guide for [installing Kura on Raspberry Pi](https://eclipse.github.io/kura/intro/raspberry-pi-quick-start.html) we will not repeat the procedure here. When you finish, come back and follow procedure bellow. In this example wi will assume that Raspberry Pi has IP: 192.168.1.11 and our PC 192.168.1.10.

1. Connect Raspberry Pi and local PC to the same network
2. On PC open browser and enter Raspberry's IP (which can be obtained with **ifconifg** command in Terminal of Raspberry Pi)
3. If IP is correct, login window should appear. Enter **admin** as username and **admin** as password. 

First we will configure the Cloud service, so we will be able to connect to our Kapua; We need to fill in the data from account and user we created earlier in Kapua.

1. Go to **Cloud Service** and under **MqttDataTransport** fill the following fields: 
   - **Broker URL: mqqt:**//[IP address of local PC]:1883/, e.g. mqtt://192.168.1.10:1883/
   - **Topic.context.account-name:** Account123
   - **Username:** User123
   - **Password:** Kapu@12345678
   - **Client-id:** test1 (it has to be same as **device custom name** in **CloudService**)
2. Go to **DataService** and under **connect.auto-on-startup** select **true**. 
3. All other fields should remain unchanged. 
4. Click **connect** and Kura should connect to Kapua (go to Kapua, under **devices** and **connections** there should be listed your Raspberry Pi with name **test1**.

Kura is now fully configured and is ready to send some data. 

## Sending data to Kapua

As you can see, our **data** sections Kapua is empty. That is because we did not yet send any data to it. So in this example we will user **Example publisher** deployment package, which can be obtained from [Eclipse marketplace](https://marketplace.eclipse.org/). 

This bundle sends some data to provided Cloud (in our case Kapua). We will use it to verify our connection. [Click here](https://marketplace.eclipse.org/content/example-publisher-eclipse-kura) to visit Example Publisher page. 

Click on download button and copy the link address (it has **.dp** extension) and paste it into **Kura -> Packages -> Install/Upgrade -> URL**. 

After clicking **Submit** package should be installed but not visible under **Services** on the left side (If it is listed, skip this step). 

Click **+** right from **search** box. Under **Factory** select **org.eclipse.kura.example.publisher.ExamplePublisher**, set a **Name** of your choice and click **Apply**. 

Example publisher should automatically start sending data to [Kapua](http://localhost:8080/), which can be verified in kura.log file or in Kapua itself (kura.log file is in on Raspberry Pi in /var/log folder).

If everything done correctly data should be transmitted to Kapua and accumulated in **data/metrics**. 







