package br.com.iandev.midiaindoor.core;

import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.facede.DeviceFacede;
import br.com.iandev.midiaindoor.settings.LicenseSettings;

public class License {
    private final LicenseSettings licenseSettings;

    public License() {
        this.licenseSettings = new LicenseSettings();
    }

    public License(LicenseSettings licenseSettings) {
        this.licenseSettings = licenseSettings;
    }

    public boolean isLicenceValid() {
        boolean isValid = false;
        try {
            Device device = new DeviceFacede().get();
            isValid = device.getId().equals(Long.valueOf(getLicenseSettings().getDeviceId()));
        } catch (Exception ignored) {
        }
        return isValid;
    }

    private LicenseSettings getLicenseSettings() {
        return licenseSettings;
    }
}
