package de.hsos.studcar;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.jwt.JsonWebToken;

public class MockToken implements JsonWebToken {

    @Override
    public String getName() {
        return "Alice";
    }

    @Override
    public Set<String> getClaimNames() {
        Set<String> claims = new HashSet<>();
        return claims;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getClaim(String claimName) {
        if (claimName.equals("id")) {
            return (T) "eb4123a3-b722-4798-9af5-8957f823657a";
        } else {
            return null;
        }
    }

}
