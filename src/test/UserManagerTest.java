package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entity.Administrator;
import entity.Jezik;
import entity.Kurs;
import entity.Predavac;
import entity.Sekretar;
import entity.Ucenik;
import entity.Zahtev;
import entity.Zaposleni.NivoSS;
import managers.JezikManager;
import managers.KursManager;
import managers.UserManager;

public class UserManagerTest {
	static JezikManager jm = new JezikManager("./data/jezici.txt");
	static KursManager km = new KursManager(jm,"./data/kursevi.txt","./data/cenovnik.txt");
	static UserManager um = new UserManager("./data/korisnici.txt", "./data/zahtevi.txt","./data/testovi.txt", km, jm);
	static Jezik j = new Jezik(1,"jezik");
	
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  Administrator a = new Administrator(0, null, null, null, null, null, null, null, null);
	  um.setAdmin(a);
	  Kurs k = new Kurs(11, "kurs", j);
	  List<Kurs> kursevi = new ArrayList<Kurs>();
	  kursevi.add(k);
	  Ucenik u = new Ucenik(123,null,null,null,LocalDate.of(1999, 10, 2),null,null,"ucenik","123",kursevi);
	  um.setUcenici(u);
	  List<Object> o = new ArrayList<Object>();
	  o.add(u);
	  um.setKorisnici(o);
	  jm.setJezici(j);
	  Sekretar s = new Sekretar(0, null, null, null, null, null, null, null, null, NivoSS.DR, 1, 0, LocalDate.of(2021, 07, 02));
	  Predavac p = new Predavac(0, null, null, null, null, null, null, null, null, NivoSS.DR, 1, 0, LocalDate.of(2021, 07, 10));
	  um.setPredavaci(p);
	  um.setSekretari(s);
	  
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}
  
  @Test
  public void rashodiMap() {
	  Map<String,Double> m = new LinkedHashMap<String,Double>(){{put("Januar", 0.0);put("Februar",0.0);put("Mart",0.0);put("April",0.0);
	  put("Maj",0.0);put("Jun",0.0);put("Jul",0.0);put("Avgust",161000.0);put("Septembar",0.0);put("Oktobar",0.0);put("Novembar",0.0);put("Decembar",0.0);}};
	  assertEquals(m, um.rashodiMap());
  }
  
  @Test
  public void testRashodi() {
	  Map<LocalDate,Double> m = new HashMap<LocalDate,Double>(){{put(LocalDate.of(2021, 8, 1),161000.0);}}; 
	  assertEquals(um.rashodi(),m);
  }
  
  @Test
  public void testPopularnostJezika() {
	  Map<Jezik,Integer> m = new HashMap<Jezik,Integer>(){{put(j,1);}};
	  assertEquals(um.popularnostJezika(),m);
  }
  
  @Test
  public void testIzbrojUcenike() {
    int i = um.izbrojUcenike(j,"20-24");
    assertEquals(i,1);
  }
  
  @Test
  public void testCheckLogin() { 
	  assertNotNull(um.checkLogin("ucenik", "123"));
	  assertNull(um.checkLogin("uceniik", "123"));
	  assertNull(um.checkLogin("ucenik", "12345"));
  }
  
  @Test
  public void testGetKorisnici() {
	  assertFalse(um.getKorisnici().isEmpty());
  }
  
  @Test
  public void testGetAdministrator() {
	  assertNotNull(um.getAdmin());
  }
  
  @Test
  public void testGetSekretari() {
	  assertFalse(um.getSekretari().isEmpty());
  }
  
  @Test
  public void testGetPredavaci() {
	  assertFalse(um.getPredavaci().isEmpty());
  }
  
  @Test
  public void testGetUcenici() {
	  assertFalse(um.getUcenici().isEmpty());
  }

}
