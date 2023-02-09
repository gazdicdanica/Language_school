package gui.jezik;

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

import entity.Jezik;
import entity.Predavac;
import managers.UserManager;

public class AddJezikDlg extends JDialog{

	private static final long serialVersionUID = 7226029223638448118L;
	private List<Jezik> jezici = new ArrayList<Jezik>();
	private JList<Jezik> list;
	private UserManager um;
	
	public AddJezikDlg(List<Jezik> j, Predavac p, UserManager um) {
		this.jezici = j;
		this.um = um;
		
		setModal(true);
		setResizable(false);
		setTitle("Dodavanje jezika");
		ImageIcon img = new ImageIcon("./img/add.png");
		this.setIconImage(img.getImage());
		
		list = new JList<Jezik>(new JezikListBoxModel(this.jezici));
		list.setCellRenderer(new JezikListRenderer());
		list.setVisibleRowCount(5);
		list.setFixedCellHeight(20);
		list.setFixedCellWidth(200);
		list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scr = new JScrollPane(list);
		scr.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		add(scr, BorderLayout.CENTER);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout());
		
		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jezik add = list.getSelectedValue();
				if(add == null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali nijedan jezik.", "Greška!", JOptionPane.WARNING_MESSAGE);
				}else {
					Jezik j = um.getPredavacManager().addJezik(add, p);
					if (j!=null) {
						JOptionPane.showMessageDialog(null, "Izabrani jezik je već dodat u listu jezika.", "Obaveštenje.", JOptionPane.INFORMATION_MESSAGE);
					}else {
						AddJezikDlg.this.dispose();
					}
				}
				
			}
		});
		
		JButton btnOdustani = new JButton("Odustani");
		
		btnOdustani.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddJezikDlg.this.dispose();
			}
		});
		
		
		btnPanel.add(btnDodaj);
		btnPanel.add(btnOdustani);
		add(btnPanel, BorderLayout.PAGE_END);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
