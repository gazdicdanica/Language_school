package managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import entity.Cenovnik;
import entity.Kurs;

public class CenovnikManager {
	private String file;
	private KursManager km;
	private Cenovnik cenovnik;
	
	public CenovnikManager(String file, KursManager km) {
		this.file = file;
		this.km = km;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while((line = in.readLine())!=null) {
				line = line.trim();
				this.cenovnik = parse(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private Cenovnik parse(String line) {
		Cenovnik ret;
		String tokens[] = line.split(";");
		int id = Integer.parseInt(tokens[0]);
		LocalDate vaziOd = LocalDate.parse(tokens[1]);
		LocalDate vaziDo = LocalDate.parse(tokens[2]);
		try {
			String ceneString [] = tokens[3].split(",");
			String ceneTestaString[] = tokens[4].split(",");
			Map<Kurs, Float> cene = new HashMap<Kurs,Float>();
			Map<Kurs,Float> ceneTesta = new HashMap<Kurs,Float>();
			for(int i = 0; i<ceneString.length; i++) {
				String kc[] = ceneString[i].split("\\-");
				int idKurs = Integer.parseInt(kc[0]);
				Kurs k = km.nadjiKursPoId(idKurs);
				Float cena = Float.parseFloat(kc[1]);
				cene.put(k, cena);
			
				String ct[] = ceneTestaString[i].split("\\-");
				int kursId = Integer.parseInt(ct[0]);
				Kurs kurs = km.nadjiKursPoId(kursId);
				Float cenaTesta = Float.parseFloat(ct[1]);
				ceneTesta.put(kurs, cenaTesta);
			}
			ret = new Cenovnik(id,vaziOd,vaziDo,cene,ceneTesta);
		}catch(IndexOutOfBoundsException e) {
			ret = new Cenovnik(id,vaziOd,vaziDo);
		}
		
		return ret;
	}
	
	public Cenovnik getCenovnik() {
		return this.cenovnik;
	}
	
	public boolean saveData() {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(file));
			p.println(this.cenovnik.fileString());
			p.close();
			return true;
		}catch(IOException e) {
			return false;
		}
	}
	
	public void setCenovnik(Cenovnik c) {
		this.cenovnik = c;
	}
}
