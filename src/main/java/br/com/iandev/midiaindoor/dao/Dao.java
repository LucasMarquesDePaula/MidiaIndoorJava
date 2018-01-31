package br.com.iandev.midiaindoor.dao;

import br.com.iandev.midiaindoor.model.Model;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class Dao<E extends Model> {

    private volatile static SessionFactory SESSION_FACTORY = HibernateUtil.getSessionFactory();
    private volatile static Session session;

    private volatile Class<E> clazz;

    public Dao(Class<E> clazz) {
        this.clazz = clazz;
    }

    public synchronized static Session getSession() {
        if (session == null) {
            session = SESSION_FACTORY.openSession();
        }
        return session;
    }

    public synchronized Transaction beginTransaction() {
        return getSession().beginTransaction();
    }

    public synchronized void commit() {
        getSession().getTransaction().commit();
//        session = null;
    }

    public synchronized void rollback() {
        getSession().getTransaction().rollback();
//        session = null;
    }

    public synchronized E findById(Serializable id) {
        return (E) getSession().get(this.clazz, id);
    }

    public synchronized void save(E entity) {
        Serializable id = entity.getId();
        if (id != null) {
            E old = this.findById(id);
            if (old != null) {                
                getSession().merge(entity);
                return;
            }
        }
        getSession().save(entity);
    }

    public synchronized List<E> save(List<E> entityList) {
        entityList.forEach((entity) -> {
            this.save(entity);
        });
        return entityList;
    }

    public synchronized void delete(E entity) {
        getSession().delete(entity);
    }

    public synchronized void delete(List<E> entityList) {
        entityList.forEach((entity) -> {
            this.delete(entity);
        });
    }

    public synchronized void delete() {
        list().forEach((entity) -> {
            getSession().delete(entity);
        });
    }

    public synchronized List<E> list() {
        return getSession().createCriteria(this.clazz).list();
    }

    public synchronized Criteria createCriteria() {
        return getSession().createCriteria(this.clazz);
    }

    public synchronized void clear() {
        getSession().clear();
    }

    public synchronized void flush() {
        getSession().flush();
    }

}
