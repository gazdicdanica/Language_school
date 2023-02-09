package gui.izvestaj;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entity.Jezik;
import gui.admin.PrihodiFrame;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class DatumDlg extends JDialog{
	
	private UserManager um;
	private JComboBox<Jezik> cbJezik;
	
  private static final long serialVersionUID = 6881889905425580056L;
	
  public DatumDlg(UserManager um, boolean jezik) {
	  this.um = um;
	  
	  setModal(true);
	  setTitle("Opseg datuma");
	  setLayout(new MigLayout("wrap 3", "[]10[][]", "[]10[]10[]10[]"));
	  setResizable(false);
	  setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
	  add(new JLabel("Od: "));
	  JTextField tfOd = new JTextField(20);
	  add(tfOd, "span 2");
	  add(new JLabel("Do: "));
	  JTextField tfDo = new JTextField(20);
	  add(tfDo, "span 2");
	  if(jezik) {
		  add(new JLabel("Jezik:"));
		  cbJezik = new JComboBox<Jezik>();
		  for(Jezik j:um.getJezikManager().getJezici()) {
			  cbJezik.addItem(j);
		  }
		  cbJezik.setPreferredSize(new Dimension(200, cbJezik.getPreferredSize().height));
		  cbJezik.setSelectedIndex(-1);
		  add(cbJezik, "span 2");
	  }
	  
		
	  JButton btnOK = new JButton("Potvrdi");
	  JButton btnOdustani = new JButton("Odustani");
	  add(btnOK, "cell 1 3");
	  add(btnOdustani, "cell 2 3");
		
	  btnOK.addActionListener(new ActionListener(){

		  @Override
		  public void actionPerformed(ActionEvent e) {
			  try {
				  LocalDate start = LocalDate.parse(tfOd.getText().trim());
				  LocalDate end = LocalDate.parse(tfDo.getText().trim());
				  if(end.isBefore(start)) {
					  JOptionPane.showMessageDialog(null, "Krajnji datum ne može biti pre početnog.", "Greška!", JOptionPane.WARNING_MESSAGE);
					  return;
				  }
				  if(jezik) {
					  Jezik j = (Jezik) cbJezik.getSelectedItem();
					  ZahtevIzvestaj i = new ZahtevIzvestaj(um.getZahtevManager(), start, end, j);
					  i.setVisible(true);
					  i.toFront();
					  i.requestFocus();
				  }else {
					  PrihodiFrame f = new PrihodiFrame(um, start, end);
					  f.setVisible(true);
					  f.toFront();
					  f.requestFocus();
					  f.repaint();
				  }
				  dispose();
			  }catch(DateTimeParseException d) {
				  JOptionPane.showMessageDialog(null, "Unesite datume u fomatu: gggg-mm-dd", "Greška!", JOptionPane.WARNING_MESSAGE);
				  return;
			  }
		  }
	  });
		
	pack();
	setLocationRelativeTo(null);
	setVisible(true);
  }
}
