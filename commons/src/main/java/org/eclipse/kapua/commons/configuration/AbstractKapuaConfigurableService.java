/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Configurable service definition abstract reference implementation.
 *
 * @since 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractKapuaConfigurableService extends AbstractKapuaService implements KapuaConfigurableService, Serializable {

    private Domain domain = null;
    private String pid = null;

    /**
     * Reads metadata for the service pid
     *
     * @param pid
     * @return
     * @throws IOException
     * @throws Exception
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     */
    private static KapuaTmetadata readMetadata(String pid)
            throws IOException, Exception, XMLStreamException, FactoryConfigurationError {
        KapuaTmetadata metaData = null;
        StringBuilder sbMetatypeXmlName = new StringBuilder();
        sbMetatypeXmlName.append("META-INF/metatypes/").append(pid).append(".xml");

        String metatypeXmlName = sbMetatypeXmlName.toString();
        URL metatypeXmlURL = ResourceUtils.getResource(metatypeXmlName);
        if (metatypeXmlURL != null) {
            String metatypeXml = ResourceUtils.readResource(metatypeXmlURL);
            metaData = XmlUtil.unmarshal(metatypeXml, KapuaTmetadata.class);
        }

        return metaData;
    }

    /**
     * Validate configuration
     *
     * @param pid
     * @param ocd
     * @param updatedProps
     * @throws KapuaException
     */
    private static void validateConfigurations(String pid, KapuaTocd ocd, Map<String, Object> updatedProps)
            throws KapuaException {
        if (ocd != null) {

            // build a map of all the attribute definitions
            Map<String, KapuaTad> attrDefs = new HashMap<String, KapuaTad>();
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
            if (ocd != null) {
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
            }
        }
    }

    /**
     * Convert the properties map to {@link Properties}
     *
     * @param values
     * @return
     */
    private static Properties toProperties(Map<String, Object> values) {
        Properties props = new Properties();
        for (Entry<String, Object> entry : values.entrySet())
            props.setProperty(entry.getKey(), StringUtil.valueToString(entry.getValue()));

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
    private static Map<String, Object> toValues(KapuaTocd ocd, Properties props) throws KapuaException {
        List<KapuaTad> ads = ocd.getAD();
        Map<String, Object> values = new HashMap<String, Object>();
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
     * @param em
     * @param serviceConfig
     * @return
     * @throws KapuaException
     */
    private ServiceConfig create(EntityManager em, ServiceConfig serviceConfig)
            throws KapuaException {
        try {

            em.beginTransaction();

            ServiceConfig newServiceConfig = ServiceConfigDAO.create(em, serviceConfig);
            newServiceConfig = ServiceConfigDAO.find(em, newServiceConfig.getId());
            em.commit();
            return newServiceConfig;
        } catch (Exception pe) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(pe);
        } finally {
            em.close();
        }

    }

    /**
     * Update the service configuration entity
     *
     * @param em
     * @param serviceConfig
     * @return
     * @throws KapuaException
     */
    private ServiceConfig update(EntityManager em, ServiceConfig serviceConfig)
            throws KapuaException {
        try {

            ServiceConfig theServiceConfig = ServiceConfigDAO.find(em, serviceConfig.getId());
            if (theServiceConfig == null) {
                throw new KapuaEntityNotFoundException(ServiceConfig.TYPE, serviceConfig.getId());
            }

            em.beginTransaction();
            ServiceConfigDAO.update(em, serviceConfig);
            em.commit();

            ServiceConfig updServiceConfig = ServiceConfigDAO.find(em, serviceConfig.getId());
            return updServiceConfig;
        } catch (Exception pe) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(pe);
        } finally {
            em.close();
        }
    }

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

    @Override
    public KapuaTocd getConfigMetadata()
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

        KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.read, scopeId));

        try {
            KapuaTmetadata metadata = readMetadata(this.pid);
            if (metadata != null && metadata.getOCD() != null && metadata.getOCD().size() > 0) {
                for (KapuaTocd ocd : metadata.getOCD()) {
                    if (ocd.getId() != null && ocd.getId().equals(pid)) {
                        return ocd;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw KapuaConfigurationException.internalError(e);
        }
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId)
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.read, scopeId));

        AndPredicate predicate = new AndPredicate()
                .and(new AttributePredicate<String>("pid", this.pid, Operator.EQUAL))
                .and(new AttributePredicate<KapuaId>("scopeId", scopeId, Operator.EQUAL));

        ServiceConfigQueryImpl query = new ServiceConfigQueryImpl(scopeId);
        query.setPredicate(predicate);

        Properties properties = null;
        EntityManager em = this.entityManagerFactory.createEntityManager();
        ServiceConfigListResult result = ServiceConfigDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), query);
        if (result != null && result.getSize() > 0)
            properties = result.getItem(0).getConfigurations();

        KapuaTocd ocd = this.getConfigMetadata();
        return toValues(ocd, properties);
    }

    @Override
    public void setConfigValues(KapuaId scopeId, Map<String, Object> values)
            throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.write, scopeId));

        KapuaTocd ocd = this.getConfigMetadata();
        validateConfigurations(this.pid, ocd, values);

        Properties props = toProperties(values);

        AndPredicate predicate = new AndPredicate()
                .and(new AttributePredicate<String>("pid", this.pid, Operator.EQUAL))
                .and(new AttributePredicate<KapuaId>("scopeId", scopeId, Operator.EQUAL));

        ServiceConfigQueryImpl query = new ServiceConfigQueryImpl(scopeId);
        query.setPredicate(predicate);

        ServiceConfig serviceConfig = null;
        EntityManager em = this.entityManagerFactory.createEntityManager();
        ServiceConfigListResultImpl result = ServiceConfigDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), query);

        // In not exists create then return
        if (result == null || result.getSize() == 0) {
            ServiceConfigImpl serviceConfigNew = new ServiceConfigImpl(scopeId);
            serviceConfigNew.setPid(this.pid);
            serviceConfigNew.setConfigurations(props);
            serviceConfig = this.create(em, serviceConfigNew);
            return;
        }

        // If exists update it
        serviceConfig = result.getItem(0);
        serviceConfig.setConfigurations(props);
        this.update(em, serviceConfig);
        return;
    }
}
