package context.healthinformatics.graphs;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.sequentialdataanalysis.Chunk;

/**
 * Class to create the boxplot.
 */
public class BoxPlot extends InterfaceHelper {

	private static final long serialVersionUID = 1L;
	private JPanel chartContainerPanel;
	private JPanel mainPanel;
	private static final int BOX_PLOT_PANEL_HEIGHT = 400;
	private static final int BOX_PLOT_HEIGHT = 300;
	private JButton boxPlotTempRefreshButton;

	private int width;

	private DefaultBoxAndWhiskerCategoryDataset dataset;

	/**
	 * Creates a new demo.
	 *
	 * @param title
	 *            the frame title.
	 */
	public BoxPlot(final String title) {
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		width = getScreenWidth() / 2 - 2 * INSETS;
		boxPlotTempRefreshButton = new JButton("Refresh BoxPlotWithData");
		mainPanel = createEmptyWithGridBagLayoutPanel();
		mainPanel.setPreferredSize(new Dimension(width, BOX_PLOT_PANEL_HEIGHT));
		mainPanel.add(chartContainerPanel, setGrids(0, 0));
		mainPanel.add(boxPlotTempRefreshButton, setGrids(0, 1));
	}

	/**
	 * Get the mainPanel of the boxplot.
	 * 
	 * @return the panel with the boxplot
	 */
	public JPanel getPanel() {
		return mainPanel;
	}

	/**
	 * Create a botplot.
	 * 
	 * @param title
	 *            the title of the boxplot
	 */
	public void createBoxPlot(String title) {

		final CategoryAxis xAxis = new CategoryAxis("Type");
		final NumberAxis yAxis = new NumberAxis("Value");
		yAxis.setAutoRangeIncludesZero(false);
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis,
				renderer);

		final JFreeChart chart = new JFreeChart(title, new Font("SansSerif",
				Font.BOLD, 14), plot, true);
		mainPanel.remove(chartContainerPanel);
		chartContainerPanel = createEmptyWithGridBagLayoutPanel();
		chartContainerPanel.setPreferredSize(new Dimension(width,
				BOX_PLOT_HEIGHT));
		ChartPanel chartPanelTest = new ChartPanel(chart);
		chartPanelTest.setPreferredSize(new Dimension(width, BOX_PLOT_HEIGHT));
		chartContainerPanel.add(chartPanelTest, setGrids(0, 0));

		mainPanel.add(chartContainerPanel, setGrids(0, 0));
		mainPanel.revalidate();
	}

	/**
	 * Create the data set for the boxplot.
	 * 
	 * @param columns
	 *            the columns for which the box plot is made
	 */
	public void setDataPerColumn(ArrayList<String> columns) {
		dataset = new DefaultBoxAndWhiskerCategoryDataset();
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		StringBuilder buildTitle = new StringBuilder();
		for (int j = 0; j < columns.size(); j++) {
			buildTitle.append(" " + columns.get(j));
			ArrayList<Double> dataList = new ArrayList<Double>();
			for (int i = 0; i < chunks.size(); i++) {
				dataList.addAll(loopThroughChunks(chunks.get(i), columns.get(j)));
			}
			dataset.add(dataList, columns.get(j), " Type " + j);
		}
		createBoxPlot("Boxplot with: " + buildTitle.toString());
	}

	/**
	 * Create a dataset per chunk.
	 * 
	 * @param columns
	 *            the columns which we need the data from
	 */
	public void setDataPerChunk(ArrayList<String> columns) {
		dataset = new DefaultBoxAndWhiskerCategoryDataset();
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		StringBuilder buildTitle = new StringBuilder();
		for (int j = 0; j < columns.size(); j++) {
			buildTitle.append(" " + columns.get(j));
			for (int i = 0; i < chunks.size(); i++) {
				ArrayList<Double> dataList = new ArrayList<Double>();
				dataList.addAll(loopThroughChunks(chunks.get(i), columns.get(j)));
				dataset.add(dataList, columns.get(j), " Type " + i);
			}
		}
		createBoxPlot("Boxplot with: " + buildTitle.toString());
	}

	/**
	 * Get the values of the specified column of all the chunks and his
	 * children.
	 * 
	 * @param currentChunk
	 *            the chunk which we are looking at.=
	 * @param column
	 *            the column of the chunk we need
	 * @return the values or values if the chunk has childs
	 */
	private ArrayList<Double> loopThroughChunks(Chunk currentChunk, String column) {
		ArrayList<Double> listOfValues = new ArrayList<Double>();

		if (currentChunk.hasChild()) {
			ArrayList<Chunk> children = currentChunk.getChildren();
			for (int i = 0; i < children.size(); i++) {
				listOfValues.addAll(loopThroughChunks(children.get(i), column));
			}
		} else {
			listOfValues.add(getChunkData(currentChunk, column));
		}
		return listOfValues;
	}

	private double getChunkData(Chunk chunk, String column) {
		Db data = SingletonDb.getDb();
		try {
			ResultSet rs;
			rs = data.selectResultSet("result", column,
					"resultid = " + chunk.getLine());
			String value = "";
			if (rs.next()) {
				value = rs.getObject(column).toString();
			}
			rs.close();
			Double res = parseToDouble(value);
			return res;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Something went wrong creating the boxplot",
					"Analyse Error", JOptionPane.WARNING_MESSAGE);
		}
		return -1;
	}

	/**
	 * Parses the string from the db to a value.
	 * 
	 * @param value
	 *            the value
	 * @return double of the string
	 */
	public Double parseToDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return null;
		}

	}

	// @Override
	// public void actionPerformed(ActionEvent e) {
	// if (e.getSource() == boxPlotTempRefreshButton) {
	// ArrayList<String> listOfColumns = new ArrayList<String>();
	// listOfColumns.add("value");
	// // listOfColumns.add("time");
	// try {
	// setDataPerChunk(listOfColumns);
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	//
	// }

}
