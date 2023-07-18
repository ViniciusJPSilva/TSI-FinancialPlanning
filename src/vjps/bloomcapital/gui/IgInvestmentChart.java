package vjps.bloomcapital.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

import vjps.bloomcapital.gui.chart.StandardChartPanel;

/**
 *
 * Classe que representa um diálogo de exibição de gráficos de investimentos.
 *
 * @author Vinícius J P Silva
 *
 */
public class IgInvestmentChart extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;

	/**
	 * Cria um novo diálogo de exibição de gráficos de investimentos.
	 * 
	 * @param component         o componente pai para o diálogo
	 * @param orientation       a orientação do gráfico (horizontal ou vertical)
	 * @param datasetFirstChart o conjunto de dados para o primeiro gráfico
	 * @param datasetSecondChart o conjunto de dados para o segundo gráfico
	 */
	public IgInvestmentChart(Component component, String title, PlotOrientation orientation, Dataset datasetFirstChart, Dataset datasetSecondChart) {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		contentPanel = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setBackground(new Color(255, 255, 255));
		
		JPanel firstChartPanel = new JPanel();
		firstChartPanel.setBounds(10, 11, 500, 315);
		firstChartPanel.setBackground(new Color(255, 255, 255));
		contentPanel.add(firstChartPanel);
		
		JPanel secondChartPanel = new JPanel();
		secondChartPanel.setBounds(549, 14, 500, 315);
		secondChartPanel.setBackground(new Color(255, 255, 255));
		contentPanel.add(secondChartPanel);
		
		
		firstChartPanel.add(new StandardChartPanel("", datasetFirstChart, 500, 315, false) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected JFreeChart createChart(Dataset dataset, String title) {
				CategoryDataset realDataset = (CategoryDataset) dataset;
				
				JFreeChart chart = ChartFactory.createBarChart(
		                title,
		                "",
		                "Valor (R$)",
		                realDataset,
		                orientation,           
		                true, true, false
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
		            	String categoryLabel = dataset.getColumnKey(category).toString();
		                double value = dataset.getValue(series, category).doubleValue();

		                return String.format("%s: R$ %,.2f", categoryLabel, value);
		            }
		        });
		       

		        return chart;
			}
		});
		
		secondChartPanel.add(new StandardChartPanel("", datasetSecondChart, 500, 315, false) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected JFreeChart createChart(Dataset dataset, String title) {
				CategoryDataset realDataset = (CategoryDataset) dataset;
				
				JFreeChart chart = ChartFactory.createBarChart(
		                title,
		                "",
		                "Rentabilidade",
		                realDataset,
		                orientation,           
		                true, true, false
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
		            	String categoryLabel = dataset.getColumnKey(category).toString();
		                double value = dataset.getValue(series, category).doubleValue();

		                return String.format("%s: %,.2f%%", categoryLabel, value);
		            }
		        });
		        
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
			}
		});
		
		setSize(1065, 379);
		setLocationRelativeTo(component);
		
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setVisible(true);
		
	}
} // class IgInvestmentChart
