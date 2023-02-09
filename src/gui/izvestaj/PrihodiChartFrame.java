package gui.izvestaj;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import managers.UserManager;

public class PrihodiChartFrame extends JFrame{
	
	private UserManager um;
	
  private static final long serialVersionUID = 450863522388222565L;
	
  public PrihodiChartFrame(UserManager um) throws IllegalArgumentException{
	  this.um = um;
	  
	  setResizable(false);
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setTitle("Prihodi i rashodi u tekućoj godini");
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  setLayout(new GridLayout(2, 2));
	  
	  Map<String, Double> prihodi = um.prihodiMap();
	  Map<String, Double> rashodi = um.rashodiMap();
	  
	  PRPieChart prihodiChart = new PRPieChart(prihodi, true);
	  JPanel prihodiPnl = new XChartPanel<PieChart>(prihodiChart.getChart());
	  PRPieChart rashodiChart = new PRPieChart(rashodi, false);
	  JPanel rashodiPnl = new XChartPanel<PieChart>(rashodiChart.getChart());
	  
	  Map<LocalDate,Float> p = um.prihodi();
	  Map<LocalDate,Double> r = um.rashodi();
	  
	  PXChart prihodiXChart = new PXChart(p);
	  JPanel pPanel = new XChartPanel<XYChart>(prihodiXChart.getChart());
	  RXChart rashodiXChart = new RXChart(r);
	  JPanel rPanel = new XChartPanel<XYChart>(rashodiXChart.getChart());
	  
	  add(prihodiPnl);
	  add(rashodiPnl);
	  add(pPanel);
	  add(rPanel);
	  
	  pack();
	  setLocationRelativeTo(null);
	  setVisible(true);
  }
}
