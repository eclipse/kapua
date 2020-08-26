# Simulator

The Kura simulator is a simulator framework which can act as a Kura gateway instance, without the necessary
OSGi runtime and the required hardware abstractions. It can be used to create gateway simulations, testing
Kapua.

The following sections assume that you are located in the project folder `simulator-kura` and using a
bash-like shell running on Linux. While other environments are possible, some commands might need adaption
for this.

## Starting

The default main class is `org.eclipse.kapua.kura.simulator.main.SimulatorRunner`. It can be run after
the module was built by executing e.g. (which will print out some basic help):

    java -jar target/kapua-simulator-kura-*-app.jar -?

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

Only one of the following options may be active:

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

### Data generation

The simulator runner can also provide a stream of metrics to be sent to Kapua. For this it is necessary to provide a
[simple simulation model](#simple-simulation-model)  which maps data generators with the actual metrics names.

The simulation configuration is read from a JSON configuration file and can be specified using a local file system path,
a URI from which the file can be loaded or a plain JSON content through an environment variable.

<dl>

<dt>
<code>-f</code>,
<code>--simulation</code>,
<code>KSIM_SIMULATION_URL</code>
</dt>
<dd>
A local file system path or a URI to the JSON configuration file of the data simulation.
</dd>

<dt>
<code>KSIM_SIMULATION_CONFIGURATION</code>
</dt>
<dd>
The actual JSON content for the simulation.
</dd>

</dl>

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

## Simple simulation model

This section describes the simple simulation model which is available in the basic simulation runner. The model
is intended to provide a stream of telemetry data for testing.

The basic runner allows to read a JSON representation of the model. Programmatically it is possible to
also other means of configuring a data simulator. This section will however focus on the JSON representation.

A default model would look like:

	{
		"applications": {
			"example1": {
				"scheduler": { "period": 2000 },
				"topics": {
					"t1/data": {
						"positionGenerator": "spos",
						"metrics": {
							"temp1": { "generator": "sine1", "name": "value" },
							"temp2": { "generator": "sine2", "name": "value" }
						}
					},
					"t2/data": {
						"metrics": {
							"temp1": { "generator": "sine1", "name": "value" },
							"temp2": { "generator": "sine2", "name": "value" }
						}
					}
				},
				"generators": {
					"sine1": {
						"type": "sine", "period": 1000, "offset": 50, "amplitude": 100
					},
					"sine2": {
						"type": "sine", "period": 2000, "shift": 45.5, "offset": 30, "amplitude": 100
					},
					"spos": {
						"type": "spos"
					}
				}
			}
		}
	}

Each model consists of a number of "applications", which represent actual Kura applications.

Each application consists of a scheduler, topics and generators. The scheduler defines when
the generator values will be refreshed. The topics then map the generated data to topics and metrics.

Generators typically generate their values on a function which maps from timestamp to a value. This
makes the generated values comparable as it is clear what can be expected as values. As the scheduler
runs all generators with exactly the same timestamp all generators in an application will generate the
same values. For simulators which spawn multiple instances in a single JVM, the scheduler will pass
the same timestamp to all applications of all simulator instances.

Under the "generators" section the generator instances get defined. The key can be later on used for
referencing the generator. The simulation runner will search, using the Java `ServiceLoader` mechanism
for generator implementations which can provide a generator. Typically the field `type` is used to
identify a generator. However every generator factory can opt-in to provide a working generation. If no
generator can be found, then the simulator will fail during startup. This way it is also possible
providing additional generator factories.

Generators can provide three different kinds of payload (body, position, metrics). It depends on the
generator implementation which kinds (maybe multiple) it provides. For metrics it also depends on the
generator which metric names it provides.

For example the "sine" generator provide a single metric named "value".

The topics section defines a map of topic and their payload mappings. The key of the map is the
name of the topic. The available field then are:

<dl>
<dt>bodyGenerator</dt><dd>The name of the generator providing the body (BLOB) content</dd>
<dt>positionGenerator</dt><dd>The name of the generator providing the position information</dd>
<dt>metrics</dt><dd>A map for mapping generator values to metric entries</dd>
</dl>

Each metric mapping again has a key (which is used a metric name) and the reference to the
generator (field `generator`) and a value of the generator (field `name`), as each
generator can technically provide a map of values.

In the above example there are three generators: sine1, sine2 and spos. The first two are
sine generators with different configurations and the last one is a position generator with
no further configuration.

There is only one application configured (`example1`), which has two topics (`t1/data` 
and `t2/data`). Each topic has two metrics (`temp1` and `temp2`), which each reference
to the same generator (`temp1` ⇒ `sine1` : `value`).

### Provided generators

The following generators are available out of the box.

#### Sine generator

**Processes:** `type = "sine"`

**Generates:** metrics

<dl>
<dt>value</dt><dd>A sine curve based on the provided parameters</dd>
</dl>

<table>
<thead><tr><th>Name</th><th>Default</th><th>Description</th></tr></thead>
<tbody>

<tr><td><code>period</code></td> <td>60000</td> <td>The period (in ms) for a full cycle of the curve</td></tr>
<tr><td><code>amplitude</code></td> <td>100</td> <td>The amplitude of the curve</td></tr>
<tr><td><code>offset</code></td> <td>0</td> <td>The offset of the curve</td></tr>
<tr><td><code>shift</code></td> <td></td> <td>The shift in degrees (0-360) of the curve</td></tr>

</tbody>
</table>

#### Straight position generator

**Processes:** `type = "spos"`

**Generates:** position

Generates a position on the map which will move straight east on the equator. Circling the earth every 80 days.
Speed, number of satellites, precision and altitude will be sine curves.

## Examples

To start simulator with an example simulation configuration, run:


    export KSIM_SIMULATION_CONFIGURATION=$(curl -s https://raw.githubusercontent.com/eclipse/kapua/develop/simulator-kura/src/test/resources/example1.json)
    java -jar target/kapua-simulator-kura-*-app.jar

