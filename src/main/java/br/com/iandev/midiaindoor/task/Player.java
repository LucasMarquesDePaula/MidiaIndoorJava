package br.com.iandev.midiaindoor.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import br.com.iandev.midiaindoor.core.App;
import br.com.iandev.midiaindoor.core.interfaces.Playable;
import br.com.iandev.midiaindoor.model.Channel;
import br.com.iandev.midiaindoor.model.Content;
import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.model.Program;
import br.com.iandev.midiaindoor.model.ProgramContent;
import br.com.iandev.midiaindoor.model.Programming;
import br.com.iandev.midiaindoor.util.DateUtil;
import br.com.iandev.midiaindoor.facede.ChannelFacede;
import br.com.iandev.midiaindoor.facede.ContentFacede;
import br.com.iandev.midiaindoor.facede.DeviceFacede;
import br.com.iandev.midiaindoor.facede.ProgramContentFacede;
import br.com.iandev.midiaindoor.facede.ProgramFacede;
import br.com.iandev.midiaindoor.facede.ProgrammingFacede;
import org.apache.log4j.Logger;

public class Player extends Task<Playable> {

    private final DeviceFacede deviceFacede;
    private final ChannelFacede channelFacede;
    private final ContentFacede contentFacede;
    private final ProgramFacede programFacede;
    private final ProgrammingFacede programmingFacede;
    private final ProgramContentFacede programContentFacede;

    private static List<Playable> playableList;
    private final Logger LOGGER = Logger.getLogger(Player.class);

    public Player() {
        this.deviceFacede = new DeviceFacede();
        this.channelFacede = new ChannelFacede();
        this.contentFacede = new ContentFacede();
        this.programFacede = new ProgramFacede();
        this.programmingFacede = new ProgrammingFacede();
        this.programContentFacede = new ProgramContentFacede();
    }

    @Override
    public final Playable execute() throws Exception {
        App app = App.getInstance();
        if (playableList == null || playableList.isEmpty()) {
            Device device = deviceFacede.get();
            playableList = getPlayableList(
                    app.getTimeCounter().getDate(),
                    device.getTimeZone(), device,
                    channelFacede.list(),
                    programFacede.list(),
                    programmingFacede.list(),
                    programContentFacede.list(),
                    contentFacede.list()
            );
        }
        Content content = contentFacede.get((Content) playableList.remove(0));
        content.setLastPlaybackDate(app.getTimeCounter().getDate());
        contentFacede.save(content);
        return content;
    }

    public static List<Playable> getPlayableList(Date date, TimeZone timeZone, Device device, List<Channel> channelList, List<Program> programList, List<Programming> programmingList, List<ProgramContent> programContentList, List<Content> contentList) {
        List<Grade> gradeList = new ArrayList<>();

        if (!Character.valueOf('A').equals(device.getStatus())) {
            return null;
        }

        for (Channel channel : channelList) {
            if (!Character.valueOf('A').equals(channel.getStatus())) {
                continue;
            }

            if (!device.getChannel().getId().equals(channel.getId())) {
                continue;
            }

            for (Programming programming : programmingList) {
                if (!programming.getChannel().getId().equals(channel.getId())) {
                    continue;
                }
                if (!programming.mustPlay(date, timeZone)) {
                    continue;
                }
                for (Program program : programList) {
                    if (!program.getId().equals(programming.getProgram().getId())) {
                        continue;
                    }
                    if (!Character.valueOf('A').equals(program.getStatus())) {
                        continue;
                    }

                    for (ProgramContent programContent : programContentList) {
                        if (!programContent.getProgram().getId().equals(program.getId())) {
                            continue;
                        }

                        for (Content content : contentList) {
                            if (!content.getId().equals(programContent.getContent().getId())) {
                                continue;
                            }
                            if (!content.canPlay(DateUtil.trunc(date, timeZone))) {
                                continue;
                            }
                            gradeList.add(new Grade(channel, program, programming, programContent, content));
                        }
                    }
                }
            }
        }

        Collections.sort(gradeList, (Grade o1, Grade o2) -> {
            Long programId1 = o1.getProgram().getId();
            Long programId2 = o2.getProgram().getId();
            if (!programId1.equals(programId2)) {
                return programId1.compareTo(programId2);
            }

            Integer sequence1 = o1.getProgramContent().getSequence();
            Integer sequence2 = o2.getProgramContent().getSequence();
            return sequence1.compareTo(sequence2);
        });

        List<Playable> playableList = new ArrayList<>();
        gradeList.forEach((grade) -> {
            playableList.add(grade.getContent());
        });

        return playableList;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    static private class Grade {

        private final Channel channel;
        private final Programming programming;
        private final Program program;
        private final ProgramContent programContent;
        private final Content content;

        Grade(Channel channel, Program program, Programming programming, ProgramContent programContent, Content content) {
            this.channel = channel;
            this.program = program;
            this.programming = programming;
            this.programContent = programContent;
            this.content = content;
        }

        public Channel getChannel() {
            return channel;
        }

        public Program getProgram() {
            return program;
        }

        public Programming getProgramming() {
            return programming;
        }

        public ProgramContent getProgramContent() {
            return programContent;
        }

        public Content getContent() {
            return content;
        }
    }
}
