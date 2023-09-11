package org.eclipse.kapua.service.authentication;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import org.eclipse.kapua.client.security.metric.AuthMetric;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.guice.GuiceLocatorImpl;
import org.eclipse.kapua.service.authentication.setting.ServiceAuthenticationSetting;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.camel.application.MetricsCamel;
import org.eclipse.kapua.service.device.authentication.api.DeviceConnectionCredentialAdapter;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionFactory;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;
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

    @Bean
    AuthMetric authenticationMetric() {
        return KapuaLocator.getInstance().getComponent(AuthMetric.class);
    }

    @Bean
    DeviceConnectionOptionFactory deviceConnectionOptionFactory() {
        return KapuaLocator.getInstance().getComponent(DeviceConnectionOptionFactory.class);
    }

    @Bean
    DeviceConnectionOptionService deviceConnectionOptionService() {
        return KapuaLocator.getInstance().getComponent(DeviceConnectionOptionService.class);
    }

    @Bean
    AuthorizationService authorizationService() {
        return KapuaLocator.getInstance().getComponent(AuthorizationService.class);
    }

    @Bean
    DeviceConnectionFactory deviceConnectionFactory() {
        return KapuaLocator.getInstance().getComponent(DeviceConnectionFactory.class);
    }

    @Bean
    PermissionFactory permissionFactory() {
        return KapuaLocator.getInstance().getComponent(PermissionFactory.class);
    }

    @Bean
    DeviceConnectionService deviceConnectionService() {
        return KapuaLocator.getInstance().getComponent(DeviceConnectionService.class);
    }

    @Bean
    DeviceRegistryService deviceRegistryService() {
        return KapuaLocator.getInstance().getComponent(DeviceRegistryService.class);
    }

    @Bean
    SystemSetting systemSetting() {
        return KapuaLocator.getInstance().getComponent(SystemSetting.class);
    }

    @Bean
    ServiceAuthenticationSetting serviceAuthenticationSetting() {
        return KapuaLocator.getInstance().getComponent(ServiceAuthenticationSetting.class);
    }

    @Bean
    ServiceEventBus serviceEventBus() {
        return KapuaLocator.getInstance().getComponent(ServiceEventBus.class);
    }
}
