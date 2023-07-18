package vjps.bloomcapital.gui.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;

/**
 * Painel que exibe um gráfico de pizza 3D com base nos dados fornecidos.
 * 
 * @author Vinícius J P Silva
 * 
 * @see {@link javax.swing.JPanel}
 * @see {@link org.jfree.chart.JFreeChart}
 *
 */
public class PieChartPanel extends StandardChartPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor.
	 * 
	 * @param title titulo do gráfico
	 * @param dataset o conjunto de dados do gráfico de pizza
	 */
	public PieChartPanel(String title, PieDataset dataset) {
		super(title, dataset, true);
	}
    
	
	/**
	 * 	Cria um gráfico de pizza 3D com base nos dados fornecidos.
	 * 
	 * @param dataset o conjunto de dados do gráfico de pizza
	 * @param title o título do gráfico
	 * 
	 * @return o gráfico de pizza 3D criado
	 */
	@Override
	protected JFreeChart createChart(Dataset dataset, String title) {
        
        final JFreeChart chart = ChartFactory.createPieChart3D(title, (PieDataset) dataset, true, true, false);
        
        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(90);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(1.0f);
        plot.setNoDataMessage("Não há dados para serem exibidos!");
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(Color.WHITE);
        plot.setDepthFactor(0.07);
        
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.LEFT); // Posicionar a legenda no lado esquerdo
        legend.setFrame(BlockBorder.NONE); // Remover a moldura da legenda
        
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0}");
        plot.setLegendLabelGenerator(labelGenerator);
        plot.setLegendItemShape(new Rectangle(10, 10)); 
        
        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setLabelOutlinePaint(Color.WHITE);
        plot.setLabelShadowPaint(null);
        plot.setLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("") {
			private static final long serialVersionUID = 1L;

			/*
			 * NOTA: O método abaixo formata o label do gráfico de pizza para
			 * exibir ar porcentagens com apenas 2 casas decimais.
			 * 
			 * O suppress warning é para o parâmetro "Comparable key" cujo tipo sempre será,
			 * no caso desta implementação, uma string.
			 * 
			 */
			@SuppressWarnings("rawtypes") 
			@Override
            public String generateSectionLabel(PieDataset dataset, Comparable key) {
                Number value = dataset.getValue(key);
                double percent = value.doubleValue();
                return String.format("%,.2f%%", percent);
            }
        });
        
        plot.setToolTipGenerator(new StandardPieToolTipGenerator("") {
        	private static final long serialVersionUID = 1L;

			/*
			 * NOTA: O método abaixo formata o label do gráfico de pizza para
			 * exibir ar porcentagens com apenas 2 casas decimais.
			 * 
			 * O suppress warning é para o parâmetro "Comparable key" cujo tipo sempre será,
			 * no caso desta implementação, uma string.
			 * 
			 */
			@SuppressWarnings("rawtypes")
			@Override
            public String generateSectionLabel(PieDataset dataset, Comparable key) {
                Number value = dataset.getValue(key);
                double percent = value.doubleValue();
                return String.format("%s: %,.2f%%", key.toString(), percent);
            }
        	
        });

        return chart;
    } // createChart()
	
	
} // class PieChartPanel
