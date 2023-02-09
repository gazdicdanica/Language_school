package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Sekretar;
import entity.Zahtev;
import managers.JezikManager;
import managers.KursManager;
import managers.UserManager;
import managers.ZahtevManager;

public class ZahtevManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt","./data/cenovnik.txt");
	static UserManager um = new UserManager("./data/korisnici.txt", "./data/zahtevi.txt","./data/testovi.txt", km, jm);
	static ZahtevManager zm = new ZahtevManager("./data/zahtevi.txt", um);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  Zahtev z1 = new Zahtev(1, null, null, Zahtev.stanje.uObradi);
	  Zahtev z2 = new Zahtev(2,null,null,Zahtev.stanje.prihvacen,new Sekretar(0, null, null, null, null, null, null, null, null),LocalDate.of(2021, 8, 3));
	  zm.setZahtevi(new ArrayList<Zahtev>());
	  zm.getZahtevi().add(z1);
	  zm.getZahtevi().add(z2);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Test
  public void testNadjiZahtevPoId() {
	  assertNotNull(zm.nadjiZahtevPoId(1));
	  assertNull(zm.nadjiZahtevPoId(0));
  }
  
  @Test
  public void testGetNeobradjeniZahtevi() {
	  assertFalse(zm.getNeobradjeniZahtevi().isEmpty());
  }
  
  @Test
  public void testGetZahteviIzvestaj() {
	  assertFalse(zm.getZahteviIzvestaj(LocalDate.of(2021, 7, 12), LocalDate.of(2021, 9, 1), null).isEmpty());
  }
}
