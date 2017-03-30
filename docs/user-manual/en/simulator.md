# Simulator

The Kura simulator is a simulator framework which can act as a Kura gateway instance, without the nessary
OSGi runtime and the required hardware abstractions. It can be used to create gateway simulations, testing
Kapua.

The following sections assume that you are located in the project folder `simulator-kura` and using a
bash-like shell running on Linux. While other environments are possible, some commands might need adaption
for this.

## Starting

The default main class is `org.eclipse.kapua.kura.simulator.main.SimulatorRunner`. It can be run after
the module was built by be executing e.g. (which will print out some basic help):

    java -jar target/kapua-simulator-kura-*-shaded.jar -?

Once properly started the simulator will keep running (other forever or until the specified time elapsed)
and then exit. While it is running it will try to stay connected to the broker. For more details about the
behavior of the simulator see: [Behavior of the simulator](#behavior-of-the-simulator).

## Controlling the simulator

The application's behavior can be configured either by using command line arguments or by setting
environment variables (not Java system properties).

Environment variables will take precedence over command line arguments.

### Simulator options

The following options are supported:

<dl>

<dt>
<code>-?</code>,
<code>--help</code>
</dt>
<dd>
Show help and exit
</dd>

<dt>
<code>-a</code>,
<code>--account-name</code>,
<code>KSIM_ACCOUNT_NAME</code> 
&lt;NAME&gt;
</dt>
<dd>
Set the Kapua account name, defaults to <code>kapua-sys</code>
</dd>

<dt>
<code>-c</code>,
<code>--count</code>,
<code>KSIM_NUM_GATEWAYS</code> 
&lt;COUNT&gt;
</dt>
<dd>
The number of instances to simulate, defaults to <code>1</code>
</dd>

<dt>
<code>-n</code>,
<code>--base-name</code>,
<code>KSIM_BASE_NAME</code> 
&lt;PREFIXS&gt;
</dt>
<dd>
The prefix used to construct the simulator id when using the <code>default</code> name factory, defaults to <code>sim-</code>
</dd>


<dt>
<code>--name-factory</code>,
<code>KSIM_NAME_FACTORY</code> 
&lt;NAME&gt;
</dt>
<dd>
The name factory to use for creating simulator names based on the instance number.
Also see <a href="#name-factories">Name Factories</a> below.
</dd>

<dt>
<code>-s</code>,
<code>--seconds</code>
&lt;SECONDS&gt;
</dt>
<dd>
The number of seconds after which the simulator will exit. The default is to keep running.
</dd>

</dl>

### Logging Options

Only one of the follow options may be active:

<dl>

<dt>
<code>-q</code>,
<code>--quiet</code>
</dt>
<dd>
Suppress output
</dd>

<dt>
<code>-v</code>,
<code>--verbose</code>
</dt>
<dd>
Show more details
</dd>

<dt>
<code>-d</code>,
<code>--debug</code>
</dt>
<dd>
Enable debug output
</dd>

</dl>

### Broker connection options

The connection to the broker requires a URL. It is possible to either specify the full URL or specify only
parts and let the simulator create the final URL. Both ways are mutually exclusive. 

For specifying the full URL use the following option:

<dl>

<dt>
<code>-b</code>,
<code>--broker</code>,
<code>KSIM_BROKER_URL</code>
</dt>
<dd>
The full URL to the broker
</dd>

</dl>

If the full URI is not specified then the simulator will create one based on the following
pattern: `<schema>://<user>:<password>@<host>:<port>` using the following options or
their defaults:

<dl>


<dt>
<code>KSIM_BROKER_SCHEMA</code>
</dt>
<dd>
The transport schema of the broker, defaults to <code>tcp</code>. The following options are available:

<dl>
<dt>tcp</dt><dd>MQTT over TCP</dd>
<dt>ssl</dt><dd>MQTT over TCP with TLS</dd>
<dt>ws</dt><dd>MQTT over WebSockets</dd>
<dt>wss</dt><dd>MQTT over WebSockets with TLS</dd>
</dl>
 
</dd>

<dt>
<code>-h</code>,
<code>--broker-host</code>,
<code>KSIM_BROKER_HOST</code>
</dt>
<dd>
The hostname of the broker, defaults to <code>localhost</code>
</dd>

<dt>
<code>KSIM_BROKER_PORT</code>
</dt>
<dd>
The port number of the broker, defaults to <code>1883</code>
</dd>

<dt>
<code>KSIM_BROKER_USER</code>
</dt>
<dd>
The user name to use for the MQTT connection to the broker, defaults to <code>kapua-broker</code>
</dd>

<dt>
<code>KSIM_BROKER_PASSWORD</code>
</dt>
<dd>
The password to use for the MQTT connection to the broker, defaults to <code>kapua-password</code>
</dd>

</dl>

If no broker connection options are specified this will result in a broker URL of: `tcp://kapua-broker:kapua-password@localhost:1883`.

### Name factories

The following name factories are available:

<dl>

<dt><code>default</code></dt>
<dd>Use the "base name" as prefix and append the instance number</dd>

<dt><code>host:name</code></dt>
<dd>
Use the local hostname as prefix, appending <code>-</code> and the instance number.
All special characters will get replaces with dashes (<code>-</code>).
</dd>

<dt><code>host:addr</code></dt>
<dd>
Use the IP address of the local hostname as prefix, appending <code>-</code> and the instance number.
All special characters will get replaces with dashes (<code>-</code>).
</dd>

<dt><code>host:iface:name</code></dt>
<dd>
Use the hostname of the main interface as prefix, appending <code>-</code> and the instance number.
All special characters will get replaces with dashes (<code>-</code>).
</dd>

<dt><code>host:iface:index</code></dt>
<dd>
Use the index number of the main interface as prefix, appending <code>-</code> and the instance number.
All special characters will get replaces with dashes (<code>-</code>).
</dd>

<dt><code>host:iface:mac</code></dt>
<dd>
Use the hex encoded MAC address of the main interface as prefix, appending <code>-</code> and the instance number.
All special characters will get replaces with dashes (<code>-</code>).
</dd>

</dl>

## Behavior of the simulator

Once the connection to the broker is established it will issue a "birth certificate" including some dummy information.
The information is intended to identify the different fields also by their content and the simulator ID.

The simulator currently supports the following control applications:

* **Command** The simulated command application will simply reply with some text indicating that the command could not be found.
* **Deployment** The deployment application will allow to view and controls OSGi bundles and allow to "download" DP archives.
  However the list of reported OSGi bundles and the download process are only simulated. But adding and removing DP archive will
  result in more or less OSGi bundles to be reported.
