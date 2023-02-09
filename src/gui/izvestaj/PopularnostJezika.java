package gui.izvestaj;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;

import entity.Jezik;
import managers.UserManager;

public class PopularnostJezika extends JFrame{

	private UserManager um;
	
  private static final long serialVersionUID = 6033966680524720348L;
	
  public PopularnostJezika(UserManager um) {
	  this.um = um;
	  
	  setSize(700,500);
	  setResizable(false);
	  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	  setTitle("Popularnost jezika - Izveštaj");
	  ImageIcon img = new ImageIcon("./img/report.png");
	  setIconImage(img.getImage());
	  
	  Map<Jezik, Integer> map = um.popularnostJezika();
	  
	  JezikChart chart = new JezikChart(map);
	  JPanel chartPnl = new XChartPanel<PieChart>(chart.getChart());
	  
	  add(chartPnl, BorderLayout.CENTER);
	  
	  setLocationRelativeTo(null);
	  setVisible(true);
  }
}
