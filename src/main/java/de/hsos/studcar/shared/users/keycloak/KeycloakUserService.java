package de.hsos.studcar.shared.users.keycloak;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONObject;

import de.hsos.studcar.shared.users.User;
import de.hsos.studcar.shared.users.UserService;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

@ApplicationScoped
public class KeycloakUserService implements UserService {

    @ConfigProperty(name = "quarkus.oidc.token-uri", defaultValue = "http://localhost:59258/realms/quarkus/protocol/openid-connect/token")
    String tokenUri;
    @ConfigProperty(name = "quarkus.oidc.admin-uri", defaultValue = "http://localhost:59258/admin/realms/quarkus")
    String adminUri;
    @ConfigProperty(name = "quarkus.oidc.client-id", defaultValue = "backend-service")
    String clientId;
    @ConfigProperty(name = "quarkus.oidc.credentials.secret", defaultValue = "secret")
    String clientSecret;
    @ConfigProperty(name = "quarkus.oidc.credentials.username", defaultValue = "admin")
    String username;
    @ConfigProperty(name = "quarkus.oidc.credentials.password", defaultValue = "admin")
    String password;

    @Override
    public User getUser(String userId) {
        Client client = null;

        try {
            client = ClientBuilder.newClient();

            String accessToken = getAccessToken();

            String userString = client.target(adminUri + "/users/" + userId)
                    .request()
                    .header("Authorization", "Bearer " + accessToken)
                    .get(String.class);

            User user = User.getUserFromJson(userString);

            client.close();
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            client.close();
        }
    }

    @Override
    public String getUserName(String userId) {
        User user = getUser(userId);
        return user.firstName + " " + user.lastName;
    }

    private String getAccessToken() {
        Client client = null;
        try {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("grant_type", "password");
            formData.add("username", username);
            formData.add("password", password);

            String form = encodeFormData(formData);

            client = ClientBuilder.newClient();

            String response = client
                    .target(tokenUri)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

            JSONObject jsonObject = new JSONObject(response);
            String accessToken = jsonObject.get("access_token").toString();
            client.close();
            return accessToken;
        } finally {
            client.close();
        }
    }

    // chat GPT
    private String encodeFormData(MultivaluedMap<String, String> formData) {
        StringBuilder encodedForm = new StringBuilder();

        try {
            for (String key : formData.keySet()) {
                String value = formData.getFirst(key);
                if (value != null) {
                    if (encodedForm.length() > 0) {
                        encodedForm.append("&");
                    }
                    encodedForm.append(URLEncoder.encode(key, StandardCharsets.UTF_8.name()))
                            .append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedForm.toString();
    }
}
