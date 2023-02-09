package gui.izvestaj;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import entity.Jezik;
import gui.jezik.JezikListBoxModel;
import gui.jezik.JezikListRenderer;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class AnalizaUcenika extends JFrame{

	private UserManager um;
	private JList<Jezik> list;
	
  private static final long serialVersionUID = -2632058769754720274L;
	
  @SuppressWarnings("unchecked")
public AnalizaUcenika(UserManager um) {
	  this.um = um;
	  
	  setTitle("Analiza učenika po starosnim kategorijama");
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setResizable(false);
	  setLayout(new MigLayout("wrap 1", "[]", "[]10[]10[]10[]"));
	  
	  add(new JLabel("Izaberite jezik:"));
	  
	  list = new JList<Jezik>(new JezikListBoxModel(um.getJezikManager().getJezici()));
	  list.setCellRenderer(new JezikListRenderer());
	  list.setVisibleRowCount(5);
	  JScrollPane scr = new JScrollPane(list);
	  list.setFixedCellHeight(20);
	  list.setFixedCellWidth(300);
	  add(scr);
	  
	  add(new JLabel("Izaberite uzrasnu kategoriju: "));
	  String[] kat = {"5-9", "10-15", "16-19", "20-24", "25-30", "31-39", "40+"};
	  
	  JComboBox<String> cb = new JComboBox<>(kat);
	  cb.setPreferredSize(new Dimension(300,20));
	  add(cb);
	  
	  JPanel btnPanel = new JPanel();
	  JButton btnPrikazi = new JButton("Prikaži");
	  
	  JLabel lbl = new JLabel("Broj učenika koji zadovoljavaju kriterijum pretrage: ");
	  add(lbl);
	  
	  btnPanel.add(btnPrikazi);
	  btnPrikazi.addActionListener(new ActionListener(){
  
		  @Override
		  public void actionPerformed(ActionEvent e) {
			  Jezik j = list.getSelectedValue();
			  if(j == null) {
				  JOptionPane.showMessageDialog(null, "Niste izabrali nijedan jezik.", "Greška!", JOptionPane.WARNING_MESSAGE);
				  return;
			  }String kategorija =(String) cb.getSelectedItem();
			  int br = um.izbrojUcenike(j, kategorija);
			  lbl.setText("Broj učenika koji zadovoljavaju kriterijum pretrage: " + br);
		  }
	  });
	  
	  add(btnPanel);
	  
	  pack();
	  setLocationRelativeTo(null);
  }
  
}
