package br.com.iandev.midiaindoor.core.integration.bdo;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;

import br.com.iandev.midiaindoor.core.integration.LicencerException;
import java.io.IOException;

public class Licensor extends Authenticator {
    private URL getURLForRegistry() throws IOException {
        return this.getURL(String.format("http://%s/bdoserver2.7/CntServlet", super.getMainServerFQDN()), super.getBaseQuery("MIDIAINDOOR-servidor-registro"));
    }

    public JSONObject doLicensing() throws LicencerException {
        try {
            OutputStream bous = new ByteArrayOutputStream();
            super.doRequest(bous, getURLForRegistry());
            return new JSONObject(bous.toString());
        } catch (Exception ex) {
            throw new LicencerException(ex.getMessage());
        }
    }
}