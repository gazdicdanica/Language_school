package gui.izvestaj;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;

public class PRPieChart {
	private Map<String, Double> prihodi;
	private Map<String, Double> rashodi;
	private boolean bool;
	
	public PRPieChart(Map<String, Double> map, boolean bool) {
		this.bool = bool;
		if(this.bool)
			this.prihodi = map;
		else
			this.rashodi = map;
	}
	
	public PieChart getChart() {
		PieChart chart = new PieChartBuilder().width(500).height(400).build();
		if(bool) {
			chart.setTitle("Prihodi u tekućoj godini");
			Set<Map.Entry<String, Double>>set = prihodi.entrySet();
			Iterator<Entry<String, Double>> iter = set.iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Double> pair = (Map.Entry<String, Double>)iter.next();
				chart.addSeries(pair.getKey(), pair.getValue());
			}
		}else {
			chart.setTitle("Rashodi u tekućoj godini");
			Set<Map.Entry<String, Double>>set = rashodi.entrySet();
			Iterator iter = set.iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Double> pair = (Map.Entry<String, Double>)iter.next();
				chart.addSeries(pair.getKey(), pair.getValue());
			}
		}return chart;
		
	}
}
