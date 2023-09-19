package de.hsos.studcar.shared.users;

import org.json.JSONArray;
import org.json.JSONObject;

public class User {
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String username;

    public static User getUserFromJson(String json) {
        JSONObject userObject = new JSONObject(json);
        User user = new User();

        user.firstName = userObject.optString("firstName");
        user.lastName = userObject.optString("lastName");
        user.id = userObject.optString("id");
        user.email = userObject.optString("email");
        user.username = userObject.optString("username");
        JSONObject attributes = userObject.optJSONObject("attributes");
        if (attributes != null) {
            JSONArray phoneNumber = attributes.optJSONArray("phoneNumber");
            if (phoneNumber != null) {
                user.phoneNumber = phoneNumber.optString(0);
            }
        }

        return user;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", phoneNumber=" + phoneNumber + ", username=" + username + "]";
    }

}
