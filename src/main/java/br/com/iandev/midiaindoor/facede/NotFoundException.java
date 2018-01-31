package br.com.iandev.midiaindoor.facede;

import java.util.Map;

public class NotFoundException extends FacedeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Map<String, String> messages) {
        super(message, messages);
    }
}
