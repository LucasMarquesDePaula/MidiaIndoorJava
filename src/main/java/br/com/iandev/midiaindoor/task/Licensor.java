package br.com.iandev.midiaindoor.task;

import org.json.JSONObject;

import br.com.iandev.midiaindoor.core.App;
import br.com.iandev.midiaindoor.core.TimeCounter;
import br.com.iandev.midiaindoor.facede.ChannelFacede;
import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.model.Person;
import br.com.iandev.midiaindoor.util.JSONUtil;
import br.com.iandev.midiaindoor.facede.DeviceFacede;
import br.com.iandev.midiaindoor.facede.NotFoundException;
import br.com.iandev.midiaindoor.facede.PersonFacede;
import br.com.iandev.midiaindoor.settings.LicenseSettings;

import org.apache.log4j.Logger;

public class Licensor extends IntegrationTask<LicenseSettings> {

    private static final Logger LOGGER = Logger.getLogger(Licensor.class);

    @Override
    public final LicenseSettings execute() throws Exception {
        super.setUp();

        br.com.iandev.midiaindoor.core.integration.bdo.Licensor licensor = new br.com.iandev.midiaindoor.core.integration.bdo.Licensor();

        JSONObject jsonObject = licensor.doLicensing();

        App.getInstance().setTimeCounter(new TimeCounter(JSONUtil.getDate(jsonObject, "date")));

        Person person = new Person().parse(jsonObject.getJSONObject("person"));
        Device device = new Device().parse(jsonObject.getJSONObject("device"));

        LicenseSettings licenseSettings = getLicenseSettings();
        licenseSettings.setOwnerID(person.getId().toString());
        licenseSettings.setDeviceId(device.getId().toString());

        PersonFacede personController = new PersonFacede();
        DeviceFacede deviceController = new DeviceFacede();

        personController.set(person);

        device.setPerson(person);
        try {
            device.setChannel(new ChannelFacede().get(device.getChannel()));
        } catch (Exception ignored) {
            device.setChannel(null);
        }
        deviceController.set(device);

        return licenseSettings;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }
}
