package gui.izvestaj;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.PieStyler;

import entity.Jezik;

public class JezikChart {
	private Map<Jezik,Integer> map;
	
	public JezikChart(Map<Jezik, Integer> map) {
		this.map = map;
	}
	
	public PieChart getChart() {
		PieChart chart = new PieChartBuilder().width(600).height(400).title("Popularnost jezika").build();
		
		Set<Map.Entry<Jezik,Integer>>set = map.entrySet();
		Iterator iter = set.iterator();
		
		while(iter.hasNext()) {
			Map.Entry<Jezik, Integer> pair = (Map.Entry<Jezik, Integer>)iter.next();
			chart.addSeries(pair.getKey().toString(), pair.getValue());
			chart.getStyler().setAnnotationType(PieStyler.AnnotationType.LabelAndValue);
		}
		return chart;
	}
	
}
