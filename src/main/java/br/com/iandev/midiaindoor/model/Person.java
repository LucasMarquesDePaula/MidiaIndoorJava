package br.com.iandev.midiaindoor.model;

import org.json.JSONObject;

import br.com.iandev.midiaindoor.util.JSONUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.log4j.Logger;

@Entity
@Table(name = "person")
public class Person extends Model<Person, Long> {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "alias")
    private String alias;
    @Column(name = "status")
    private Character status;

    public Person() {
    }

    public Person(Long id) {
        setId(id);
    }

    @Override
    public Person parse(JSONObject jsonObject) {
        Person model = new Person(null);
        try {
            model.setId(JSONUtil.getLong(jsonObject, "id"));
            model.setName(JSONUtil.getString(jsonObject, "name"));
            model.setAlias(JSONUtil.getString(jsonObject, "alias"));
            model.setStatus(JSONUtil.getCharacter(jsonObject, "status"));

        } catch (Exception ex) {
            Logger.getLogger(ProgramContent.class).error("", ex);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", getId(), getName());
    }

}
