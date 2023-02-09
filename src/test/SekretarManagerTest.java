package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Sekretar;
import entity.Zaposleni.NivoSS;
import managers.JezikManager;
import managers.KursManager;
import managers.SekretarManager;
import managers.UserManager;

public class SekretarManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt","./data/cenovnik.txt");
	static UserManager um = new UserManager("./data/korisnici.txt", "./data/zahtevi.txt","./data/testovi.txt", km, jm);
	static SekretarManager sm = new SekretarManager(um);
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  Sekretar s = new Sekretar(123, null, null, null, null, null, null, null, "123", NivoSS.DR, 0, 0, null);
	  um.setSekretari(s);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testNadjiSekretaraPoId() {
	  assertNotNull(sm.nadjiSekretaraPoId(123));
	  assertNull(sm.nadjiSekretaraPoId(1));
  }
  
  @Test
  public void testPromeniLozinku() {
	  assertFalse(sm.promeniLozinku(sm.nadjiSekretaraPoId(123), "12345", null, null));
  }
}
