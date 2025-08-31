package co.com.crediya.securityports;

public interface JwtPort {
    String generateToken(String email, String role);
    boolean validateToken(String token);
}
