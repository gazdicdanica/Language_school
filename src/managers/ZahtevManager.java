package managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import entity.Jezik;
import entity.Kurs;
import entity.Sekretar;
import entity.Ucenik;
import entity.Zahtev;
import entity.Zahtev.stanje;

public class ZahtevManager {
	private String file;
	private UcenikManager ucenikManager;
	private UserManager userManager;
	private KursManager kursManager;
	private List<Zahtev> zahtevi = new ArrayList<Zahtev>();
	
	public ZahtevManager(String file, UserManager um) {
		this.file = file;
		this.userManager = um;
		this.ucenikManager = um.getUcenikManager();
		this.kursManager = um.getKursManager();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.file));
			String line;
			while((line=in.readLine())!=null) {
				Zahtev z = parse(line);
				zahtevi.add(z);
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

	private Zahtev parse(String line) {
		String tokens [] = line.split(";");
		int id = Integer.parseInt(tokens[0]);
		int idUcenik = Integer.parseInt(tokens[1]);
		Ucenik u = ucenikManager.nadjiUcenikaPoId(idUcenik);
		int idKurs = Integer.parseInt(tokens[2]);
		Kurs k = kursManager.nadjiKursPoId(idKurs);
		Zahtev.stanje s = Zahtev.stanje.valueOf(tokens[3]);
		String idstr = tokens[4];
		String date = tokens[5];
		Zahtev ret;
		if(idstr.equals("/")) {
			ret = new Zahtev(id, u, k, s);
		}else {
			int idSekretar = Integer.parseInt(tokens[4]);
			Sekretar sekretar = userManager.getSekretarManager().nadjiSekretaraPoId(idSekretar);
			LocalDate datum = LocalDate.parse(date);
			ret = new Zahtev(id, u, k, s, sekretar, datum);
		}
		return ret;
	}
	
	public List<Zahtev> getZahtevi(){
		return this.zahtevi;
	}
	
	public boolean saveData() {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(this.file, false));
			
			for(Zahtev z:zahtevi) {
				p.println(z.fileString());
			}
			
			p.close();
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	
	public void removeZahtev(Ucenik u, Kurs k) {
		Iterator<Zahtev> itr = zahtevi.iterator();
		while(itr.hasNext()) {
			Zahtev z = itr.next();
			if(z.getUcenik().getId() == u.getId() && z.getKurs().getId() == k.getId()&&z.getStanje().equals(Zahtev.stanje.uObradi)) {
				itr.remove();
			}
		}saveData();
	}
	
	public void addZahtev(Ucenik u, Kurs k) {
		int id;
		Random rand = new Random();
		boolean found = false;
		while(true) {
			id = rand.nextInt(100);
			for(Zahtev z:zahtevi) {
				if(z.getId() == id) {
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
		Zahtev z = new Zahtev(id, u, k);
		zahtevi.add(z);
		z.obradiZahtev();
		saveData();
		
	}
	
	public Zahtev nadjiZahtevPoId(int id) {
		Zahtev ret = null;
		for(int i=0;i<zahtevi.size();i++) {
			Zahtev z = zahtevi.get(i);
			if(z.getId() == id) {
				ret = z;
				break;
			}
		}return ret;
	}
	
	public void odobriZahtev(int id, Sekretar s) {
		Zahtev z = this.nadjiZahtevPoId(id);
		Ucenik u  = z.getUcenik();
		u.getKursevi().add(z.getKurs());
		if(u.getKorisnickoIme().equals("")) {
			u.setKorisnickoIme(ucenikManager.napraviKIme(u));
			u.setLozinka(userManager.sifra());
		}
		z.prihvatiZahtev();
		z.setSekretar(s);
		z.setDatum(LocalDate.now());
		userManager.saveData();
		saveData();
	}
	
	public void odbijZahtev(int id, Sekretar s) {
		Zahtev z = this.nadjiZahtevPoId(id);
		z.odbijZahtev();
		z.setSekretar(s);
		z.setDatum(LocalDate.now());
		saveData();
	}
	
	public List<Zahtev> getNeobradjeniZahtevi(){
		List<Zahtev> ret = new ArrayList<Zahtev>();
		for(Zahtev z:zahtevi) {
			if(z.getStanje().equals(Zahtev.stanje.uObradi)) {
				ret.add(z);
			}
		}return ret;
	}
	
	public List<Zahtev> getZahteviIzvestaj(LocalDate start, LocalDate end, Jezik j){
		List<Zahtev> ret = new ArrayList<Zahtev>();
		for(Zahtev z:zahtevi) {
			if(!z.getStanje().equals(stanje.uObradi) && z.getDatum().isAfter(start)&&z.getDatum().isBefore(end)) {
				if(j!=null) {
					if(z.getKurs().getJezik().equals(j))
						ret.add(z);
				}else {
					ret.add(z);
				}
			}
		}return ret;
	}
	
	public void setZahtevi(ArrayList<Zahtev > zahtevi) {
		this.zahtevi = zahtevi;
	}

}
