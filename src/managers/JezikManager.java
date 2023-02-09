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

public class JezikManager {
	private List<Jezik> jezici;
	private String file;
	
	public JezikManager(String file){
		jezici = new ArrayList<Jezik>();
		this.file = file;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while((line = in.readLine())!=null) {
				line = line.trim();
				String[]tokens = line.split(";");
				Jezik j = parse(tokens);
				jezici.add(j);
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

	private Jezik parse(String[] tokens) {
		int id = Integer.parseInt(tokens[0]);
		String ime = tokens[1];
		Jezik ret = new Jezik(id, ime);
		return ret;
	}
	
	public Jezik nadjiJezikPoId(int id) {
		Jezik ret = null;
		for(int i=0;i<jezici.size();i++) {
			Jezik j = jezici.get(i);
			if(j.getId() == id) {
				ret = j;
				break;
			}
		}
		return ret;
	}
	
	public List<Jezik> getJezici(){
		return this.jezici;
	}
		
	public boolean tryDelJezik(Jezik j, KursManager km) {
		for(Kurs k:km.getKursevi()) {
			if(j.getId() == k.getJezik().getId()) {
				return false;
			}
		}return true;
	}
	
	public void delJezik(Jezik j, PredavacManager pm) {
		jezici.remove(j);
		saveData();
		pm.removeJezik(j);
		
	}
	
	public boolean saveData() {
		try {
			PrintWriter p = new PrintWriter(new FileWriter(file));
			for(Jezik j:jezici) {
				p.println(j.fileString());
			}
			p.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void addJezik(String jezik) {
		Random rand = new Random();
		boolean found = false;
		int id;
		while(true) {
			id = rand.nextInt(100);
			for(Jezik j:jezici) {
				if(j.getId() == id) {
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
		
		Jezik j = new Jezik(id, jezik);
		jezici.add(j);
		saveData();
	}
	
	public void setJezici(Jezik j) {
		this.jezici = new ArrayList<Jezik>();
		jezici.add(j);
	}
}
