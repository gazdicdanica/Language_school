package gui.izvestaj;

import java.util.Arrays;
import java.util.List;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

public class OceneChart{
	private List<Integer> broj;
	
	public OceneChart(List<Integer> broj) {
		this.broj = broj;
	}
	
	public CategoryChart getChart() {
		CategoryChart chart = new CategoryChartBuilder().width(600).height(400).title("Statistika ocena").xAxisTitle("Ocene").yAxisTitle("Broj učenika").build();
		
		chart.addSeries("ocene", Arrays.asList(new Integer[] {10,9,8,7,6,5}), broj);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
	    chart.getStyler().setHasAnnotations(true);
		return chart;
	}
}
