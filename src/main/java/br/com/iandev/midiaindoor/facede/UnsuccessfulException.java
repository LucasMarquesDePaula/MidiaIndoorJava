package br.com.iandev.midiaindoor.facede;

import java.util.Map;

public class UnsuccessfulException extends FacedeException {

    public UnsuccessfulException() {
        super();
    } 

    public UnsuccessfulException(String message) {
        super(message);
    }

    public UnsuccessfulException(String message, Map<String, String> messages) {
        super(message, messages);
    }
}
