package tbs.utils.AOP.authorize.error;

public class AuthorizationFailureException extends Exception {
    public AuthorizationFailureException(String message) {
        super(message);
    }
}
