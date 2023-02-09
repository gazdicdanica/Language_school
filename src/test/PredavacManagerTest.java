package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Jezik;
import entity.Kurs;
import entity.Predavac;
import entity.Zaposleni.NivoSS;
import managers.JezikManager;
import managers.KursManager;
import managers.PredavacManager;
import managers.UserManager;

public class PredavacManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt","./data/cenovnik.txt");
	static UserManager um = new UserManager("./data/korisnici.txt", "./data/zahtevi.txt","./data/testovi.txt", km, jm);
	static PredavacManager pm = new PredavacManager(um,km);
	static Kurs k;
	static Predavac p;
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  p = new Predavac(123, null, null, null, null, null, null, null, "123", NivoSS.DR, 0, 0, null);
	  um.setPredavaci(p);
	  Jezik j = new Jezik(1,null);
	  k = new Kurs(11, null, j);
	  km.setKursevi(k);
	  p.setJezici(j);
	  p.dodajKurs(k);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testNadjiPredavaPoId() {
	  assertNotNull(pm.nadjiPredavacPoId(123));
	  assertNull(pm.nadjiPredavacPoId(4));
  }
  
  @Test
  public void testGetMoguciKursevi() {
	  assertFalse(pm.getMoguciKursevi(p).isEmpty());
  }
  
  @Test
  public void testGetPredavaciKurs() {
	  assertFalse(pm.getPredavaciKurs(k).isEmpty());
  }
  
  @Test 
  public void testGetMoguciPredavaci() {
	  assertTrue(pm.getMoguciPredavaci(k).isEmpty());
  }
  
  @Test
  public void testPromeniLozinku() {
	  assertFalse(pm.promeniLozinku(p, "12345", null, null));
  }
  
  @Test
  public void testAddKurs() {
	  assertNotNull(pm.addKurs(k, p));
  }
  
  @Test
  public void testAddJezik() {
	  assertNotNull(pm.addJezik(k.getJezik(), p));
  }
  
}
