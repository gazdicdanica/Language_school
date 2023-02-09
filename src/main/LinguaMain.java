package main;

import gui.main.LoginFrame;
import managers.ManagerFactory;

public class LinguaMain {

	public static void main(String[] args) {
		ManagerFactory mf = new ManagerFactory("./data/korisnici.txt", "./data/kursevi.txt", "./data/jezici.txt", "./data/zahtevi.txt", "./data/testovi.txt", "./data/cenovnik.txt");
		LoginFrame login = new LoginFrame(mf);
		
	}

}
