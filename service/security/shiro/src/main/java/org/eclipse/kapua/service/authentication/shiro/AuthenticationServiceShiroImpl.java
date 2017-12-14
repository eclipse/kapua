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
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.subject.Subject;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificatePredicates;
import org.eclipse.kapua.service.certificate.CertificateQuery;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.util.CertificateUtils;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Authentication service implementation.
 * <p>
 * since 1.0
 */
@KapuaProvider
public class AuthenticationServiceShiroImpl implements AuthenticationService {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationServiceShiroImpl.class);

    static {
        // org.apache.shiro.config.Ini
        // org.apache.shiro.config.IniSecurityManagerFactory
        // org.apache.shiro.util.Factory;
        // Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        // SecurityUtils.setSecurityManager(securityManager);

        // Make the SecurityManager instance available to the entire application:
        Collection<Realm> realms = new ArrayList<>();
        try {
            realms.add(new org.eclipse.kapua.service.authentication.shiro.realm.UserPassAuthenticatingRealm());
            realms.add(new org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizingRealm());
        } catch (KapuaException e) {
            // TODO add default realm???
        }

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setAuthenticator(new KapuaAuthenticator());
        logger.info("Set '{}' as shiro authenticator", KapuaAuthenticator.class);
        // if (defaultSecurityManager.getAuthenticator() instanceof ModularRealmAuthenticator) {
        // KapuaAuthenticationStrategy authenticationStrategy = new KapuaAuthenticationStrategy();
        // ((ModularRealmAuthenticator) defaultSecurityManager.getAuthenticator()).setAuthenticationStrategy(authenticationStrategy);
        // logger.info("Set '{}' as shiro authentication strategy ", KapuaAuthenticationStrategy.class);
        // }
        // else {
        // logger.warn("Cannot set '{}' as shiro authentication strategy! Authenticator class is '{}' and this option is only available for ModularRealmAuthenticator!",
        // new Object[]{KapuaAuthenticationStrategy.class, defaultSecurityManager.getAuthenticator() != null ? defaultSecurityManager.getAuthenticator().getClass() : "null"});
        // }

        defaultSecurityManager.setRealms(realms);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        // TODO in the old application this code was executed only inside the broker context
        // but now it's no more needed since we are using an external service (that returns a token) to authenticate the broker component
        if (defaultSecurityManager.getSessionManager() instanceof AbstractSessionManager) {
            ((AbstractSessionManager) defaultSecurityManager.getSessionManager()).setGlobalSessionTimeout(-1);
            logger.info("Shiro global session timeout set to indefinite.");
        } else {
            logger.warn("Cannot set Shiro global session timeout to indefinite.");
        }
        if (defaultSecurityManager.getSessionManager() instanceof AbstractValidatingSessionManager) {
            ((AbstractValidatingSessionManager) defaultSecurityManager.getSessionManager()).setSessionValidationSchedulerEnabled(false);
            logger.info("Shiro global session validator scheduler disabled.");
        } else {
            logger.warn("Cannot disable Shiro session validator scheduler.");
        }
    }

    @Override
    public AccessToken login(LoginCredentials loginCredentials) throws KapuaException {

        checkCurrentSubjectNotAuthenticated();

        //
        // Parse login credentials
        AuthenticationToken shiroAuthenticationToken;
        if (loginCredentials instanceof UsernamePasswordCredentialsImpl) {
            shiroAuthenticationToken = new UsernamePasswordCredentialsImpl(((UsernamePasswordCredentialsImpl) loginCredentials).getUsername(),
                    ((UsernamePasswordCredentialsImpl) loginCredentials).getPassword());
        } else if (loginCredentials instanceof ApiKeyCredentialsImpl) {
            shiroAuthenticationToken = new ApiKeyCredentialsImpl(((ApiKeyCredentialsImpl) loginCredentials).getApiKey());
        } else if (loginCredentials instanceof JwtCredentialsImpl) {
            shiroAuthenticationToken = new JwtCredentialsImpl(((JwtCredentialsImpl) loginCredentials).getJwt());
        } else {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED);
        }

        //
        // Login the user
        AccessToken accessToken;
        Subject currentUser = null;
        try {
            //
            // Shiro login
            currentUser = SecurityUtils.getSubject();
            currentUser.login(shiroAuthenticationToken);

            //
            // Create the access token
            Subject shiroSubject = SecurityUtils.getSubject();
            Session shiroSession = shiroSubject.getSession();
            accessToken = createAccessToken(shiroSession);

            //
            // Establish session
            establishSession(shiroSubject, accessToken);

            //
            // Set some logging
            MDC.put(KapuaSecurityUtils.MDC_USER_ID, accessToken.getUserId().toCompactId());
            logger.info("Login for thread '{}' - '{}' - '{}'", Thread.currentThread().getId(), Thread.currentThread().getName(), shiroSubject);

        } catch (ShiroException se) {

            KapuaAuthenticationException kae;
            if (se instanceof UnknownAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof LockedAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof DisabledAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof IncorrectCredentialsException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof ExpiredCredentialsException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS, se, shiroAuthenticationToken.getPrincipal());
            } else {
                throw KapuaException.internalError(se);
            }

            if (currentUser != null) {
                currentUser.logout();
            }

            throw kae;
        }

        return accessToken;
    }

    @Override
    public void authenticate(SessionCredentials sessionCredentials) throws KapuaException {
        checkCurrentSubjectNotAuthenticated();

        //
        // Parse login credentials
        AuthenticationToken shiroAuthenticationToken;
        if (sessionCredentials instanceof AccessTokenCredentialsImpl) {
            shiroAuthenticationToken = new AccessTokenCredentialsImpl(((AccessTokenCredentialsImpl) sessionCredentials).getTokenId());
        } else {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED);
        }

        //
        // Login the user
        Subject currentUser = null;
        try {
            //
            // Shiro login
            currentUser = SecurityUtils.getSubject();
            currentUser.login(shiroAuthenticationToken);

            //
            // Retrieve token
            AccessToken accessToken = findAccessToken((String) shiroAuthenticationToken.getCredentials());

            //
            // Enstablish session
            establishSession(currentUser, accessToken);

            //
            // Set some logging
            Subject shiroSubject = SecurityUtils.getSubject();
            MDC.put(KapuaSecurityUtils.MDC_USER_ID, accessToken.getUserId().toCompactId());
            logger.info("Login for thread '{}' - '{}' - '{}'", Thread.currentThread().getId(), Thread.currentThread().getName(), shiroSubject);
        } catch (ShiroException se) {

            KapuaAuthenticationException kae;
            if (se instanceof UnknownAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.UNKNOWN_SESSION_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof DisabledAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.DISABLED_SESSION_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof LockedAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.LOCKED_SESSION_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof IncorrectCredentialsException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_SESSION_CREDENTIALS, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof ExpiredCredentialsException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.EXPIRED_SESSION_CREDENTIALS, se, shiroAuthenticationToken.getPrincipal());
            } else {
                throw KapuaException.internalError(se);
            }

            if (currentUser != null) {
                currentUser.logout();
            }
            throw kae;
        }

    }

    @Override
    public void logout()
            throws KapuaException {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
            if (kapuaSession != null) {
                AccessToken accessToken = kapuaSession.getAccessToken();

                if (accessToken != null) {
                    KapuaLocator locator = KapuaLocator.getInstance();
                    AccessTokenService accessTokenService = locator.getService(AccessTokenService.class);
                    KapuaSecurityUtils.doPrivileged(() -> {
                        accessTokenService.invalidate(accessToken.getScopeId(), accessToken.getId());
                    });
                }
            }
            currentUser.logout();
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        } finally {
            KapuaSecurityUtils.clearSession();
        }
    }

    @Override
    public AccessToken findAccessToken(String tokenId) throws KapuaException {
        AccessToken accessToken = null;
        try {
            KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
            if (kapuaSession != null) {
                accessToken = kapuaSession.getAccessToken();

                if (accessToken == null) {
                    KapuaLocator locator = KapuaLocator.getInstance();
                    AccessTokenService accessTokenService = locator.getService(AccessTokenService.class);
                    accessToken = accessTokenService.findByTokenId(tokenId);
                }
            }
        } finally {
            KapuaSecurityUtils.clearSession();
        }

        return accessToken;
    }

    @Override
    public AccessToken refreshAccessToken(String tokenId, String refreshToken) throws KapuaException {
        Date now = new Date();
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessTokenService accessTokenService = locator.getService(AccessTokenService.class);
        AccessToken expiredAccessToken = KapuaSecurityUtils.doPrivileged(() -> findAccessToken(tokenId));
        if (expiredAccessToken == null ||
                expiredAccessToken.getInvalidatedOn() != null && now.after(expiredAccessToken.getInvalidatedOn()) ||
                !expiredAccessToken.getRefreshToken().equals(refreshToken) ||
                expiredAccessToken.getRefreshExpiresOn() != null && now.after(expiredAccessToken.getRefreshExpiresOn())) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.REFRESH_ERROR);
        }
        KapuaSecurityUtils.doPrivileged(() -> {
            accessTokenService.invalidate(expiredAccessToken.getScopeId(), expiredAccessToken.getId());
            return null;
        });
        return createAccessToken((KapuaEid) expiredAccessToken.getScopeId(), (KapuaEid) expiredAccessToken.getUserId());
    }

    @Override
    public boolean verifyCredentials(LoginCredentials loginCredentials) throws KapuaException {
        //
        // Parse login credentials
        AuthenticationToken shiroAuthenticationToken;
        if (loginCredentials instanceof UsernamePasswordCredentialsImpl) {
            shiroAuthenticationToken = new UsernamePasswordCredentialsImpl(((UsernamePasswordCredentialsImpl) loginCredentials).getUsername(),
                    ((UsernamePasswordCredentialsImpl) loginCredentials).getPassword());
        } else {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_CREDENTIALS_TYPE_PROVIDED);
        }

        //
        // Login the user
        Subject currentUser = null;
        try {
            //
            // Shiro login
            currentUser = SecurityUtils.getSubject();
            currentUser.login(shiroAuthenticationToken);
        } catch (ShiroException se) {

            KapuaAuthenticationException kae;
            if (se instanceof UnknownAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.UNKNOWN_LOGIN_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof LockedAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.LOCKED_LOGIN_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof DisabledAccountException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.DISABLED_LOGIN_CREDENTIAL, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof IncorrectCredentialsException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.INVALID_LOGIN_CREDENTIALS, se, shiroAuthenticationToken.getPrincipal());
            } else if (se instanceof ExpiredCredentialsException) {
                kae = new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.EXPIRED_LOGIN_CREDENTIALS, se, shiroAuthenticationToken.getPrincipal());
            } else {
                throw KapuaException.internalError(se);
            }

            if (currentUser != null) {
                currentUser.logout();
            }

            throw kae;
        }

        return true;
    }

    //
    // Private Methods
    //

    /**
     * Checks if the Shiro {@link Subject} is authenticated or not.
     * If {@link Subject#isAuthenticated()} {@code equals true}, {@link KapuaAuthenticationException} is raised.
     *
     * @throws KapuaAuthenticationException If {@link Subject#isAuthenticated()} {@code equals true}
     * @since 1.0
     */
    private void checkCurrentSubjectNotAuthenticated()
            throws KapuaAuthenticationException {
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser != null && currentUser.isAuthenticated()) {
            logger.info("Thread already authenticated for thread '{}' - '{}' - '{}'", Thread.currentThread().getId(), Thread.currentThread().getName(), currentUser);
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.SUBJECT_ALREADY_LOGGED);
        }
    }

    /**
     * Create and persist a {@link AccessToken} from the data contained in the Shiro {@link Session}
     *
     * @param session The Shiro {@link Session} from which extract data
     * @return The persisted {@link AccessToken}
     * @throws KapuaException
     * @since 1.0
     */
    private AccessToken createAccessToken(Session session) throws KapuaException {
        //
        // Extract userId and scope id from the shiro session
        KapuaEid scopeId = (KapuaEid) session.getAttribute("scopeId");
        KapuaEid userId = (KapuaEid) session.getAttribute("userId");

        return createAccessToken(scopeId, userId);
    }

    /**
     * Create and persist a {@link AccessToken} from a scopeId and a userId
     *
     * @param scopeId The scopeID
     * @param userId  The userID
     * @return The persisted {@link AccessToken}
     * @throws KapuaException
     * @since 1.0
     */
    private AccessToken createAccessToken(KapuaEid scopeId, KapuaEid userId) throws KapuaException {
        //
        // Create the access token
        KapuaLocator locator = KapuaLocator.getInstance();
        AccessTokenService accessTokenService = locator.getService(AccessTokenService.class);
        AccessTokenFactory accessTokenFactory = locator.getFactory(AccessTokenFactory.class);

        // Retrieve TTL access token
        KapuaAuthenticationSetting settings = KapuaAuthenticationSetting.getInstance();
        long tokenTtl = settings.getLong(KapuaAuthenticationSettingKeys.AUTHENTICATION_TOKEN_EXPIRE_AFTER);
        long refreshTokenTtl = settings.getLong(KapuaAuthenticationSettingKeys.AUTHENTICATION_REFRESH_TOKEN_EXPIRE_AFTER);
        // Generate token
        Date now = new Date();
        String jwt = generateJwt(scopeId, userId, now, tokenTtl);

        // Persist token
        AccessTokenCreator accessTokenCreator = accessTokenFactory.newCreator(scopeId,
                userId,
                jwt,
                new Date(now.getTime() + tokenTtl),
                UUID.randomUUID().toString(),
                new Date(now.getTime() + refreshTokenTtl));
        AccessToken accessToken;
        try {
            accessToken = KapuaSecurityUtils.doPrivileged(() -> accessTokenService.create(accessTokenCreator));
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }

        return accessToken;
    }

    private void establishSession(Subject subject, AccessToken accessToken) {
        KapuaSession kapuaSession = new KapuaSession(accessToken, accessToken.getScopeId(), accessToken.getUserId());
        KapuaSecurityUtils.setSession(kapuaSession);
        subject.getSession().setAttribute(KapuaSession.KAPUA_SESSION_KEY, kapuaSession);
    }

    private String generateJwt(KapuaEid scopeId, KapuaEid userId, Date now, long ttl) {

        KapuaAuthenticationSetting settings = KapuaAuthenticationSetting.getInstance();

        //
        // Build claims
        JwtClaims claims = new JwtClaims();

        // Reserved claims
        String issuer = settings.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_ISSUER);
        Date issuedAtDate = now; // Issued at claim
        Date expiresOnDate = new Date(now.getTime() + ttl); // Expires claim.

        claims.setIssuer(issuer);
        claims.setIssuedAt(NumericDate.fromMilliseconds(issuedAtDate.getTime()));
        claims.setExpirationTime(NumericDate.fromMilliseconds(expiresOnDate.getTime()));

        // Jwts.builder().setIssuer(issuer)
        // .setIssuedAt(issuedAtDate)
        // .setExpiration(new Date(expiresOnDate))
        // .setSubject(userId.getShortId()).claims.setClaim("sId", scopeId.getShortId());
        claims.setSubject(userId.toCompactId());
        claims.setClaim("sId", scopeId.toCompactId());

        String jwt = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            CertificateService certificateService = locator.getService(CertificateService.class);
            CertificateFactory certificateFactory = locator.getFactory(CertificateFactory.class);
            CertificateQuery certificateQuery = certificateFactory.newQuery(scopeId);
            certificateQuery.setPredicate(new AndPredicate()
                    .and(new AttributePredicate<>(CertificatePredicates.USAGE_NAME, "JWT"))
                    .and(new AttributePredicate<>(CertificatePredicates.STATUS, CertificateStatus.VALID)));
            certificateQuery.setLimit(1);

            Certificate certificate = KapuaSecurityUtils.doPrivileged(() -> certificateService.query(certificateQuery)).getItem(0);

            JsonWebSignature jws = new JsonWebSignature();
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            jws.setPayload(claims.toJson());
            jws.setKey(CertificateUtils.stringToPrivateKey(certificate.getPrivateKey(), certificate.getPassword()));
            jwt = jws.getCompactSerialization();
        } catch (JoseException | KapuaException e) {
            KapuaRuntimeException.internalError(e);
        }
        return jwt;
    }

}
