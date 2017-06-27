/**
 * Gateway client SDK for Eclipse Kapua™
 * <p>
 * Provides easy access to Eclipse Kapua, acting as a gateway device.
 * </p>
 * <pre>
try (Client client = KuraMqttProfile.newProfile(FuseClient.Builder::new)
  .accountName("kapua-sys")
  .clientId("foo-bar-1")
  .brokerUrl("tcp://localhost:1883")
  .credentials(userAndPassword("kapua-broker", "kapua-password"))
  .build()) {

  try (Application application = client.buildApplication("app1").build()) {

    // subscribe to a topic

    application.data(Topic.of("my", "receiver")).subscribe(message {@code ->} {
      System.out.format("Received: %s%n", message);
    });

    // cache sender instance

    {@code Sender<RuntimeException>} sender = application
      .data(Topic.of("my", "sender"))
      .errors(ignore());

    int i = 0;
    while (true) {
      // send
      sender.send(Payload.of("counter", i++));
      Thread.sleep(1000);
    }
  }
}
 * </pre>
 */
package org.eclipse.kapua.client.gateway;