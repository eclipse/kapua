package org.eclipse.kapua.service.authentication;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.guice.GuiceLocatorImpl;
import org.eclipse.kapua.service.camel.application.MetricsCamel;
import org.eclipse.kapua.service.device.authentication.api.DeviceConnectionCredentialAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Guice's injection bridge for Spring.
 *
 * @since 2.0.0
 */
@Configuration
public class SpringBridge {
    @Bean
    MetricsCamel metricsCamel() {
        return KapuaLocator.getInstance().getComponent(MetricsCamel.class);
    }

    @Bean
    MetricsAuthentication metricsAuthentication() {
        return KapuaLocator.getInstance().getComponent(MetricsAuthentication.class);
    }

    @Bean
    Map<String, DeviceConnectionCredentialAdapter> deviceConnectionCredentialAdapterMap() {
        return ((GuiceLocatorImpl) KapuaLocator.getInstance())
                .getInjector()
                .getInstance(
                        Key.get(
                                (TypeLiteral<Map<String, DeviceConnectionCredentialAdapter>>) TypeLiteral.get(Types.mapOf(String.class, DeviceConnectionCredentialAdapter.class))
                        )
                );
    }
}