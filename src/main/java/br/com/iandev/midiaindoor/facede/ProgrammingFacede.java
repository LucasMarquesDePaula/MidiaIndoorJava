package br.com.iandev.midiaindoor.facede;

import br.com.iandev.midiaindoor.dao.ProgrammingDao;
import br.com.iandev.midiaindoor.model.Programming;
import org.hibernate.Transaction;

public class ProgrammingFacede extends CrudFacede<Programming> {

    public ProgrammingFacede() {
        super(new ProgrammingDao());
    }

    public ProgrammingFacede(Transaction transaction) {
        super(new ProgrammingDao(), transaction);
    }
}
