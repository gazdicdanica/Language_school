package gui.admin;

import java.time.LocalDate;
import java.time.Period;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import entity.Predavac;
import entity.Sekretar;
import entity.Test;
import entity.Ucenik;
import entity.Zahtev;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class PrihodiFrame extends JFrame{

	private UserManager um;
	private LocalDate start;
	private LocalDate end;
	
  private static final long serialVersionUID = -1167470981339506331L;

  public PrihodiFrame(UserManager um, LocalDate start, LocalDate end) {
	  this.um = um;
	  this.start = start;
	  this.end = end;
	  
	  setTitle("Prihodi/rashodi za period " + start.toString() + " - " + end.toString());
	  setResizable(false);
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setLayout(new MigLayout("wrap 3", "[]10[]10[]", "[]10[]10[]10[]10[]10"));
	  
	  Period period = Period.between(start, end);
	  int month = period.getMonths();
	  
	  add(new JLabel("Prihodi"), "span 3");
	  add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 3");
	  
	  for(Zahtev z:um.getZahtevManager().getZahtevi()) {
		  if(z.getStanje().equals(Zahtev.stanje.prihvacen) && z.getDatum().isAfter(start) && z.getDatum().isBefore(end)) {
			  add(new JLabel(z.getUcenik().toString()));
			  add(new JLabel(z.getKurs().toString() + " - upis"));
			  add(new JLabel(String.valueOf(um.getKursManager().getCenovnikManager().getCenovnik().getCene().get(z.getKurs()))+ "RSD"));
		  }
	  }for(Test t:um.getTestManager().getTestovi()) {
		  if(t.getDatum().isAfter(start)&&t.getDatum().isBefore(end)) {
			  for(Ucenik u:t.getUcenici()) {
				  if(um.getTestManager().ponovljenTest(u, t.getDatum(), t.getKurs())) {
					  add(new JLabel(u.toString()));
					  add(new JLabel(t.getKurs().toString() + " - test"));
					  add(new JLabel(String.valueOf(um.getKursManager().getCenovnikManager().getCenovnik().getCeneTesta().get(t.getKurs())) + "RSD"));
				  }
			  }
		  }
	  }
	  add(new JLabel(), "span 3");
	  add(new JLabel("Rashodi"), "span 3");
	  add(new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap, span 3");
	  
	  if(month>0) {
		  for(Predavac p:um.getPredavaci()) {
			  add(new JLabel(p.toString()));
			  add(new JLabel("Mesečna plata"));
			  add(new JLabel(String.valueOf(p.getPlata()*month) + "RSD"));
		  }for(Sekretar s:um.getSekretari()) {
			  add(new JLabel(s.toString()));
			  add(new JLabel("Mesečna plata"));
			  add(new JLabel(String.valueOf(s.getPlata()*month) + "RSD"));
		  }
	  }
	  
	  pack();
	  setLocationRelativeTo(null);
	  
  }
}
