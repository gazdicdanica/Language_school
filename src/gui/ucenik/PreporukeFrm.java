package gui.ucenik;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import entity.Kurs;
import entity.Ucenik;
import gui.kurs.KursModel;
import managers.UserManager;

public class PreporukeFrm extends JFrame{

	private JTable tbl;
	private UserManager um;
	private Ucenik u;
	private List<Kurs> preporuke;
	private JFrame parent;
	
  private static final long serialVersionUID = -8495231907984799959L;
	
  public PreporukeFrm(Ucenik u, List<Kurs> preporuke, UserManager um, JFrame parent) {
	  this.u = u;
	  this.um = um;
	  this.preporuke = preporuke;
	  this.parent = parent;
	  
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setResizable(false);
	  setTitle("Preporuke za dalje kurseve");
	  ImageIcon img = new ImageIcon("./img/languages.png");
	  setIconImage(img.getImage());
	  
	  tbl = new JTable(new KursModel(preporuke,um.getKursManager(), PreporukeFrm.this));
	  tbl.getTableHeader().setOpaque(false);
	  tbl.getTableHeader().setBackground(new Color(204, 166, 166));
	  tbl.getTableHeader().setReorderingAllowed(false);
	  
	  JButton btnDodaj = new JButton();
	  ImageIcon imgDodaj = new ImageIcon("./img/add.png");
	  btnDodaj.setIcon(new ImageIcon(imgDodaj.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
	  
	  btnDodaj.addActionListener(new ActionListener(){
  
		  @Override
		  public void actionPerformed(ActionEvent e) {
			  int row = tbl.getSelectedRow();
			  if(row == -1) {
				  JOptionPane.showMessageDialog(null, "Niste izabrali nijedan kurs.", "Greška!", JOptionPane.WARNING_MESSAGE);
				  return;
			  }int id = Integer.parseInt(tbl.getValueAt(row, 0).toString());
			  Kurs k = um.getKursManager().nadjiKursPoId(id);
			  Object[] options = {"Da", "Ne"};
			  int answer = JOptionPane.showOptionDialog(null, "Da li želite da se upišete na ovaj kurs?", "Da li ste sigurni?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			  switch(answer) {
			  case JOptionPane.NO_OPTION:break;
			  case JOptionPane.YES_OPTION:
				  um.getUcenikManager().addKurs(k, u);
				  um.saveData();
				  UcenikFrame f = (UcenikFrame) parent;
				  f.refresh();
			  }
		  }
	  });
	  
	  JToolBar tb = new JToolBar();
	  tb.add(btnDodaj);
	  tb.setFloatable(false);
	  
	  JScrollPane scr = new JScrollPane(tbl);
	  add(scr, BorderLayout.CENTER);
	  JPanel btn = new JPanel();
	  btn.setLayout(new BoxLayout(btn, BoxLayout.LINE_AXIS));
	  btn.add(tb);
	  btn.add(Box.createHorizontalGlue());
	  btn.add(new JLabel("Zbog vaše lojalnosti na svaki sledeći kurs dobijate 10% popusta!   "));
	  add(btn, BorderLayout.PAGE_START);
	  pack();
	  setLocationRelativeTo(null);
  }
}
