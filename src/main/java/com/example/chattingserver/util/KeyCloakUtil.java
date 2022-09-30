package com.example.chattingserver.util;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class KeyCloakUtil {

    public String getUserId(HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) token.getPrincipal();
        KeycloakSecurityContext securityContext = principal.getKeycloakSecurityContext();
        AccessToken accessToken = securityContext.getToken();
        return accessToken.getSubject();
    }

    public String getUserEmail(HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) token.getPrincipal();
        KeycloakSecurityContext securityContext = principal.getKeycloakSecurityContext();
        AccessToken accessToken = securityContext.getToken();
        return accessToken.getEmail();
    }

    public String getUserEmail(Principal principal) {
        KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) principal;
        KeycloakSecurityContext securityContext = keycloakPrincipal.getKeycloakSecurityContext();
        AccessToken accessToken = securityContext.getToken();
        return accessToken.getEmail();
    }
}
