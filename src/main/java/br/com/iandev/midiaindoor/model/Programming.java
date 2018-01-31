package br.com.iandev.midiaindoor.model;

import org.json.JSONObject;

import java.util.Date;
import java.util.TimeZone;

import br.com.iandev.midiaindoor.util.DateUtil;
import br.com.iandev.midiaindoor.util.IntervalUtil;
import br.com.iandev.midiaindoor.util.JSONUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.jboss.logging.Logger;

@Entity
@Table(name = "programming")
public class Programming extends Model<Programming, Long> {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", foreignKey = @javax.persistence.ForeignKey(value = javax.persistence.ConstraintMode.NO_CONSTRAINT))
    private Channel channel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", foreignKey = @javax.persistence.ForeignKey(value = javax.persistence.ConstraintMode.NO_CONSTRAINT))
    private Program program;
    @Column(name = "days_of_week")
    private String daysOfWeek;
    @Column(name = "start_time")
    private Long startTime;
    @Column(name = "end_time")
    private Long endTime;

    public Programming() {
    }

    public Programming(Long id) {
        setId(id);
    }

    @Override
    public Programming parse(JSONObject jsonObject) {
        Programming model = new Programming(null);
        try {
            model.setId(JSONUtil.getLong(jsonObject, "id"));
            model.setChannel(new Channel(JSONUtil.getLong(jsonObject, "channel")));
            model.setProgram(new Program(JSONUtil.getLong(jsonObject, "program")));
            model.setDaysOfWeek(JSONUtil.getString(jsonObject, "daysOfWeek"));
            model.setStartTime(JSONUtil.getInterval(jsonObject, "startTime"));
            model.setEndTime(JSONUtil.getInterval(jsonObject, "endTime"));
        } catch (Exception ex) {
            Logger.getLogger(Programming.class).error("", ex);
        }
        return model;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public boolean mustPlay(Date date, TimeZone timeZone) {
        Long startTime = this.getStartTime();
        if (startTime == null) {
            startTime = IntervalUtil.parse("00:00:00");
        }

        Long endTime = this.getEndTime();
        if (endTime == null) {
            endTime = IntervalUtil.parse("23:59:59");
        }

        String daysOfWeek = this.getDaysOfWeek();
        if (daysOfWeek == null) {
            daysOfWeek = "1111111";
        }

        long time = IntervalUtil.trunc(date.getTime(), timeZone);
        return time >= startTime && time <= endTime && daysOfWeek.length() == 7 && Character.valueOf('1').equals(daysOfWeek.charAt(DateUtil.getDayOfWeek(date, timeZone) - 1));
    }

    @Override
    public String toString() {
        return String.format("%s - %d -> %d", this.getDaysOfWeek(), this.getStartTime(), this.getEndTime());
    }

}
