package context.healthinformatics.graphs;

import java.util.ArrayList;

import javax.swing.JPanel;

import context.healthinformatics.graphs.graphspanel.BoxPlotPanel;
import context.healthinformatics.graphs.graphspanel.FrequencyBarPanel;

/**
 * Controls the graphs.
 */
public class GraphController {

	private BoxPlot boxPlot;
	private JPanel boxPlotGraph;
	private FrequencyBar frequencyBar;
	private JPanel frequencyBarGraph;

	/**
	 * Constructs for each graph a class.
	 */
	public GraphController() {
		boxPlot = new BoxPlot();
		frequencyBar = new FrequencyBar();
		boxPlotGraph = boxPlot.getPanel();
		frequencyBarGraph = frequencyBar.getPanel();
		boxPlotGraph.setVisible(false);
		frequencyBarGraph.setVisible(false);
	}

	/**
	 * updates the graphs which are needed to be plotted.
	 * @param graphInput the panel with the data of all graphs.
	 */
	public void updateGraphs(GraphInputInterface graphInput) {
		if (graphInput.isSelected(graphInput.getBoxPlotCheckBox())) {
			plotBoxPlot(graphInput.getBoxPlotPanel());
		} else {
			boxPlotGraph.setVisible(false);
		}
		if (graphInput.isSelected(graphInput.getFrequencyCheckBox())) {
			plotFrequencyBar(graphInput.getFrequencyPanel());
		} else {
			frequencyBarGraph.setVisible(false);
		}
	}

	/**
	 * Creates a box plot.
	 * @param boxPlotPanel the panel with all data.
	 */
	private void plotBoxPlot(BoxPlotPanel boxPlotPanel) throws NullPointerException {
		GraphInputContainer container = boxPlotPanel.getGraphContainer();
		String type = container.getSelectedColumnX();
		ArrayList<String> columns = new ArrayList<String>();
		columns.add(container.getSelectedColumnY());
		if (type.equals("All")) {
			boxPlot.setDataPerColumn(columns);
		} else if (type.equals("Chunks")) {
			boxPlot.setDataPerChunk(columns);
		}
		boxPlotGraph = boxPlot.getPanel();
		boxPlotGraph.setVisible(true);
		boxPlot.createBoxPlot(container.getGraphTitleValue());
	}
	
	/**
	 * @return the panel with the box plot.
	 */
	public JPanel getBoxPlot() {
		return boxPlotGraph;
	}
	
	/**
	 * Creates a frequency bar.
	 * @param frequencyPanel the panel with all data.
	 */
	private void plotFrequencyBar(FrequencyBarPanel frequencyPanel) {
		frequencyBar.createFrequencyBar(frequencyPanel.getGraphContainer().getGraphTitleValue());
		frequencyBarGraph = frequencyBar.getPanel();
		frequencyBarGraph.setVisible(true);
	}

	/**
	 * @return the panel with the frequency bar.
	 */
	public JPanel getFerquencyBar() {
		return frequencyBarGraph;
	}

}
