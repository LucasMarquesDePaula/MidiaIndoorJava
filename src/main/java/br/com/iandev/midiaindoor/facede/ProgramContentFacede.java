package br.com.iandev.midiaindoor.facede;

import br.com.iandev.midiaindoor.dao.ProgramContentDao;
import br.com.iandev.midiaindoor.model.ProgramContent;

public class ProgramContentFacede extends CrudFacede<ProgramContent> {

    public ProgramContentFacede() {
        super(new ProgramContentDao());
    }
}
