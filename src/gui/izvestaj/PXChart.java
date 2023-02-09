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

public class PXChart {
	
	private Map<LocalDate,Float> map;
	private List<Date> datumi;
	private List<Float> cena;
	
	public PXChart(Map<LocalDate,Float> map) {
		this.map = map;
		this.datumi = new ArrayList<Date>();
		this.cena = new ArrayList<Float>();
		
		Set<Map.Entry<LocalDate, Float>>set = map.entrySet();
		Iterator<Entry<LocalDate, Float>> iter = set.iterator();
		while(iter.hasNext()) {
			Map.Entry<LocalDate, Float> pair = (Map.Entry<LocalDate, Float>)iter.next();
			Date date = Date.from(pair.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant());
			datumi.add(date);
			cena.add(pair.getValue());
		}
	}
	
	public XYChart getChart() {
		XYChart chart = new XYChartBuilder().width(500).height(400).build();
		chart.setTitle("Prihodi po datumima");
		XYSeries series = chart.addSeries("Prihodi",datumi, cena);
		series.setMarker(SeriesMarkers.NONE);
		return chart;
	}
}
