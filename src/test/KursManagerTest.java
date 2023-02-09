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
import managers.JezikManager;
import managers.KursManager;
import managers.UserManager;

public class KursManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt", "./data/cenovnik.txt");
	static UserManager um = new UserManager("./data/korisnici.txt", "./data/zahtevi.txt","./data/testovi.txt", km, jm);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  Kurs k = new Kurs(123,null,new Jezik(1,null));
	  km.setKursevi(k);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testNadjiKursPoId() {
	  assertNotNull(km.nadjiKursPoId(123));
	  assertNull(km.nadjiKursPoId(1));
  }
  
  @Test
  public void testGetJezikManager() {
	  assertNotNull(km.getJezikManager());
  }
  
  @Test
  public void testGetKursevi() {
	  assertFalse(km.getKursevi().isEmpty());
  }
  
  @Test
  public void testTryDelKurs() {
	  assertTrue(km.tryDelKurs(123, um));
  }
  
  @Test
  public void testGetCenovnikManager() {
	  assertNotNull(km.getCenovnikManager());
  }
}
