package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Jezik;
import entity.Kurs;
import entity.Ucenik;
import managers.JezikManager;
import managers.KursManager;
import managers.UcenikManager;
import managers.UserManager;

public class UcenikManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt","./data/cenovnik.txt");
	static UserManager um = new UserManager("./data/korisnici.txt", "./data/zahtevi.txt","./data/testovi.txt", km, jm);
	static UcenikManager ucm = new UcenikManager(um, km);
	static Ucenik u;
	static Kurs k;
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  u = new Ucenik(123, "imenko", "prezimic", null, null, null, null, null, "123");
	  um.setUcenici(u);
	  k = new Kurs(0, null, new Jezik(1,null));
	  km.setKursevi(k);
	  u.getKursevi().add(k);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testNadjiUcenikaPoId() {
	  assertNotNull(ucm.nadjiUcenikaPoId(123));
	  assertNull(ucm.nadjiUcenikaPoId(0));
  }
  
  @Test
  public void testNadjiMoguceKurseve() {
	  assertTrue(ucm.nadjiMoguceKurseve(u).isEmpty());
  }
  
  @Test
  public void testNapraviIme() {
	  assertEquals("imenkoprezimic123", ucm.napraviKIme(u));
  }
  
  @Test
  public void testPromeniLozinku() {
	  assertFalse(ucm.promeniLozinku(u, "12345", null));
  }
  
  @Test
  public void testAddKurs() {
	  assertFalse(ucm.addKurs(k, u));
  }
  
  @Test
  public void testPao() {
	  assertFalse(ucm.pao(u, k));
  }
}
