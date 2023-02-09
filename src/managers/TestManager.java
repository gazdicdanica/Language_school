package managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.Kurs;
import entity.Predavac;
import entity.RezultatTesta;
import entity.Test;
import entity.Ucenik;

public class TestManager {

	private UserManager userManager;
	private KursManager kursManager;
	private String file;
	private List<Test> testovi = new ArrayList<Test>();
	
	public TestManager(String file, UserManager um, KursManager km) {
		this.kursManager = km;
		this.userManager = um;
		this.file = file;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while((line=in.readLine())!=null) {
				Test t = parse(line);
				testovi.add(t);
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Test parse(String line) {
		String[] tokens = line.split(";");
		int id = Integer.parseInt(tokens[0]);
		Kurs k = kursManager.nadjiKursPoId(Integer.parseInt(tokens[1]));
		LocalDate datum = LocalDate.parse(tokens[2]);
		Predavac p;
		if(tokens[3].equals("/")) {
			p = null;
		}else
			p = userManager.getPredavacManager().nadjiPredavacPoId(Integer.parseInt(tokens[3]));
		String u = tokens[4];
		String r = tokens[6];
		Test ret;
		if(u.equals("/")) {
			ret = new Test(id, k, datum, p, false);
		}else {
			List<Ucenik> ucenici = new ArrayList<Ucenik>();
			String[] uceniciId = u.split(",");
			for(int i = 0; i<uceniciId.length ; i++) {
				Ucenik ucenik = userManager.getUcenikManager().nadjiUcenikaPoId(Integer.parseInt(uceniciId[i]));
				ucenici.add(ucenik);
			}
			String ocenjen = tokens[5];
			boolean o;
			if(ocenjen.equals("true")) {
				o = true;
			}else {
				o = false;
			}
			if(r.equals("/")) {
				ret = new Test(id, k, datum, p, o, ucenici);
			}else {
				List<RezultatTesta> rezultati = new ArrayList<RezultatTesta>();
				String rez [] = r.split(",");
				for(int i = 0; i < rez.length; i++) {
					String rr [] = rez[i].split("\\.");
					Ucenik ucenik = userManager.getUcenikManager().nadjiUcenikaPoId(Integer.parseInt(rr[0]));
					RezultatTesta rezultat = new RezultatTesta(ucenik, Integer.parseInt(rr[1]));
					rezultati.add(rezultat);
				}ret = new Test(id, k, datum, p, o, ucenici, rezultati);
			}
			
		}return ret;
		
		
 	}
	
	public List<Test> getTestovi(){
		return this.testovi;
	}
	
	public void setTestovi(List<Test> testovi) {
		this.testovi = testovi;
	}
	
	public List<Test> getDostupniTermini(Kurs k) {
		List<Test> ret = new ArrayList<Test>();
		List<Test> t = this.getTestoviZaKurs(k);
		if(t.size()>0) {
			for(Test test:t) {
				if(!test.getOcenjen() && LocalDate.now().compareTo(test.getDatum())<0)
					ret.add(test);
			}
		}return ret;
	}
	
	public List<Test> getTestoviZaKurs(Kurs k){
		List<Test> ret = new ArrayList<Test>();
		for(Test t:testovi) {
			if (t.getKurs().equals(k)){
				ret.add(t);
			}
		}return ret;
	}
	
	public void dodajTest(Kurs k, LocalDate datum, Predavac p) {
		Random rand = new Random();
		int id;
		boolean found = false;
		while(true) {
			id = rand.nextInt(100);
			for(Test t:testovi) {
				if(t.getId() == id) {
					found = true;
					break;
				}
			}if(found) {
				found = false;
				continue;
			}else {
				break;
			}
		}
		Test t = new Test(id, k, datum, p, false);
		testovi.add(t);
		saveData();
	}
	
	public boolean obrisiTest(Test t) {
		if (t.getUcenici().size()>0) {
			return false;
		}else {
			testovi.remove(t);
			saveData();
			return true;
		}
	}
	
	public Test nadjiTestPoId(int id) {
		for(Test t:testovi) {
			if(t.getId() == id)
				return t;
		}return null;
	}
	
	public void izmeniTest(Test test, LocalDate d) {
		test.setDatum(d);
		saveData();
	}
	
	public boolean saveData() {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(this.file, false));
			for(Test t:testovi) {
				p.println(t.fileString());
			}
			p.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public RezultatTesta nadjiRezultat(Test t, Ucenik u) {
		if(!t.getOcenjen()) {
			return null;
		}
		for(RezultatTesta r:t.getRezultati()) {
			if(r.getUcenik().equals(u)) {
				return r;
			}
		}return null;
	}
	
	
	public boolean ponovljenTest(Ucenik u, LocalDate datum, Kurs k) {
		for(Test t:testovi) {
			if(t.getKurs().equals(k) && t.getDatum().isBefore(datum) && t.getUcenici().contains(u)) {
				return true;
			}
		}return false;
	}
	
	public List<Test> ocenjeniTestovi(Kurs k){
		List<Test> ret = new ArrayList<Test> ();
		for(Test t: this.getTestoviZaKurs(k)) {
			if(t.getOcenjen())
				ret.add(t);
		}return ret;
	}
	
	public double izracunajProsek(Test t) {
		double ukupno = 0;
		for(RezultatTesta r:t.getRezultati()) {
			ukupno+=r.getOcena();
		}t.setProsek(ukupno/t.getRezultati().size());
		return ukupno;
	}
}

