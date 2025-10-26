package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query query;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testCreate() {
        Employe employe = new Employe();
        employe.setNom("Dupont");
        employe.setPrenom("Jean");
        employe.setTelephone("0612345678");

        boolean result = employeService.create(employe);
        assertTrue(result);
        verify(session).save(employe);
    }



    @Test
    public void testDelete() {
        Employe employe = new Employe();
        boolean result = employeService.delete(employe);
        assertTrue(result);
        verify(session).delete(employe);
    }

    @Test
    public void testUpdate() {
        Employe employe = new Employe();
        employe.setNom("Dupont Updated");
        boolean result = employeService.update(employe);
        assertTrue(result);
        verify(session).update(employe);
    }

    @Test
    public void testFindById() {

        int id = 1;
        Employe employe = new Employe();
        employe.setId(id);
        when(session.get(Employe.class, id)).thenReturn(employe);
        Employe result = employeService.findById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(session).get(Employe.class, id);
    }

    @Test
    public void testFindAll() {
        List<Employe> employes = new ArrayList<>();
        employes.add(new Employe());
        employes.add(new Employe());
        when(session.createQuery("from Employe", Employe.class)).thenReturn(query);
        when(query.list()).thenReturn(employes);
        List<Employe> result = employeService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByProjet() {
        int projetId = 1;
        List<Employe> employes = new ArrayList<>();
        employes.add(new Employe());
        when(session.createQuery(
                "select distinct e from Employe e join e.employeTaches et join et.tache t where t.projet.id = :id", 
                Employe.class)).thenReturn(query);
        when(query.setParameter("id", projetId)).thenReturn(query);
        when(query.list()).thenReturn(employes);
        List<Employe> result = employeService.findByProjet(projetId);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAfficherTachesRealisees() {
        int employeId = 1;
        Employe employe = new Employe();
        employe.setId(employeId);
        employe.setNom("Dupont");
        employe.setPrenom("Jean");
        List<EmployeTache> employeTaches = new ArrayList<>();
        EmployeTache employeTache = new EmployeTache();
        Tache tache = new Tache();
        tache.setId(1);
        tache.setNom("Tache test");
        employeTache.setTache(tache);
        employeTache.setDateDebutReelle(new Date());
        employeTache.setDateFinReelle(new Date());
        employeTaches.add(employeTache);
        when(session.get(Employe.class, employeId)).thenReturn(employe);
        when(session.createQuery("from EmployeTache where employe.id = :id and dateFinReelle is not null", 
                EmployeTache.class)).thenReturn(query);
        when(query.setParameter("id", employeId)).thenReturn(query);
        when(query.list()).thenReturn(employeTaches);
        employeService.afficherTachesRealisees(employeId);
    }

    @Test
    public void testAfficherProjetsGeres() {

        int employeId = 1;
        Employe employe = new Employe();
        employe.setId(employeId);
        employe.setNom("Dupont");
        employe.setPrenom("Jean");

        List<Projet> projets = new ArrayList<>();
        Projet projet = new Projet();
        projet.setId(1);
        projet.setNom("Projet test");
        projet.setDateDebut(new Date());
        projet.setDateFin(new Date());
        projets.add(projet);

        when(session.get(Employe.class, employeId)).thenReturn(employe);
        when(session.createQuery("from Projet where chef.id = :id", Projet.class)).thenReturn(query);
        when(query.setParameter("id", employeId)).thenReturn(query);
        when(query.list()).thenReturn(projets);

        employeService.afficherProjetsGeres(employeId);
    }
}