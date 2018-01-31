package br.com.iandev.midiaindoor.facede;

import br.com.iandev.midiaindoor.dao.ProgramDao;
import br.com.iandev.midiaindoor.model.Program;
import org.hibernate.Transaction;

public class ProgramFacede extends CrudFacede<Program> {

    public ProgramFacede() {
        super(new ProgramDao());
    }

    public ProgramFacede(Transaction transaction) {
        super(new ProgramDao(), transaction);
    }
}
