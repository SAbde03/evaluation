package ma.projet.util;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ma.projet.config.AppConfig;

/**
 * HibernateUtil - Provides access to the SessionFactory
 * This implementation delegates to the Spring-managed SessionFactory
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    /**
     * Get the SessionFactory from Spring context
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Initialize Spring context if not already done
                ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
                sessionFactory = context.getBean(SessionFactory.class);
            } catch (Exception ex) {
                System.err.println("SessionFactory retrieval failed: " + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }
}
