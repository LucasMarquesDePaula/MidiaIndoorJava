package br.com.iandev.midiaindoor.facede;

import br.com.iandev.midiaindoor.dao.ContentDao;
import br.com.iandev.midiaindoor.dao.Dao;

import br.com.iandev.midiaindoor.model.Content;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 * Created by Lucas on 31/03/2017. Changes: Date Responsible Change 31/03/2017
 * Lucas
 */
public class ContentFacede extends CrudFacede<Content> {

    public ContentFacede() {
        super(new ContentDao());
    }

    public ContentFacede(Transaction transaction) {
        super(new ContentDao(), transaction);
    }

    @Override
    protected void beforeSave(Content model, Dao<Content> dao, Transaction transaction) throws ValidationException {
        Long id = model.getId();
        if (id == null) {
            return;
        }
        try {
            Content content = get(model);

            if (model.getLastUpdateDate() == null) {
                model.setLastUpdateDate(content.getLastUpdateDate());
            }

            if (model.getLastUpdateAttemptDate() == null) {
                model.setLastUpdateAttemptDate(content.getLastUpdateAttemptDate());
            }

            if (model.getHash() == null) {
                model.setHash(content.getHash());
            }

            if (model.getIndexFilePath() == null || "".equals(content.getIndexFilePath())) {
                model.setIndexFilePath(content.getIndexFilePath());
            }

            if (model.getDurationInterval() == null || content.getDurationInterval() == -1) {
                model.setDurationInterval(content.getDurationInterval());
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void beforeDelete(Content model, Dao<Content> dao, Transaction transaction1) throws IllegalOperationException {
        try {
            model.clearFiles();
        } catch (Exception ex) {
            Logger.getLogger(ContentFacede.class).error("", ex);
        }
    }
}
