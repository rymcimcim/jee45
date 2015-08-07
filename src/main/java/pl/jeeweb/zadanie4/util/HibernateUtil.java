package pl.jeeweb.zadanie4.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import pl.jeeweb.zadanie4.entity.Pacjent;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    
    static {
        try {
            Configuration configuration = new Configuration().addPackage("pl.jeeweb.zadanie4.entity").addAnnotatedClass(Pacjent.class);
            configuration.configure();
            serviceRegistry = (ServiceRegistry) new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory((ServiceRegistry) serviceRegistry);
            /*factory = new Configuration().addPackage("pl.jeeweb.zadanie23.entity").addAnnotatedClass(User.class).configure().
             /*configure().
             addPackage("pl.jeeweb.zadanie23.entity").
             addAnnotatedClass(User.class).
             buildSessionFactory();*/
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        /*try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }*/
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
