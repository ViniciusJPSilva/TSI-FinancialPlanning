package vjps.bloomcapital.gui.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

/**
 * A classe abstrata StandardChartPanel é uma extensão de JPanel que representa um painel de gráfico padrão.
 * 
 * @author Vinícius J P Silva
 * 
 * @see {@link JPanel}
 */
public abstract class StandardChartPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor.
	 * 
	 * @param title titulo do gráfico
	 * @param dataset o conjunto de dados do gráfico de pizza
	 * @param enableMouseWhell Indica se a rolagem do mouse está habilitada
	 */
	public StandardChartPanel(String title, Dataset dataset, boolean enableMouseWhell) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(499, 250));
        setBackground(Color.WHITE);
        
        final JFreeChart chart = createChart(dataset, title);
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(499, 250));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chartPanel.setMouseWheelEnabled(enableMouseWhell);
        
        add(chartPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Construtor sobrecarregado.
	 * 
	 * @param title O título do gráfico
	 * @param dataset O conjunto de dados do gráfico
	 * @param width A largura do painel
	 * @param height A altura do painel
	 * @param enableMouseWhell Indica se a rolagem do mouse está habilitada
	 */
	public StandardChartPanel(String title, Dataset dataset, int width, int height, boolean enableMouseWhell) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        
        final JFreeChart chart = createChart(dataset, title);
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(499, 250));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chartPanel.setMouseWheelEnabled(enableMouseWhell);
        
        add(chartPanel, BorderLayout.CENTER);
	}

	/**
	 * Cria um gráfico com um formato especifico com base nos dados fornecidos.
	 * 
	 * @param dataset o conjunto de dados do gráfico de pizza
	 * @param title o título do gráfico
	 * 
	 * @return o gráfico
	 */
	protected abstract JFreeChart createChart(Dataset dataset, String title);
	
} // class StandardChartPanel
