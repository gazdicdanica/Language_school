package gui.sekretar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.Kurs;
import entity.Predavac;
import gui.kurs.KursListBoxModel;
import gui.kurs.KursListRenderer;
import gui.predavac.AddPredavacDlg;
import gui.predavac.PredavacListBoxModel;
import gui.predavac.PredavacListRenderer;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class DodajPredavacaNaKurs extends JDialog{

	private static final long serialVersionUID = 5310652884660680453L;

	
	public DodajPredavacaNaKurs(UserManager um) {
	
		setResizable(false);
		setTitle("Dodaj predavače");
		setLayout(new MigLayout("wrap 1", "[]", "[][]10[][]10[]"));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		
		ImageIcon img = new ImageIcon("./img/add.png");
		setIconImage(img.getImage());
		
		JLabel lblKursevi = new JLabel("Kursevi");
		add(lblKursevi);
		JList<Kurs> listKurs = new JList<Kurs>(new KursListBoxModel(um.getKursManager().getKursevi()));
		listKurs.setCellRenderer(new KursListRenderer());
		JScrollPane scr1 = new JScrollPane(listKurs);
		listKurs.setFixedCellHeight(25);
		listKurs.setFixedCellWidth(250);
		listKurs.setVisibleRowCount(5);
		listKurs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(scr1);
		
		JLabel lblPredavac = new JLabel("Trenutni predavači");
		add(lblPredavac);
		JList<Predavac> listPredavac = new JList<Predavac>();
		listPredavac.setCellRenderer(new PredavacListRenderer());
		JScrollPane scr2 = new JScrollPane(listPredavac);
		listPredavac.setFixedCellHeight(25);
		listPredavac.setFixedCellWidth(250);
		listPredavac.setVisibleRowCount(5);
		listPredavac.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(scr2);
		
		JButton btnDodaj = new JButton("Dodaj predavača");
		btnDodaj.setEnabled(false);
				
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Kurs k = listKurs.getSelectedValue();
				List<Predavac> p = um.getPredavacManager().getMoguciPredavaci(k);
				if(p.size() == 0) {
					JOptionPane.showMessageDialog(null, "Nažalost nema dostupnih predavača za ovaj kurs.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				AddPredavacDlg dlg = new AddPredavacDlg(p, um, k, listPredavac);
				dlg.setVisible(true);
				listKurs.updateUI();
				listPredavac.updateUI();
			}
		});
		
		add(btnDodaj);
		
		listKurs.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				btnDodaj.setEnabled(true);
				Kurs k = listKurs.getSelectedValue();
				List<Predavac> p = um.getPredavacManager().getPredavaciKurs(k);
				listPredavac.setModel(new PredavacListBoxModel(p));
			}
		});
		
		pack();
		setLocationRelativeTo(null);
//		setVisible(true);
	}
}
