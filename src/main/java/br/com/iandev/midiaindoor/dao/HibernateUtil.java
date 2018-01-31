package br.com.iandev.midiaindoor.dao;

import br.com.iandev.midiaindoor.core.Files;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            
            configuration.setProperty("hibernate.connection.url", String.format("jdbc:hsqldb:file:%s/database;create=true", Files.DIR_DB.getAbsolutePath()));
            configuration.configure();
            
            return configuration.buildSessionFactory();
        } catch (HibernateException ex) {
            Logger.getLogger(HibernateUtil.class).error("", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

}
