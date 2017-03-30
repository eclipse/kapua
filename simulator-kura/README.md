This is a framework to simulate Eclipse Kura gateways. It is intended
to provide help creating your own (virtual) Kura gateway instances for testing.

# Building

This module is built by its parent build.

# Running

This module can either be used a library but also provides main entry points for running as a
standalone application.

## Simulator runner

The main class is `org.eclipse.kapua.kura.simulator.main.SimulatorRunner`. It can be run after
the module was built by be executing e.g. (which will print out some basic help):

    java -jar target/kapua-simulator-kura-*-shaded.jar -?

For more information see the [User manual](../docs/user-manual) , section "Simulator runner".
