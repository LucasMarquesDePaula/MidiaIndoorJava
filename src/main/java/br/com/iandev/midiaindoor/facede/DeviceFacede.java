package br.com.iandev.midiaindoor.facede;

import br.com.iandev.midiaindoor.dao.Dao;
import br.com.iandev.midiaindoor.dao.DeviceDao;

import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.settings.LicenseSettings;
import org.hibernate.Transaction;

public final class DeviceFacede extends CrudFacede<Device> {

    public DeviceFacede() {
        super(new DeviceDao());
    }

    public DeviceFacede(Transaction transaction) {
        super(new DeviceDao(), transaction);
    }

    @Override
    protected void beforeSet(Device model, Dao<Device> dao, Transaction transaction) throws ValidationException {
        LicenseSettings licenseSettings = new LicenseSettings();
        Long deviceId = Long.parseLong(licenseSettings.getDeviceId());

        if (!deviceId.equals(model.getId())) {
            throw new ValidationException();
        }

        model.setCode(licenseSettings.getCode());

        try {
            Device device = super.get();
            if (model.getLastUpdateDate() == null) {
                model.setLastUpdateDate(device.getLastUpdateDate());
            }

            if (model.getLastUpdateAttemptDate() == null) {
                model.setLastUpdateAttemptDate(device.getLastUpdateAttemptDate());
            }

            if (model.getLastAuthenticationDate() == null) {
                model.setLastAuthenticationDate(device.getLastAuthenticationDate());
            }
        } catch (NotFoundException ex) {
        }
    }

}
