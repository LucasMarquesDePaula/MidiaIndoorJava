package br.com.iandev.midiaindoor.core.integration.bdo;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import br.com.iandev.midiaindoor.core.integration.SynchronizerException;
import java.io.IOException;

public class Synchronizer extends Authenticator {

    private URL getURLForSync() throws IOException {
        return super.getURL(String.format("http://%s/bdoserver2.7/CntServlet", Authenticator.getMainServerFQDN()), super.getBaseQuery("MIDIAINDOOR-servidor-programacao"));
    }

    public JSONObject doSync() throws SynchronizerException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Authenticator.doRequest(baos, getURLForSync());
            return new JSONObject(baos.toString("UTF-8"));
        } catch (Exception ex) {
            throw new SynchronizerException(ex.getMessage());
        }
    }
}
