package gui.izvestaj;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import entity.Test;

public class RezultatiChart{
	private List<Test> testovi;
	private List<Date> datumi;
	private List<Double> prosek;
	
	public RezultatiChart(List<Test> testovi) throws IllegalArgumentException{
		this.testovi = testovi;
		this.datumi = new ArrayList<Date>();
		this.prosek = new ArrayList<Double>();
		
		for(Test t:testovi) { 
			Date date = Date.from(t.getDatum().atStartOfDay(ZoneId.systemDefault()).toInstant());
			this.datumi.add(date);
			this.prosek.add(t.getProsek());
		}
	}
	
	public XYChart getChart() {
		XYChart chart = new XYChartBuilder().width(600).height(400).xAxisTitle("Datum").yAxisTitle("Prosečna ocena").build();
		chart.getStyler().setYAxisMax(10.0);
		XYSeries series = chart.addSeries("Rezultati testa", datumi, prosek);
		series.setMarker(SeriesMarkers.NONE);
		return chart;
	}
	
}
