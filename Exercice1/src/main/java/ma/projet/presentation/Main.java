package ma.projet.presentation;

import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main class that serves as the entry point for the application.
 * It bootstraps the Spring context and runs the Application class.
 */
public class Main {
    public static void main(String[] args) {
        // Create and configure the Spring context using HibernateUtil configuration
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateUtil.class);

        System.out.println("Application démarrée...");

        try {
            // Get the Application bean and run it
            Application app = context.getBean(Application.class);
            app.run();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution de l'application: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the Spring context
            context.close();
        }
    }
}
