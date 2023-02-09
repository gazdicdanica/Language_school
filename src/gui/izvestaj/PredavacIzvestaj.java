package gui.izvestaj;

import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import entity.Predavac;
import entity.Test;
import managers.TestManager;
import net.miginfocom.swing.MigLayout;

public class PredavacIzvestaj extends JFrame{

	private Predavac p;
	private TestManager tm;
	private LocalDate start;
	private LocalDate end;
	
  private static final long serialVersionUID = 1661360724319590343L;
	
  public PredavacIzvestaj(Predavac p, TestManager tm, LocalDate start, LocalDate end) {
	  this.p = p;
	  this.tm = tm;
	  this.start = start;
	  this.end = end;
	  setTitle(p.toString() + " - Izveštaj");
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setResizable(false);
	  setLayout(new MigLayout("wrap 2", "[]15[]", "[]10[]10[]"));
	  
	  add(new JLabel("Od datuma: "));
	  add(new JLabel(start.toString()));
	  
	  add(new JLabel("Do datuma: "));
	  add(new JLabel(end.toString()));
	  
	  int br = prebroj();
	  
	  add(new JLabel("Broj testova koje je održao predavač " + p.toString() + ": " + br), "span 2");
	  
	  pack();
	  setLocationRelativeTo(null);
	  
  }
  
  private int prebroj() {
	  int ret = 0;
	  for(Test test:tm.getTestovi()) {
		  LocalDate datum = test.getDatum();
		  if(test.getPredavac().equals(p) && datum.isAfter(start) && datum.isBefore(end)) {
			  ret+=1;
		  }
	  }return ret;
  }
}
