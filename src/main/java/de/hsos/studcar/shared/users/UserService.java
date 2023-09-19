package de.hsos.studcar.shared.users;

public interface UserService {

    User getUser(String userId);

    String getUserName(String userId);

}