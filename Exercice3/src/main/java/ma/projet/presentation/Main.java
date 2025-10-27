package ma.projet.presentation;

import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // Create and configure the Spring context using HibernateUtil configuration
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateUtil.class);

        System.out.println("Application démarrée...");

        try {
            // Get the Application bean and run it
            App app = context.getBean(App.class);
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

