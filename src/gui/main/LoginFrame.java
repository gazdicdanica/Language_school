package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.Administrator;
import entity.Predavac;
import entity.Sekretar;
import entity.Ucenik;
import gui.admin.AdminFrame;
import gui.predavac.PredavacFrame;
import gui.sekretar.SekretarFrame;
import gui.ucenik.UcenikFrame;
import managers.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class LoginFrame extends JFrame{

	private static final long serialVersionUID = -3682888377848497638L;
	
	private ManagerFactory managers;
	private JTextField txtUname;
	private JPasswordField txtPassword;
	
	public LoginFrame(ManagerFactory mf) {
		this.managers = mf;
		loginFrame();
	}
		
	
	private void loginFrame() {
		ImageIcon img = new ImageIcon("./img/languages.png");
		this.setIconImage(img.getImage());
		this.setSize(700, 450);
		this.setLocationRelativeTo(null);
		this.setTitle("Škola stranih jezika Lingua");
		JPanel pnlLogin = new JPanel();
		pnlLogin.setBackground(new Color(204, 166, 166));
		pnlLogin.setLayout(new MigLayout("fillx, filly", "[]60[][]60[]", "[]40[]20[][]15[][]"));
		this.add(pnlLogin, BorderLayout.CENTER);
		JLabel lbl= new JLabel("Dobrodošli!");
		lbl.setFont(lbl.getFont().deriveFont(30f));
		pnlLogin.add(lbl, "cell 2 1,alignx center");
		
		JLabel lblUname= new JLabel("Korisničko ime:");
		pnlLogin.add(lblUname, "cell 1 2,alignx center");
		
		txtUname = new JTextField(40);
		pnlLogin.add(txtUname, "cell 2 2");
		
		JLabel lblPassword = new JLabel("Lozinka: ");
		pnlLogin.add(lblPassword, "cell 1 3,alignx center");
		
		txtPassword = new JPasswordField(40);
		pnlLogin.add(txtPassword, "cell 2 3");
		
		JButton btnLogin = new JButton("Uloguj se");
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = new String(txtUname.getText());
				String password = new String(txtPassword.getPassword());
				if(username.equals("") || password.equals("")) {
					JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!", "Greska!", JOptionPane.WARNING_MESSAGE);
				}else{
					Object o = managers.getUserManager().checkLogin(username, password);
					if(o == null) {
						JOptionPane.showMessageDialog(null, "Pogresno korisnicko ime i/ili lozinka.", "Greska!", JOptionPane.WARNING_MESSAGE);
					}else {
						LoginFrame.this.setVisible(false);
						switch(String.valueOf(o.getClass().getSimpleName())) {
						case "Administrator":
							new AdminFrame((Administrator) o, managers.getUserManager(), LoginFrame.this);
							break;
						case "Sekretar":
							new SekretarFrame((Sekretar) o, managers.getUserManager(), LoginFrame.this);
							break;
						case "Predavac":
							new PredavacFrame((Predavac) o, managers.getUserManager(), managers.getUserManager().getTestManager(), LoginFrame.this);
							break;
						case "Ucenik":
							new UcenikFrame((Ucenik)o, managers.getUserManager(), managers.getUserManager().getTestManager(), LoginFrame.this);
							break;
						}
						
					}txtUname.setText("");
					txtPassword.setText("");
				}
					
			}
				
			
		});
		
		JButton btnCancel = new JButton("Odustani");
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		this.getRootPane().setDefaultButton(btnLogin);
		pnlLogin.add(btnLogin, "cell 2 4, alignx center, gapright 15");
		pnlLogin.add(btnCancel, "cell 2 4, alignx center");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				handleClosing();
			}
		});
		this.setVisible(true);
	}
	
	
	private void handleClosing() {
		Object[] options = {"Da", "Ne"};
		int answer = JOptionPane.showOptionDialog(null, "Da li zelite da zatvorite aplikaciju?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		switch(answer) {
		case JOptionPane.YES_OPTION:
			System.exit(0);
		case JOptionPane.NO_OPTION:
			break;
		}
		
	}
	
	
	public JTextField getTfUname() {
		return this.txtUname;
	}
	
	public JPasswordField getPfPassword() {
		return this.txtPassword;
	}

}

