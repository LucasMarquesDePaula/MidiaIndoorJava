package br.com.iandev.midiaindoor.model;

import org.json.JSONObject;

import java.util.Date;
import java.util.TimeZone;

import br.com.iandev.midiaindoor.core.interfaces.UpdatebleException;
import br.com.iandev.midiaindoor.core.interfaces.Updateble;
import br.com.iandev.midiaindoor.util.JSONUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.apache.log4j.Logger;

@Entity
@Table(name = "device")
public class Device extends Model<Device, Long> implements Updateble {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private Character status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", foreignKey = @javax.persistence.ForeignKey(value = javax.persistence.ConstraintMode.NO_CONSTRAINT))
    private Person person;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", foreignKey = @javax.persistence.ForeignKey(value = javax.persistence.ConstraintMode.NO_CONSTRAINT))
    private Channel channel;

    @Column(name = "code")
    private String code;
    @Column(name = "time_zone")
    private TimeZone timeZone;

    @Column(name = "update_interval")
    private Long updateInterval;
    @Column(name = "update_tolerance_interval")
    private Long updateToleranceInterval;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "last_update_attempt_date")
    private Date lastUpdateAttemptDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "last_authentication_date")
    private Date lastAuthenticationDate;

    @Column(name = "token_expiration_interval")
    private Long tokenExpirationInterval;

    @Column(name = "token")
    private String token;

    @Column(name = "ip")
    private String ip;

    public Device() {
    }

    public Device(Long id) {
        setId(id);
    }

    @Override
    public Device parse(JSONObject jsonObject) {
        Device model = new Device();
        try {
            model.setId(JSONUtil.getLong(jsonObject, "id"));
            model.setDescription(JSONUtil.getString(jsonObject, "description"));
            model.setStatus(JSONUtil.getCharacter(jsonObject, "status"));

            model.setPerson(new Person(JSONUtil.getLong(jsonObject, "person")));
            model.setChannel(new Channel(JSONUtil.getLong(jsonObject, "channel")));

            model.setCode(JSONUtil.getString(jsonObject, "code"));
            model.setTimeZone(JSONUtil.getTimeZone(jsonObject, "timeZone"));

            model.setUpdateInterval(JSONUtil.getLong(jsonObject, "updateInterval"));
            model.setUpdateToleranceInterval(JSONUtil.getLong(jsonObject, "updateToleranceInterval"));

            model.setLastUpdateDate(JSONUtil.getDate(jsonObject, "lastUpdateDate"));
            model.setLastUpdateDate(JSONUtil.getDate(jsonObject, "lastUpdateAttemptDate"));

            model.setLastAuthenticationDate(JSONUtil.getDate(jsonObject, "lastAuthenticationDate"));
            model.setTokenExpirationInterval(JSONUtil.getLong(jsonObject, "tokenExpirationInterval"));
            model.setToken(JSONUtil.getString(jsonObject, "token"));
            model.setIp(JSONUtil.getString(jsonObject, "ip"));

        } catch (Exception ex) {
            Logger.getLogger(Device.class).error("", ex);
        }
        return model;
    }

    @Override
    public Date getNextUpdateDate() throws UpdatebleException {

        if (this.getLastUpdateDate() == null) {
            throw new UpdatebleException("lastUpdateDate must not be null");
        }

        Long updateInterval = this.getUpdateInterval();
        if (updateInterval == null || updateInterval < 0L) {
            updateInterval = 0L;
        }

        return new Date(this.getLastUpdateDate().getTime() + updateInterval);
    }

    @Override
    public boolean mustUpdate(Date date) {
        boolean mustUpdate = true;
        try {
            mustUpdate = this.getNextUpdateDate().getTime() <= date.getTime();
        } catch (Exception ex) {
            // Silence is golden
        }
        return mustUpdate;
    }

    @Override
    public boolean outOfDate(Date date) {
        Long updateToleranceInterval = this.getUpdateToleranceInterval();

        if (updateToleranceInterval == null) {
            updateToleranceInterval = 0L;
        }

        boolean outOfDate;
        try {
            outOfDate = this.getNextUpdateDate().getTime() + updateToleranceInterval < date.getTime();
        } catch (Exception ex) {
            return true;
        }

        return outOfDate && updateToleranceInterval != -1L;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public Long getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public void setUpdateInterval(Long updateInterval) {
        this.updateInterval = updateInterval;
    }

    @Override
    public Long getUpdateToleranceInterval() {
        return updateToleranceInterval;
    }

    @Override
    public void setUpdateToleranceInterval(Long updateToleranceInterval) {
        this.updateToleranceInterval = updateToleranceInterval;
    }

    @Override
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public Date getLastUpdateAttemptDate() {
        return lastUpdateAttemptDate;
    }

    @Override
    public void setLastUpdateAttemptDate(Date lastUpdateAttemptDate) {
        this.lastUpdateAttemptDate = lastUpdateAttemptDate;
    }

    public Date getLastAuthenticationDate() {
        return lastAuthenticationDate;
    }

    public void setLastAuthenticationDate(Date lastAuthenticationDate) {
        this.lastAuthenticationDate = lastAuthenticationDate;
    }

    public Long getTokenExpirationInterval() {
        return tokenExpirationInterval;
    }

    public void setTokenExpirationInterval(Long tokenExpirationInterval) {
        this.tokenExpirationInterval = tokenExpirationInterval;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", this.getId(), this.getDescription());
    }

}
