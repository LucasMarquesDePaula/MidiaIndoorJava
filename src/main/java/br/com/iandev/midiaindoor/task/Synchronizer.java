package br.com.iandev.midiaindoor.task;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import br.com.iandev.midiaindoor.core.App;
import br.com.iandev.midiaindoor.core.TimeCounter;
import br.com.iandev.midiaindoor.model.Channel;
import br.com.iandev.midiaindoor.model.Content;
import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.model.Person;
import br.com.iandev.midiaindoor.model.Program;
import br.com.iandev.midiaindoor.model.ProgramContent;
import br.com.iandev.midiaindoor.model.Programming;
import br.com.iandev.midiaindoor.util.JSONUtil;
import br.com.iandev.midiaindoor.facede.ChannelFacede;
import br.com.iandev.midiaindoor.facede.ContentFacede;
import br.com.iandev.midiaindoor.facede.DeviceFacede;
import br.com.iandev.midiaindoor.facede.NotFoundException;
import br.com.iandev.midiaindoor.facede.PersonFacede;
import br.com.iandev.midiaindoor.facede.ProgramContentFacede;
import br.com.iandev.midiaindoor.facede.ProgramFacede;
import br.com.iandev.midiaindoor.facede.ProgrammingFacede;
import br.com.iandev.midiaindoor.settings.LicenseSettings;
import java.util.Iterator;
import org.apache.log4j.Logger;

public class Synchronizer extends IntegrationTask<Device> {

    private final LicenseSettings licenseSettings;
    private final DeviceFacede deviceFacede;
    private final PersonFacede personFacede;
    private final ContentFacede contentFacede;
    private final ChannelFacede channelFacede;
    private final ProgrammingFacede programmingFacede;
    private final ProgramFacede programFacede;
    private final ProgramContentFacede programContentFacede;

    private static final Logger LOGGER = Logger.getLogger(Synchronizer.class);

    public Synchronizer() {
        this.licenseSettings = new LicenseSettings();
        this.deviceFacede = new DeviceFacede();
        this.personFacede = new PersonFacede();
        this.contentFacede = new ContentFacede();
        this.channelFacede = new ChannelFacede();
        this.programmingFacede = new ProgrammingFacede();
        this.programFacede = new ProgramFacede();
        this.programContentFacede = new ProgramContentFacede();
    }

    @Override
    public final Device execute() throws Exception {
        super.setUp();
        App app = App.getInstance();

        try {
            Device device = deviceFacede.get();
            device.setLastUpdateAttemptDate(app.getTimeCounter().getDate());
            deviceFacede.set(device);
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }

        br.com.iandev.midiaindoor.core.integration.bdo.Synchronizer synchronizer = new br.com.iandev.midiaindoor.core.integration.bdo.Synchronizer();
        JSONObject jsonObject = synchronizer.doSync();

        Date date = JSONUtil.getDate(jsonObject, "date");

        app.setTimeCounter(new TimeCounter(date));

        programmingFacede.delete();
        programContentFacede.delete();
        programFacede.delete();
//        channelFacede.delete();

        Channel channel = null;
        try {
            channel = new Channel().parseList(jsonObject.getJSONArray("channel")).get(0);
            channelFacede.set(channel);
            channel = channelFacede.get();
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }

        for (Program program : new Program().parseList(jsonObject.getJSONArray("program"))) {
            try {
                programFacede.save(program);
            } catch (Exception ex) {
                LOGGER.error("", ex);
            }
        }

        List<Content> contentList = new Content().parseList(jsonObject.getJSONArray("content"));
        for (Content entity : contentFacede.list()) {
            boolean found = false;
            Iterator<Content> iterator = contentList.iterator();
            while (iterator.hasNext()) {
                Content content = iterator.next();
                if (entity.getId().equals(content.getId())) {
                    found = true;
                    contentFacede.save(content);
                    iterator.remove();
                    break;
                }
            }
            if (!found) {
                contentFacede.delete(entity);
            }
        }
        contentFacede.save(contentList);

        for (Programming programming : new Programming().parseList(jsonObject.getJSONArray("programming"))) {
            try {
                Channel c = channelFacede.get(programming.getChannel());
                Program p = programFacede.get(programming.getProgram());
                programming.setChannel(c);
                programming.setProgram(p);
                programmingFacede.save(programming);
            } catch (Exception ex) {
                LOGGER.error("", ex);
            }
        }

        for (ProgramContent programContent : new ProgramContent().parseList(jsonObject.getJSONArray("programContent"))) {
            try {
                programContent.setProgram(programFacede.get(programContent.getProgram()));
                programContent.setContent(contentFacede.get(programContent.getContent()));
                programContentFacede.save(programContent);
            } catch (Exception ex) {
                LOGGER.error("", ex);
            }
        }

        Person person = null;
        try {
            person = new Person().parse(jsonObject.getJSONObject("person"));
            personFacede.set(person);
            person = personFacede.get();
        } catch (Exception ex) {
            LOGGER.error("", ex);
        }

        Device device = new Device().parse(jsonObject.getJSONObject("device"));
        device.setChannel(channel);
        device.setPerson(person);
        device.setLastUpdateDate(app.getTimeCounter().getDate());

        deviceFacede.set(device);
        licenseSettings.setDeviceDescription(device.getDescription());

        return device;

    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }
}
