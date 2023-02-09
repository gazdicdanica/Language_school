package gui.kurs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import entity.Kurs;
import entity.Predavac;
import entity.Ucenik;
import entity.Zahtev;
import managers.UserManager;
import managers.ZahtevManager;

public class AddKursDlg extends JDialog{
	private List<Kurs> kursevi = new ArrayList<Kurs>();
	private JList<Kurs> list;
	private UserManager um;
	
	private static final long serialVersionUID = 5099284098672301929L;

	public AddKursDlg(List<Kurs> k, Object o, UserManager um) {
		this.kursevi = k;
		this.um = um;
		
		setModal(true);
		ImageIcon img = new ImageIcon("./img/add.png");
		this.setIconImage(img.getImage());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		list = new JList<Kurs>(new KursListBoxModel(this.kursevi));
		list.setCellRenderer(new KursListRenderer());
		list.setVisibleRowCount(5);
		list.setFixedCellHeight(20);
		list.setFixedCellWidth(200);
		list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scr = new JScrollPane(list);
		scr.setBorder(BorderFactory.createLineBorder(Color.black));
		add(scr, BorderLayout.CENTER);
		
		JPanel btnPnl = new JPanel();
		btnPnl.setLayout(new FlowLayout());
		
		JButton btnDodaj = new JButton("Dodaj");
		try {
			Ucenik u = (Ucenik) o;
			setTitle(u.getIme() + " " + u.getPrezime());
		}catch(Exception e) {
			
		}
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Kurs added = list.getSelectedValue();
				if(added == null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs.", "Greška!", JOptionPane.WARNING_MESSAGE);
				}else {
					switch(o.getClass().getSimpleName()) {
					case "Ucenik":
						Ucenik u = (Ucenik) o;
						Boolean bool = um.getUcenikManager().addKurs(added, u);
						if (!bool) {
							JOptionPane.showMessageDialog(null, "Učenik je već upisan na izabrani kurs ili je zahtev za upis podnet. " , "Obaveštenje.", JOptionPane.INFORMATION_MESSAGE);
						}else {
							um.getZahtevManager().addZahtev(u, added);
							AddKursDlg.this.dispose();
						}
						break;
					case "Predavac":
						Predavac p = (Predavac) o;
						Kurs i = um.getPredavacManager().addKurs(added, p);
						if(i!=null) {
							JOptionPane.showMessageDialog(null, "Ova osoba je već registrovana kao predavač na kursu " + i.getNaziv(), "Obaveštenje!", JOptionPane.INFORMATION_MESSAGE);	
						}else {
							AddKursDlg.this.dispose();
						}
						break;
					}
					
				}
			}
		});
		
		JButton btnOdustani = new JButton("Odustani");
		
		btnOdustani.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddKursDlg.this.dispose();
			}
		});
		
		btnPnl.add(btnDodaj);
		btnPnl.add(btnOdustani);
		add(btnPnl, BorderLayout.PAGE_END);
		
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
