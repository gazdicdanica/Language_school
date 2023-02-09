package gui.izvestaj;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class RXChart {
	private Map<LocalDate,Double> map;
	private List<Date> datumi;
	private List<Double> cena;
	
	public RXChart(Map<LocalDate,Double> map) {
		this.map = map;
		this.datumi = new ArrayList<Date>();
		this.cena = new ArrayList<Double>();
		
		Set<Map.Entry<LocalDate, Double>> set = map.entrySet();
		Iterator<Entry<LocalDate, Double>> iter = set.iterator();
		while(iter.hasNext()) {
			Map.Entry<LocalDate, Double> pair = (Map.Entry<LocalDate, Double>)iter.next();
			Date date = Date.from(pair.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant());
			datumi.add(date);
			cena.add(pair.getValue());
		}
		
	}
	
	public XYChart getChart() {
		XYChart chart = new XYChartBuilder().width(500).height(400).build();
		chart.setTitle("Rashodi po datumima");
		XYSeries series = chart.addSeries("Rashodi", datumi,cena);
		chart.getStyler().setYAxisDecimalPattern("1,000,000");
		series.setMarker(SeriesMarkers.NONE);
		return chart;
	}
}
