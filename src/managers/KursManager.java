package managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.Jezik;
import entity.Kurs;
import entity.Ucenik;

public class KursManager {
	private JezikManager jm;
	private CenovnikManager cm;
	private List<Kurs> kursevi;
	private String file;

	public KursManager(JezikManager manager, String file, String cenovnikFile) {
		jm = manager;
		kursevi = new ArrayList<Kurs> ();
		this.file = file;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while((line = in.readLine())!=null) {
				line = line.trim();
				String[]tokens = line.split(";");
				Kurs k = parse(tokens);
				kursevi.add(k);
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.cm = new CenovnikManager(cenovnikFile, KursManager.this);
		
	}
	
	private Kurs parse(String[] tokens) {
		int id = Integer.parseInt(tokens[0]);
		String naziv = tokens[1];
		int jezikId = Integer.parseInt(tokens[2]);
		Jezik j = jm.nadjiJezikPoId(jezikId);
		Kurs ret = new Kurs(id, naziv, j);
		return ret;
	}
	
	
	public Kurs nadjiKursPoId(int id) {
		Kurs ret = null;
		for(int i=0;i<kursevi.size();i++) {
			Kurs k = kursevi.get(i);
			if(k.getId() == id) {
				ret = k;
				break;
			}
		}
		return ret;
	}

	public JezikManager getJezikManager() {
		return jm;
	}

	public List<Kurs> getKursevi() {
		return kursevi;
	}
	
	public void editKurs(Kurs k, String naziv, float cena, float cenaTesta) {
		k.setNaziv(naziv);
		cm.getCenovnik().getCene().put(k, cena);
		cm.getCenovnik().getCeneTesta().put(k, cenaTesta);
		this.saveData();
		cm.saveData();
	}
	
	public void addKurs(String naziv, float cena, float cenaTesta, Jezik j) {
		int id;
		Random rand = new Random();
		boolean found = false;
		while(true) {
			id = rand.nextInt(100);
			for(Kurs k:kursevi) {
				if(k.getId() == id) {
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
		Kurs k = new Kurs(id, naziv, j);
		cm.getCenovnik().getCene().put(k, cena);
		cm.getCenovnik().getCeneTesta().put(k, cenaTesta);
		kursevi.add(k);
		this.saveData();
		cm.saveData();
	}
	
	public boolean saveData() {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(file));
			for(Kurs k:kursevi) {
				p.println(k.fileString());
			}
			p.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean tryDelKurs(int id, UserManager um) {
		Kurs kurs = this.nadjiKursPoId(id);
		for(Ucenik u:um.getUcenici()) {
			for(Kurs k:u.getKursevi()) {
				if(k.getId()==kurs.getId()) {
					return false;
				}
			}
		}return true;
	}
	
	public void delKurs(Kurs k) {
		kursevi.remove(k);
		cm.getCenovnik().getCene().remove(k);
		cm.getCenovnik().getCeneTesta().remove(k);
		saveData();
		cm.saveData();
	}
	
	public CenovnikManager getCenovnikManager() {
		return this.cm;
	}
	
	public void setKursevi(Kurs k) {
		this.kursevi = new ArrayList<Kurs>();
		kursevi.add(k);
	}
	
}
