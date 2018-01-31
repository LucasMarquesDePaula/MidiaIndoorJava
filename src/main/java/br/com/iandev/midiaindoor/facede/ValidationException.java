package br.com.iandev.midiaindoor.facede;

import java.util.Map;

/**
 *
 * @author Lucas
 */
public class ValidationException extends FacedeException {
    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Map<String, String> messages) {
        super(message, messages);
    }
}
