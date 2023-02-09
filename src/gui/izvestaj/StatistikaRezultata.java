package gui.izvestaj;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import entity.Kurs;
import entity.Test;
import managers.TestManager;

public class StatistikaRezultata extends JFrame{

	private TestManager tm;
	
  private static final long serialVersionUID = -3293647086918546366L;
	
  public StatistikaRezultata(TestManager tm, Kurs k) throws IllegalArgumentException{
	  this.tm = tm;
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setResizable(false);
	  setTitle("Statistika rezultata za kurs " + k.toString());
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  
	  List<Test> testovi = tm.ocenjeniTestovi(k);
	  for(Test t:testovi) {
		  tm.izracunajProsek(t);
	  }
	  
	  RezultatiChart chart = new RezultatiChart(testovi);
	  
	  JPanel pnl = new XChartPanel<XYChart>(chart.getChart());
	  add(pnl);
	  
	  pack();
	  setLocationRelativeTo(null);
  }
  
}
