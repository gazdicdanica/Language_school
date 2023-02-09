package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Jezik;
import entity.Kurs;
import entity.RezultatTesta;
import entity.Ucenik;
import managers.JezikManager;
import managers.KursManager;
import managers.TestManager;
import managers.UserManager;

public class TestManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt","./data/cenovnik.txt");
	static UserManager um = new UserManager("./data/korisnici.txt", "./data/zahtevi.txt","./data/testovi.txt", km, jm);
	static TestManager tm = new TestManager("./data/testovi.txt", um, km);
	static Kurs k;
	static Ucenik u;
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  Jezik j = new Jezik(11,null);
	  k = new Kurs(1, null, j);
	  entity.Test t1 = new entity.Test(1, k, LocalDate.of(2022, 2, 2), null, false);
	  u = new Ucenik(123, null, null, null, null, null, null, null, null, null);
	  RezultatTesta r = new RezultatTesta(u, 100);
	  entity.Test t2 = new entity.Test(2, k, null, null, true);
	  t2.setUcenik(u);
	  t2.addRezultat(r);
	  List<entity.Test> t = new ArrayList<entity.Test>();
	  t.add(t1);
	  t.add(t2);
	  tm.setTestovi(t);
	  
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testNadjiTestPoId() {
	  assertNotNull(tm.nadjiTestPoId(1));
	  assertNull(tm.nadjiTestPoId(0));
  }
  
  @Test
  public void testGetTestovi() {
	  assertFalse(tm.getTestovi().isEmpty());
  }
  
  @Test
  public void testGetTestoviZaKurs() {
	  assertFalse(tm.getTestoviZaKurs(k).isEmpty());
  }
  
  @Test
  public void testGetDostupniTermini() {
	  assertFalse(tm.getDostupniTermini(k).isEmpty());
  }
  
  @Test
  public void testNadjiRezultat() {
	  assertNotNull(tm.nadjiRezultat(tm.nadjiTestPoId(2), u));
  }
  
  @Test
  public void testOcenjeniTestovi() {
	  assertFalse(tm.ocenjeniTestovi(k).isEmpty());
  }
  
  @Test
  public void testIzracunajProsek() {
	  assertEquals(tm.izracunajProsek(tm.nadjiTestPoId(2)),10,0);
  }
}
