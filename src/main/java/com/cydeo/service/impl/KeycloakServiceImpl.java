package com.cydeo.service.impl;

import com.cydeo.config.KeycloakProperties;
import com.cydeo.exception.UserNotFoundException;
import com.cydeo.service.KeycloakService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakProperties keycloakProperties;

    public KeycloakServiceImpl(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public String getAccessToken() {
        KeycloakAuthenticationToken keycloakAuthenticationToken = getAuthentication();
        if (keycloakAuthenticationToken == null) {
            throw new IllegalStateException("SecurityContext is empty. No authentication token available.");
        }
        if (keycloakAuthenticationToken.getAccount() == null) {
            throw new IllegalStateException("KeycloakAccount is null. Authentication token is invalid.");
        }
        if (keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext() == null) {
            throw new IllegalStateException("KeycloakSecurityContext is null. Authentication token is invalid.");
        }
        return "Bearer " + keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getTokenString();
    }

    @Override
    public String getUsername() {
        KeycloakAuthenticationToken keycloakAuthenticationToken = getAuthentication();
        if (keycloakAuthenticationToken == null) {
            throw new IllegalStateException("SecurityContext is empty. No authentication token available.");
        }
        SimpleKeycloakAccount account = (SimpleKeycloakAccount) keycloakAuthenticationToken.getAccount();
        if (account == null || account.getKeycloakSecurityContext() == null) {
            throw new IllegalStateException("KeycloakAccount or KeycloakSecurityContext is null. Authentication token is invalid.");
        }
        return account.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    @Override
    public List<String> getUserClientRoles(String username) {

        // For the currently logged-in user, read roles directly from the JWT token.
        // This avoids calling the Keycloak Admin API (which requires master credentials).
        try {
            String loggedInUsername = getUsername();
            if (loggedInUsername != null && loggedInUsername.equals(username)) {
                SimpleKeycloakAccount account = (SimpleKeycloakAccount) getAuthentication().getAccount();
                org.keycloak.representations.AccessToken token = account.getKeycloakSecurityContext().getToken();
                org.keycloak.representations.AccessToken.Access access = token.getResourceAccess(keycloakProperties.getClientId());
                if (access != null) {
                    java.util.Set<String> roles = access.getRoles();
                    return roles != null ? new java.util.ArrayList<>(roles) : java.util.Collections.emptyList();
                }
                return java.util.Collections.emptyList();
            }
        } catch (Exception ignored) {
            // Fall through to Admin API if JWT token reading fails
        }

        // Fallback: call Keycloak Admin API for other users
        try (Keycloak keycloak = getKeycloakInstance()) {

            RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
            UsersResource usersResource = realmResource.users();

            List<UserRepresentation> userRepresentations = usersResource.search(username);

            if (userRepresentations.isEmpty()) {
                throw new UserNotFoundException("User does not exist.");
            }

            ClientRepresentation appClient = realmResource.clients()
                    .findByClientId(keycloakProperties.getClientId()).get(0);

            String clientId = appClient.getId();

            UserRepresentation keycloakUser = userRepresentations.get(0);

            List<RoleRepresentation> existingRoles = realmResource.users().get(keycloakUser.getId())
                    .roles().clientLevel(clientId).listEffective();

            return existingRoles.stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toList());

        }

    }

    @Override
    public boolean hasClientRole(String username, String role) {
        List<String> userClientRoles = getUserClientRoles(username);
        return userClientRoles.stream().anyMatch(eachRole -> eachRole.equals(role));
    }

    private KeycloakAuthenticationToken getAuthentication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (!(authentication instanceof KeycloakAuthenticationToken)) {
            return null;
        }
        return (KeycloakAuthenticationToken) authentication;
    }

    private Keycloak getKeycloakInstance() {
        return Keycloak.getInstance(
                keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(),
                keycloakProperties.getMasterUser(),
                keycloakProperties.getMasterUserPswd(),
                keycloakProperties.getMasterClient());
    }

}
