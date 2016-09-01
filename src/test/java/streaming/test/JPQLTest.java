/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streaming.test;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import streaming.entity.Film;

/**
 *
 * @author admin
 */
public class JPQLTest {
    
// Vérifier le titre du film d'id 4    
    @Test
public void checkTitreFilmId4(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select f from Film f where f.id=4");
        Film f = (Film)query.getSingleResult();
        Assert.assertEquals("Fargo", f.getTitre());
}
    
// Vérifier le nombre de films
@Test
public void ex2(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select COUNT(f) from Film f");
       long nbFilms = (long) query.getSingleResult();
        
        Assert.assertEquals(4L, nbFilms);
}

// Année de prod mini de nos films
@Test
    public void ex3(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select MIN(f.annee) from Film f");
        int anneeProd = (int) query.getSingleResult();
        
        Assert.assertEquals(1968, anneeProd);
}
    
    
    // Nombre de liens du film 'Big Lebowski' > compter des liens > count > "select count(l) from lien l  where l.film.titre='Big Lebowski (The)'";
    // long nbLiens = (long)query.getSingleresult(); quand count = long forcement
   // Assert.assertEquals(1, nbLiens);
    @Test
      public void ex4(){
    
      EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
      Query query = em.createQuery("select count(f) from Film f join f.liens lien where f.titre='Big Lebowski (The)'");
      long lienFilmLebowski = (long)query.getSingleResult();
        
      Assert.assertEquals(1, lienFilmLebowski);
}
      // Nombre de films réalisés par Polanski
    @Test
      public void ex5(){
    
      EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
      Query query = em.createQuery("select count(f) from Film f join f.realisateurs r where r.nom='Polanski'");
      long n = (long)query.getSingleResult();        
      Assert.assertEquals(2, n);
}      
      // Nombre de films interprétés par Polanski
      @Test
      public void ex6(){
    
      EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
      Query query = em.createQuery("select count(f) from Film f join f.acteurs a where a.nom='Polanski'");
      long filmPolanski = (long)query.getSingleResult();
        
      Assert.assertEquals(1, filmPolanski);
}   
          
    // Nombre de films à la fois interprétés et réalisés par polanski
@Test
    public void ex7(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select f from Film f join f.acteurs a where a.nom='Polanski' intersect select f from Film f join f.realisateurs re where re.nom='Polanski'");
        List <Film> b = query.getResultList();
        Assert.assertEquals(1, b.size());
}
    
    // Le titre du film d'horreur anglais réalisé par roman polanski
    @Test
    public void ex8(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select f from Film f join f.realisateurs re join f.pays pa join f.genre ge where re.nom='Polanski' and pa.nom='UK' and ge.nom='Horreur'");
        Film filmHorreur = (Film)query.getSingleResult();
        Assert.assertEquals("Le bal des vampires", filmHorreur.getTitre());
}
    
    // Le nombre de films réalisés par Joel Coen
        @Test
    public void ex9(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select count(f) from Film f join f.realisateurs re where re.nom='Coen' and re.prenom='Joel'");
        long n = (long)query.getSingleResult(); 
        Assert.assertEquals(2, n);
    }
    
    
    // Le nombre de films réalisés par les 2 frères coen
          @Test
    public void ex10(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select count(f) from Film f join f.realisateurs re where re.nom='Coen' ");
        // Query query = em.createQuery("select count(f) from Film f join f.realisateurs re where re.nom='Coen' and re.prenom='Joel' intersect 
        // select count(f) from Film f join f.realisateurs re where re.nom='Coen' and re.prenom='Ethan'");");
        
        // ou si la recherehc est au moins un des freres coen         
        // Query query = em.createQuery("select count(f) from Film f join f.realisateurs re where re.nom='Coen' and r.prenom IN ('Joel','Ethan') ");
        //
        
        long n = (long)query.getSingleResult(); 
        Assert.assertEquals(4, n);
    }
    
    // Le nombre de films des frères coen interprétés par Steve Buscemi
    public void ex11(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select f from Film f join f.realisateurs re where re.nom='Coen' and re.prenom='Joel' intersect select f from Film f join f.realisateurs re where re.nom='Coen' and re.prenom='Ethan' intersect select f from Film f join f.acteurs a where a.nom='Buscemi' and a.prenom='Steve'");
              
        int taille = query.getResultList().size(); 
        Assert.assertEquals(4, taille);
    }
    
    
    // Le nombre de films policiers des frères Coen interprétés par Steve Buscemi
        public void ex12(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select f from Film f join f.realisateurs re where re.nom='Coen' and re.prenom='Joel' intersect select f from Film f join f.realisateurs re where re.nom='Coen' and re.prenom='Ethan' intersect select f from Film f join f.acteurs a join f.genre g where a.nom='Buscemi' and a.prenom='Steve' and g.nom='Policier'");
              
        int taille = query.getResultList().size(); 
        Assert.assertEquals(4, taille);
    }
        
        // Le nombre d'épisodes de la saison 8 de la série Dexter
        public void ex13(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select count(e) from Episode e join e.saison s join s.serie se where s.numSaison=8 and se.titre='Dexter' ");
              
        long nbEpi = (long)query.getSingleResult();
        Assert.assertTrue(nbEpi==12);
    }
        
        // Le nombre de saisons de la série Dexter
        
        public void ex14(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select count(s) from Serie s join s.saisons sa where s.titre='Dexter'");
              
        long nbSaison = (long)query.getSingleResult();
        Assert.assertTrue(nbSaison==12);
    }
        
        // Le nombre total d'épisodes de la série Dexter
                public void ex15(){
    
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select count(s) from Serie s join s.saisons sa join sa.episodes where s.titre='Dexter'");
              
        long nbEp = (long)query.getSingleResult();
        Assert.assertTrue(nbEp==12);
    }
        
        
}
