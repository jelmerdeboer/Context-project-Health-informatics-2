package context.healthinformatics.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import context.healthinformatics.gui.InterfaceHelper;

/**
 * Creates a frequency bar.
 */
public class FrequencyBar extends InterfaceHelper {
	private static final long serialVersionUID = 1L;
	private JPanel chartContainerPanel;
	private JPanel mainPanel;
	private static final int BOX_PLOT_PANEL_HEIGHT = 400;
	private static final int BOX_PLOT_HEIGHT = 350;
	private int width;

	/**
	 * Constructor of FrequencyBar initialises the panels.
	 */
	public FrequencyBar() {
		width = getScreenWidth() / 2 - 2 * INSETS;
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(width, BOX_PLOT_PANEL_HEIGHT));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
	}

	/**
	 * Get the panel with the frequency bar.
	 * 
	 * @return the panel of the frequency bar
	 */
	public JPanel getPanel() {
		return mainPanel;
	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	private CategoryDataset createDataset(HashMap<String, Double> frequencies) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Entry<String, Double> e : frequencies.entrySet()) {
			dataset.addValue(e.getValue(), "Frequency", e.getKey());
		}
		return dataset;
	}

	/**
	 * Create a frequency bar with data and a title.
	 * 
	 * @param title
	 */
	public void createFrequencyBar(String title) {
		// TODO WE NEED TO ADJUST THIS TO READ THE FREQUENCY OF CODES!!!!!!!!
		HashMap<String, Double> map = new HashMap<String, Double>();
		map.put("a", 4.0);
		map.put("b", 7.0);
		map.put("c", 5.0);
		map.put("d", 12.0);
		map.put("2", 10.0);
		JFreeChart chart = createChart(title, createDataset(map));

		mainPanel.remove(chartContainerPanel);
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		ChartPanel chartPanelTest = new ChartPanel(chart);

		chartPanelTest.setPreferredSize(new Dimension(width - 1,
				BOX_PLOT_HEIGHT));
		chartContainerPanel.add(chartPanelTest, setGrids(0, 0));

		mainPanel.add(chartContainerPanel, setGrids(0, 0));
		mainPanel.revalidate();
	}

	/**
	 * Creates the chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return The chart.
	 */
	private JFreeChart createChart(String title, final CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createBarChart("Frequency Bar: "
				+ title, "Codes", "Frequency", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		chart.setBackgroundPaint(Color.white);
		final CategoryPlot plot = chart.getCategoryPlot();
		setLook(plot);
		return chart;

	}

	private void setLook(CategoryPlot plot) {
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final IntervalMarker target = new IntervalMarker(4.5, 7.5);
		target.setLabel("Target Range");
		target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
		target.setLabelAnchor(RectangleAnchor.LEFT);
		target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
		target.setPaint(new Color(222, 222, 255, 128));
		plot.addRangeMarker(target, Layer.BACKGROUND);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0.10);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);
	}
}
