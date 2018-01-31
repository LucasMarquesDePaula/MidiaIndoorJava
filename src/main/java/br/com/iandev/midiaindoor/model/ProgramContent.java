package br.com.iandev.midiaindoor.model;

import org.json.JSONObject;

import br.com.iandev.midiaindoor.util.JSONUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.log4j.Logger;

@Entity
@Table(name = "program_content")
public class ProgramContent extends Model<ProgramContent, Long> {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", foreignKey = @javax.persistence.ForeignKey(value = javax.persistence.ConstraintMode.NO_CONSTRAINT))
    private Content content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", foreignKey = @javax.persistence.ForeignKey(value = javax.persistence.ConstraintMode.NO_CONSTRAINT))
    private Program program;
    @Column(name = "sequence")
    private Integer sequence;

    public ProgramContent() {
    }

    public ProgramContent(Long id) {
        setId(id);
    }

    @Override
    public ProgramContent parse(JSONObject jsonObject) {
        ProgramContent model = new ProgramContent(null);
        try {
            model.setId(JSONUtil.getLong(jsonObject, "id"));
            model.setContent(new Content(JSONUtil.getLong(jsonObject, "content")));
            model.setProgram(new Program(JSONUtil.getLong(jsonObject, "program")));
            model.setSequence(JSONUtil.getInteger(jsonObject, "sequence"));

            return model;
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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getContent() != null ? this.getContent().toString() : "", this.getProgram() != null ? this.getContent().toString() : "");
    }

}
