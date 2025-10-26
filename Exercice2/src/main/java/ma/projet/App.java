package ma.projet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ma.projet.classes.Employe;
import ma.projet.config.AppConfig;
import ma.projet.service.EmployeService;

import java.util.List;

/**
 * Main Application
 */
public class App 
{
    public static void main( String[] args )
    {
        // Initialize Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get service beans
        EmployeService employeService = context.getBean(EmployeService.class);

        // Example usage
        System.out.println("Listing all employees:");
        List<Employe> employes = employeService.findAll();
        if (employes != null) {
            for (Employe e : employes) {
                System.out.println("Employee: " + e.getNom() + " " + e.getPrenom());
            }
        } else {
            System.out.println("No employees found.");
        }

        // Close the context
        context.close();
    }
}
