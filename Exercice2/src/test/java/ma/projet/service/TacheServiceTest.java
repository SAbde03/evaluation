package ma.projet.service;

import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.classes.EmployeTache;
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

public class TacheServiceTest {

    @InjectMocks
    private TacheService tacheService;

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
        Tache tache = new Tache();
        tache.setNom("Test Tache");
        tache.setPrix(1500.0);
        tache.setDateDebut(new Date());
        tache.setDateFin(new Date());

        boolean result = tacheService.create(tache);
        assertTrue(result);
        verify(session).save(tache);
    }

    @Test
    public void testCreateWithException() {
        Tache tache = new Tache();
        doThrow(new RuntimeException("Test exception")).when(session).save(any(Tache.class));
        boolean result = tacheService.create(tache);
        assertFalse(result);
    }

    @Test
    public void testDelete() {
        Tache tache = new Tache();
        boolean result = tacheService.delete(tache);
        assertTrue(result);
        verify(session).delete(tache);
    }

    @Test
    public void testUpdate() {
        Tache tache = new Tache();
        tache.setNom("Updated Tache");
        boolean result = tacheService.update(tache);
        assertTrue(result);
        verify(session).update(tache);
    }

    @Test
    public void testFindById() {
        int id = 1;
        Tache tache = new Tache();
        tache.setId(id);
        when(session.get(Tache.class, id)).thenReturn(tache);
        Tache result = tacheService.findById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(session).get(Tache.class, id);
    }

    @Test
    public void testFindAll() {
        List<Tache> taches = new ArrayList<>();
        taches.add(new Tache());
        taches.add(new Tache());

        when(session.createQuery("from Tache", Tache.class)).thenReturn(query);
        when(query.list()).thenReturn(taches);
        List<Tache> result = tacheService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByProjet() {
        int projetId = 1;
        List<Tache> taches = new ArrayList<>();
        taches.add(new Tache());

        when(session.createQuery("from Tache where projet.id = :id", Tache.class)).thenReturn(query);
        when(query.setParameter("id", projetId)).thenReturn(query);
        when(query.list()).thenReturn(taches);

        List<Tache> result = tacheService.findByProjet(projetId);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAfficherTachesPrixSuperieur() {
        List<Tache> taches = new ArrayList<>();
        Tache tache = new Tache();
        tache.setId(1);
        tache.setNom("Tache coûteuse");
        tache.setPrix(1500.0);

        Projet projet = new Projet();
        projet.setNom("Projet Test");
        tache.setProjet(projet);

        taches.add(tache);

        when(session.createNamedQuery("Tache.findByPrixSuperieur", Tache.class)).thenReturn(query);
        when(query.setParameter("prix", 1000.0)).thenReturn(query);
        when(query.list()).thenReturn(taches);

        tacheService.afficherTachesPrixSuperieur();
    }

    @Test
    public void testAfficherTachesRealiseesPeriode() {
        Date dateDebut = new Date();
        Date dateFin = new Date();
        List<Object[]> results = new ArrayList<>();

        Tache tache = new Tache();
        tache.setId(1);
        tache.setNom("Tache réalisée");

        Projet projet = new Projet();
        projet.setNom("Projet Test");
        tache.setProjet(projet);

        EmployeTache employeTache = new EmployeTache();
        employeTache.setTache(tache);
        employeTache.setDateDebutReelle(dateDebut);
        employeTache.setDateFinReelle(dateFin);

        Object[] result = new Object[]{tache, employeTache};
        results.add(result);

        when(session.createQuery(anyString(), eq(Object[].class))).thenReturn(query);
        when(query.setParameter("dateDebut", dateDebut)).thenReturn(query);
        when(query.setParameter("dateFin", dateFin)).thenReturn(query);
        when(query.list()).thenReturn(results);

        tacheService.afficherTachesRealiseesPeriode(dateDebut, dateFin);
    }
}
