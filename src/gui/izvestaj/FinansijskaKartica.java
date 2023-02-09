package gui.izvestaj;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import entity.Test;
import entity.Ucenik;
import entity.Zahtev;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class FinansijskaKartica extends JFrame{

	private Ucenik u;
	private UserManager um;
	
  private static final long serialVersionUID = -985237283774152101L;
	
  public FinansijskaKartica(Ucenik u, UserManager um) {
	  this.u = u;
	  this.um = um;
	  
	  setTitle("Istorija plaćanja");
	  setResizable(false);
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  setLayout(new MigLayout("wrap 2", "[]10[]", "[]10[]10[]10[]10[]10[]"));
	  add(new JLabel("Svrha uplate"));
	  add(new JLabel("Iznos"));
	  add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap,span 2");
	  double ukupno = 0;
	  
	  for(Zahtev z: um.getZahtevManager().getZahtevi()) {
		  if(z.getStanje().equals(Zahtev.stanje.prihvacen) && z.getUcenik().equals(u)) {
			  add(new JLabel(z.getKurs().toString() + " - upis"));
			  if(ukupno==0) {
				  add(new JLabel(String.valueOf(um.getKursManager().getCenovnikManager().getCenovnik().getCene().get(z.getKurs())) + "RSD"));
				  ukupno+=um.getKursManager().getCenovnikManager().getCenovnik().getCene().get(z.getKurs());
			  }else {
				  add(new JLabel(String.valueOf(um.getKursManager().getCenovnikManager().getCenovnik().getPopusti().get(z.getKurs())) + "RSD"));
				  ukupno+=um.getKursManager().getCenovnikManager().getCenovnik().getPopusti().get(z.getKurs());
			  }
		  }
	  }for(Test t:um.getTestManager().getTestovi()) {
		  if(t.getUcenici().contains(u) && um.getTestManager().ponovljenTest(u, t.getDatum(), t.getKurs())) {
			  add(new JLabel(t.getKurs().toString() + " - test"));
			  add(new JLabel(String.valueOf(um.getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(t.getKurs())) + "RSD"));
			  ukupno+=um.getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(t.getKurs());
		  }
	  }
	  add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap,span 2");
	  add(new JLabel("Ukupno uplaćeno: " + String.valueOf(ukupno)+"RSD"), "span 2");
	  
	  
	  pack();
	  setLocationRelativeTo(null);
	  
  }
}
