export default {
    oauth: {
        "currentIdentityProvider": "custom1",
        "customIdentityProviders": {
            "custom1": {
                "name": "oauth2",
                "clientId": "console",
                "redirectUri": window.location.origin,
                "authorizationEndpoint": "http://localhost:9090/auth/realms/master/protocol/openid-connect/auth",
                "url": window.location.origin + "/oauth/authenticate",
                "responseType": ["code"],
                "nonce": "abcdefg",
                "requiredUrlParams": ["nonce"]
            }
        }
    },
    login: {
        mode: "sso"
    }
};