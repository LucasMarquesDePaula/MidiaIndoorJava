package br.com.iandev.midiaindoor.facede;

import java.util.List;

import br.com.iandev.midiaindoor.dao.Dao;
import br.com.iandev.midiaindoor.model.Model;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

public abstract class CrudFacede<T extends Model> {

    private final Logger logger = Logger.getLogger(this.getClass());
    private final Dao<T> dao;
    private Transaction transaction;
    private boolean mustFinalizeTransaction = true;

    public CrudFacede(Dao<T> dao) {
        this.dao = dao;
    }

    public CrudFacede(Dao<T> dao, Transaction transaction) {
        this.dao = dao;
        this.transaction = transaction;
        this.mustFinalizeTransaction = false;
    }

    protected void beforeSet(T model, Dao<T> dao, Transaction transaction) throws ValidationException {
    }

    protected void beforeSave(T model, Dao<T> dao, Transaction transaction) throws ValidationException {
    }

    protected void beforeDelete(T model, Dao<T> dao, Transaction transaction) throws IllegalOperationException {
    }

    public synchronized void set(T model) throws ValidationException, UnsuccessfulException {
        try {
            if (transaction == null) {
                transaction = dao.beginTransaction();
            }
            beforeSet(model, dao, transaction);
            beforeDelete(model, dao, transaction);
//            dao.save(model);
            dao.delete();
            dao.save(model);
            if (mustFinalizeTransaction) {
                dao.commit();
            }
        } catch (ValidationException ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw ex;
        } catch (Exception ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw new UnsuccessfulException(ex.getMessage());
        } finally {
            if (mustFinalizeTransaction) {
                transaction = null;
            }
        }
    }

    public synchronized T get() throws NotFoundException {
        try {
            T model = list().get(0);
            return model;
        } catch (Exception ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public synchronized T get(T model) throws NotFoundException {
        T found = dao.findById(model.getId());
        if (found == null) {
            throw new NotFoundException("It wasn't found");
        }
        return found;
    }

    public synchronized List<T> list() {
        return dao.list();
    }

    public synchronized void save(T model) throws UnsuccessfulException, ValidationException {
        try {
            if (transaction == null) {
                transaction = dao.beginTransaction();
            }
            beforeSave(model, dao, transaction);
            dao.save(model);
            if (mustFinalizeTransaction) {
                dao.commit();
            }
        } catch (ValidationException ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw ex;
        } catch (Exception ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw new UnsuccessfulException(ex.getMessage());
        } finally {
            if (mustFinalizeTransaction) {
                transaction = null;
            }
        }
    }

    public synchronized void save(List<T> modelList) throws UnsuccessfulException, ValidationException {
        try {
            if (transaction == null) {
                transaction = dao.beginTransaction();
            }
            for (T model : modelList) {
                beforeSave(model, dao, transaction);
                dao.save(model);
            }
            if (mustFinalizeTransaction) {
                dao.commit();
            }
        } catch (ValidationException ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw ex;
        } catch (Exception ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw new UnsuccessfulException(ex.getMessage());
        } finally {
            if (mustFinalizeTransaction) {
                transaction = null;
            }
        }
    }

    public synchronized void delete(T model) throws UnsuccessfulException, IllegalOperationException {
        try {
            if (transaction == null) {
                transaction = dao.beginTransaction();
            }
            beforeDelete(model, dao, transaction);
            dao.delete(model);
            if (mustFinalizeTransaction) {
                dao.commit();
            }
        } catch (IllegalOperationException ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw ex;
        } catch (Exception ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw new UnsuccessfulException(ex.getMessage());
        } finally {
            if (mustFinalizeTransaction) {
                transaction = null;
            }
        }
    }

    public synchronized void delete(List<T> modelList) throws UnsuccessfulException, IllegalOperationException {
        try {
            if (transaction == null) {
                transaction = dao.beginTransaction();
            }
            for (T model : modelList) {
                beforeDelete(model, dao, transaction);
                dao.delete(model);
            }
            dao.commit();
        } catch (IllegalOperationException ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw ex;
        } catch (Exception ex) {
            logger.error("", ex);
            try {
                if (mustFinalizeTransaction) {
                    dao.rollback();
                }
            } catch (Exception ignored) {
            }
            throw new UnsuccessfulException(ex.getMessage());
        } finally {
            if (mustFinalizeTransaction) {
                transaction = null;
            }
        }
    }

    public synchronized void delete() throws UnsuccessfulException, IllegalOperationException {
        delete(list());
    }
}
