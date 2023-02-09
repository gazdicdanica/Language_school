package test;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Cenovnik;
import managers.CenovnikManager;
import managers.JezikManager;
import managers.KursManager;

public class CenovnikManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt","./data/testovi.txt");
	static CenovnikManager cm = new CenovnikManager("./data/cenovnik.txt", km);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  Cenovnik c = new Cenovnik(1, null, null, null, null);
	  cm.setCenovnik(c);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testGetCenovnik() {
    assertTrue(cm.getCenovnik()!=null);
  }
}
