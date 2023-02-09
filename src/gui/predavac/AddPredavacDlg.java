package gui.predavac;

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
import gui.kurs.AddKursDlg;
import managers.UserManager;

public class AddPredavacDlg extends JDialog{


	private static final long serialVersionUID = 9180109105810205327L;

	private List<Predavac> p = new ArrayList<Predavac>();
	private JList<Predavac> list;
	private UserManager um;
	private Kurs k;
	
	public AddPredavacDlg(List<Predavac> p, UserManager um, Kurs k, JList l) {
		this.p = p;
		this.um = um;
		this.k = k;
		
		setModal(true);
		ImageIcon img = new ImageIcon("./img/add.png");
		this.setIconImage(img.getImage());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		list = new JList<Predavac>(new PredavacListBoxModel(this.p));
		list.setCellRenderer(new PredavacListRenderer());
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
		
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Predavac predavac = list.getSelectedValue();
				if(predavac==null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijednog predavača.", "Greška!", JOptionPane.WARNING_MESSAGE);

				}else {
					predavac.getKursevi().add(k);
					um.saveData();
					l.updateUI();
					AddPredavacDlg.this.dispose();
				}
			}
		});
		
		JButton btnOdustani = new JButton("Odustani");
		
		btnOdustani.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddPredavacDlg.this.dispose();
			}
		});
		
		btnPnl.add(btnDodaj);
		btnPnl.add(btnOdustani);
		add(btnPnl, BorderLayout.PAGE_END);
		
		
		pack();
		setLocationRelativeTo(null);
	}
}
