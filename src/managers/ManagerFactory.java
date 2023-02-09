package managers;

public class ManagerFactory {
	private UserManager userManager;
	private KursManager kursManager;
	private JezikManager jezikManager;

	public ManagerFactory(String userFile, String kursFile, String jezikFile, String zahtevFile, String testFile, String cenovnikFile) {
		this.jezikManager = new JezikManager(jezikFile);
		this.kursManager = new KursManager(jezikManager, kursFile, cenovnikFile);
		this.userManager = new UserManager(userFile, zahtevFile, testFile, kursManager, jezikManager);
		
	}
	
	public UserManager getUserManager() {
		return this.userManager;
	}
	
}
