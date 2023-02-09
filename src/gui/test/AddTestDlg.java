package gui.test;

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
import entity.Test;
import entity.Ucenik;
import managers.TestManager;

public class AddTestDlg extends JDialog{

	private List<Test> testovi = new ArrayList<Test>();
	private JList<Test> list;
	private Kurs k;
	private TestManager tm;
	private Ucenik ucenik;
	
	private static final long serialVersionUID = 7580703654635468349L;

	public AddTestDlg(Kurs k, Ucenik u, TestManager tm) {
		this.k = k;
		this.testovi = tm.getDostupniTermini(this.k);
		this.ucenik = u;
		this.tm = tm;
		
		setTitle("Dostupni termini");
		
		setModal(true);
		ImageIcon img = new ImageIcon("./img/add.png");
		this.setIconImage(img.getImage());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		list = new JList<Test>(new TestListBoxModel(this.testovi));
		list.setCellRenderer(new TestRenderer());
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
				Test added = list.getSelectedValue();
				if(added == null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan termin.", "Greška!", JOptionPane.WARNING_MESSAGE);
					return;
				}
				added.getUcenici().add(u);
				tm.saveData();
				AddTestDlg.this.dispose();
			}
		});
		
		JButton btnOdustani = new JButton("Odustani");
		
		btnOdustani.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddTestDlg.this.dispose();
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
