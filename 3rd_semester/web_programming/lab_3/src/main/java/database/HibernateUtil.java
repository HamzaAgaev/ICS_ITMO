package database;

import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistry;
import org.hibernate.cfg.AvailableSettings;
//import org.hibernate.cfg.Configuration;

import java.util.Properties;
import beans.Result;

public class HibernateUtil {
    //Annotation based configuration
    private static SessionFactory sessionFactory;

    static {
        try {
            Properties info = new Properties();
            info.load(HibernateUtil.class.getClassLoader().getResourceAsStream("/db.cfg"));
            sessionFactory = new Configuration().configure()
                .setProperty(AvailableSettings.USER,
                    info.getProperty("user"))
                .setProperty(AvailableSettings.PASS,
                    info.getProperty("password"))
                .addAnnotatedClass(Result.class)
                .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Something went wrong during initializing Hibernate: " + ex);
            throw new ExceptionInInitializerError();
        }
    }

//    private static SessionFactory buildSessionFactory() {
//        try {
//            // Create the SessionFactory from hibernate.cfg.xml
//            Configuration configuration = new Configuration();
//            configuration.configure("hibernate.cfg.xml");
//            System.out.println("Hibernate Annotation Configuration loaded");
//
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//            System.out.println("Hibernate Annotation serviceRegistry created");
//
//            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//
//            return sessionFactory;
//        }
//        catch (Throwable ex) {
//            // Make sure you log the exception, as it might be swallowed
//            System.err.println("Initial SessionFactory creation failed. " + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }

    public static SessionFactory getSessionFactory() {
//        if (sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }
}
