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

public class JezikManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt", "./data/cenovnik.txt");
	static Jezik j;
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  j = new Jezik(123, null);
	  jm.setJezici(j);
	  Kurs k = new Kurs(1, null, j);
	  km.setKursevi(k);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testNadjiJezikPoId() {
	  assertNotNull(jm.nadjiJezikPoId(123));
	  assertNull(jm.nadjiJezikPoId(0));
  }
  
  @Test
  public void testGetJezici() {
	  assertFalse(jm.getJezici().isEmpty());
  }
  
  @Test
  public void testTryDelJezik() {
	  assertFalse(jm.tryDelJezik(j, km));
	  assertTrue(jm.tryDelJezik(new Jezik(111,null), km));
  }
}
