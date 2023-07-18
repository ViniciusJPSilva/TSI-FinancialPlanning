package vjps.bloomcapital.gui.chart;

import java.awt.Color;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

/**
 * Painel que exibe um gráfico de barras com base nos dados fornecidos.
 * 
 * @author Vinícius J P Silva
 * 
 * @see {@link javax.swing.JPanel}
 * @see {@link org.jfree.chart.JFreeChart}
 *
 */
public class VerticalBarChartPanel extends StandardChartPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor.
	 * 
	 * @param title titulo do gráfico
	 * @param dataset o conjunto de dados do gráfico de pizza
	 */
	public VerticalBarChartPanel(String title, Dataset dataset) {
		super(title, dataset, false);
	}

	/**
	 * Cria um gráfico de Barras com base nos dados fornecidos.
	 * 
	 * @param dataset o conjunto de dados do gráfico de barras
	 * @param title o título do gráfico
	 * 
	 * @return o gráfico de barras criado
	 */
	@Override
	protected JFreeChart createChart(Dataset dataset, String title) {
		CategoryDataset realDataset = (CategoryDataset) dataset;
		
		JFreeChart chart = ChartFactory.createBarChart(
                title,
                "",
                "",
                realDataset
        );
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setNoDataMessage("Não há dados para serem exibidos!");
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(Color.BLACK);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        
        // Tenta deixar o grafico 3D.
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(true);
        renderer.setShadowPaint(Color.LIGHT_GRAY);
        
        renderer.setDefaultToolTipGenerator(new CategoryToolTipGenerator() {
            @Override
            public String generateToolTip(CategoryDataset dataset, int series, int category) {
                String setrieLabel = dataset.getRowKey(series).toString();
                double value = dataset.getValue(series, category).doubleValue();

                return String.format("%s: %,.2f%%", setrieLabel, value);
            }
        });
        
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelsVisible(false); // Oculta os rótulos do eixo X
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new NumberFormat() {
			private static final long serialVersionUID = 1L;
			NumberFormat defaultFormat = NumberFormat.getPercentInstance();

            @Override
            public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
                return defaultFormat.format(number / 100, toAppendTo, pos);
            }

            @Override
            public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
                return defaultFormat.format(number / 100, toAppendTo, pos);
            }

            @Override
            public Number parse(String source, ParsePosition parsePosition) {
                throw new UnsupportedOperationException("Parsing não suportado!");
            }
        });

        return chart;
	} // createChart()
	
} // class VerticalBarChartPanel
