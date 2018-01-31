package br.com.iandev.midiaindoor.model;

import org.json.JSONObject;

import br.com.iandev.midiaindoor.util.JSONUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.log4j.Logger;

@Entity
@Table(name = "program")
public class Program extends Model<Program, Long> {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private Character status;

    public Program() {
    }

    public Program(Long id) {
        setId(id);
    }

    @Override
    public Program parse(JSONObject jsonObject) {
        Program model = new Program(null);
        try {
            model.setId(JSONUtil.getLong(jsonObject, "id"));
            model.setDescription(JSONUtil.getString(jsonObject, "description"));
            model.setStatus(JSONUtil.getCharacter(jsonObject, "status"));
        } catch (Exception ex) {
            Logger.getLogger(Program.class).error("", ex);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", this.getId(), this.getDescription());
    }
}
