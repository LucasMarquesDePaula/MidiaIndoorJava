package br.com.iandev.midiaindoor.task;

import br.com.iandev.midiaindoor.core.integration.bdo.BDOIntegrator;
import br.com.iandev.midiaindoor.settings.LicenseSettings;

public abstract class IntegrationTask<T> extends Task<T> {

    private final LicenseSettings licenseSettings = new LicenseSettings();

    protected void setUp() {
        BDOIntegrator.setIdDispositivo(getLicenseSettings().getDeviceId());
        BDOIntegrator.setDispositivo(getLicenseSettings().getDeviceDescription());
        BDOIntegrator.setCodigo(getLicenseSettings().getCode());
        BDOIntegrator.setIdPessoa(getLicenseSettings().getOwnerId());

        BDOIntegrator.setSiteFQDN(getLicenseSettings().getURL());
        BDOIntegrator.setSenha(getLicenseSettings().getPassword());
        BDOIntegrator.setVersao(getLicenseSettings().getVersion());
    }

    @Override
    public void error(Exception ex) {
        try {
            BDOIntegrator.error();
        } catch (Exception ignored) {
        }
    }

    public LicenseSettings getLicenseSettings() {
        return licenseSettings;
    }

}
