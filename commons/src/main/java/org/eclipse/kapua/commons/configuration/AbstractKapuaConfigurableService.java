/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

/**
 * Configurable service definition abstract reference implementation.
 */
public abstract class AbstractKapuaConfigurableService extends AbstractKapuaService implements KapuaConfigurableService {

    private Domain domain;
    private String pid;

    /**
     * Constructor
     *
     * @param pid
     * @param domain
     * @param entityManagerFactory
     */
    protected AbstractKapuaConfigurableService(String pid, Domain domain, EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
        this.pid = pid;
        this.domain = domain;
    }

    /**
     * Reads metadata for the service pid
     *
     * @param pid the persistent ID of the service
     * @return the metadata
     * @throws Exception In case of an error
     */
    private static KapuaTmetadata readMetadata(String pid) throws Exception {
        URL url = ResourceUtils.getResource(String.format("META-INF/metatypes/%s.xml", pid));

        if (url == null) {
            return null;
        }

        return XmlUtil.unmarshal(ResourceUtils.openAsReader(url, StandardCharsets.UTF_8), KapuaTmetadata.class);
    }

    /**
     * Validate configuration
     *
     * @param ocd
     * @param updatedProps
     * @param scopeId
     * @param parentId
     * @throws KapuaException
     */
    private void validateConfigurations(String pid, KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId)
            throws KapuaException {
        if (ocd != null) {

            // build a map of all the attribute definitions
            Map<String, KapuaTad> attrDefs = new HashMap<>();
            List<KapuaTad> defs = ocd.getAD();
            for (KapuaTad def : defs) {
                attrDefs.put(def.getId(), def);
            }

            // loop over the proposed property values
            // and validate them against the definition
            for (Entry<String, Object> property : updatedProps.entrySet()) {

                String key = property.getKey();
                KapuaTad attrDef = attrDefs.get(key);

                // is attribute undefined?
                if (attrDef == null) {
                    // we do not have an attribute descriptor to the validation
                    // against
                    // As OSGI insert attributes at runtime like service.pid,
                    // component.name,
                    // for the attribute for which we do not have a definition,
                    // just accept them.
                    continue;
                }

                // validate the attribute value
                Object objectValue = property.getValue();
                String stringValue = StringUtil.valueToString(objectValue);
                if (stringValue != null) {
                    ValueTokenizer tokenizer = new ValueTokenizer(stringValue);
                    String result = tokenizer.validate(attrDef);
                    if (result != null && !result.isEmpty()) {
                        throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ATTRIBUTE_INVALID, attrDef.getId() + ": " + result);
                    }
                }
            }

            // make sure all required properties are set
            for (KapuaTad attrDef : ocd.getAD()) {
                // to the required attributes make sure a value is defined.
                if (attrDef.isRequired()) {
                    if (updatedProps.get(attrDef.getId()) == null) {
                        // if the default one is not defined, throw
                        // exception.
                        throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.REQUIRED_ATTRIBUTE_MISSING, attrDef.getId());
                    }
                }
            }

            validateNewConfigValuesCoherence(ocd, updatedProps, scopeId, parentId);
        }
    }

    protected boolean validateNewConfigValuesCoherence(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId) throws KapuaException {
        return true;
    }

    /**
     * Convert the properties map to {@link Properties}
     *
     * @param values
     * @return
     */
    private static Properties toProperties(Map<String, Object> values) {
        Properties props = new Properties();
        for (Entry<String, Object> entry : values.entrySet()) {
            if (entry.getValue() != null) {
                props.setProperty(entry.getKey(), StringUtil.valueToString(entry.getValue()));
            }
        }

        return props;
    }

    /**
     * Convert the {@link Properties} to a properties map
     *
     * @param ocd
     * @param props
     * @return
     * @throws KapuaException
     */
    protected static Map<String, Object> toValues(KapuaTocd ocd, Properties props) throws KapuaException {
        List<KapuaTad> ads = ocd.getAD();
        Map<String, Object> values = new HashMap<>();
        for (KapuaTad ad : ads) {
            String valueStr = props == null ? ad.getDefault() : props.getProperty(ad.getId(), ad.getDefault());
            Object value = StringUtil.stringToValue(ad.getType().value(), valueStr);
            values.put(ad.getId(), value);
        }

        return values;
    }

    /**
     * Create the service configuration entity
     *
     * @param serviceConfig
     * @return
     * @throws KapuaException
     */
    private ServiceConfig createConfig(ServiceConfig serviceConfig)
            throws KapuaException {

        return entityManagerSession.onTransactedInsert(em -> ServiceDAO.create(em, serviceConfig));
    }

    /**
     * Update the service configuration entity
     *
     * @param serviceConfig
     * @return
     * @throws KapuaException
     */
    private ServiceConfig updateConfig(ServiceConfig serviceConfig)
            throws KapuaException {
        return entityManagerSession.onTransactedResult(em -> {
            ServiceConfig oldServiceConfig = ServiceConfigDAO.find(em, serviceConfig.getScopeId(), serviceConfig.getId());
            if (oldServiceConfig == null) {
                throw new KapuaEntityNotFoundException(ServiceConfig.TYPE, serviceConfig.getId());
            }

            if (!Objects.equals(oldServiceConfig.getScopeId(), serviceConfig.getScopeId())) {
                throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT, null, "scopeId");
            }
            if (!oldServiceConfig.getPid().equals(serviceConfig.getPid())) {
                throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.ILLEGAL_ARGUMENT, null, "pid");
            }

            // Update
            return ServiceConfigDAO.update(em, serviceConfig);
        });
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId)
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.read, scopeId));

        try {
            KapuaTmetadata metadata = readMetadata(pid);
            if (metadata != null && metadata.getOCD() != null && !metadata.getOCD().isEmpty()) {
                for (KapuaTocd ocd : metadata.getOCD()) {
                    if (ocd.getId() != null && ocd.getId().equals(pid)) {
                        return ocd;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId)
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.read, scopeId));

        AndPredicateImpl predicate = new AndPredicateImpl()
                .and(new AttributePredicateImpl<>(ServiceConfigAttributes.SERVICE_ID, pid, Operator.EQUAL))
                .and(new AttributePredicateImpl<>(KapuaEntityAttributes.SCOPE_ID, scopeId, Operator.EQUAL));

        ServiceConfigQueryImpl query = new ServiceConfigQueryImpl(scopeId);
        query.setPredicate(predicate);

        ServiceConfigListResult result = entityManagerSession.onResult(em -> ServiceDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), query));

        Properties properties = null;
        if (result != null && !result.isEmpty()) {
            properties = result.getFirstItem().getConfigurations();
        }

        KapuaTocd ocd = getConfigMetadata(scopeId);
        return toValues(ocd, properties);
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values)
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.write, scopeId));

        KapuaTocd ocd = getConfigMetadata(scopeId);
        validateConfigurations(pid, ocd, values, scopeId, parentId);

        ServiceConfigQueryImpl query = new ServiceConfigQueryImpl(scopeId);
        query.setPredicate(
                new AndPredicateImpl(
                        new AttributePredicateImpl<>(ServiceConfigAttributes.SERVICE_ID, pid, Operator.EQUAL),
                        new AttributePredicateImpl<>(KapuaEntityAttributes.SCOPE_ID, scopeId, Operator.EQUAL)
                )
        );

        ServiceConfigListResult result = entityManagerSession.onResult(em -> ServiceDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), query));

        Properties props = toProperties(values);
        if (result == null || result.isEmpty()) {
            // In not exists create then return
            ServiceConfig serviceConfigNew = new ServiceConfigImpl(scopeId);
            serviceConfigNew.setPid(pid);
            serviceConfigNew.setConfigurations(props);

            createConfig(serviceConfigNew);
        } else {
            // If exists update it
            ServiceConfig serviceConfig = result.getFirstItem();
            serviceConfig.setConfigurations(props);

            updateConfig(serviceConfig);
        }
    }
}
