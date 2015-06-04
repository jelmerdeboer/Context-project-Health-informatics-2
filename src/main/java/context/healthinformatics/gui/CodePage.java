package context.healthinformatics.gui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import context.healthinformatics.analyse.Interpreter;

/**
 * Class which represents one of the states for the variabel panel in the mainFrame.
 */
public class CodePage extends InterfaceHelper implements PanelState, Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final int FIELDCORRECTION = 200;
	private static final int ANALYZEBUTTONWIDTH = 200;
	private static final int ANALYZEBUTTONHEIGHT = 75;
	private MainFrame mf;
	private JTextArea code;
	private JTextArea intermediateResult;
	private JButton button;
	
	/**
	 * Constructor.
	 * @param m is the mainframe object
	 */
	public CodePage(MainFrame m) {
		mf = m;
	}

	@Override
	public JPanel loadPanel() {
		JPanel panel = MainFrame.createPanel(MainFrame.CODETABCOLOR,
				mf.getScreenWidth(), mf.getStatePanelSize());
		panel.setLayout(new GridBagLayout());
		code = createTextField(mf.getScreenWidth() / 2, 
				mf.getStatePanelSize() / 2 - FIELDCORRECTION);
		intermediateResult = createTextField(mf.getScreenWidth() / 2, 
				mf.getStatePanelSize() / 2 - FIELDCORRECTION);
		panel.add(code, setGrids(0, 0));
		intermediateResult.setEditable(false);
		panel.add(intermediateResult, setGrids(0, 2));
		intermediateResult.setText("test");
		button = createButton("Analyse", ANALYZEBUTTONWIDTH, ANALYZEBUTTONHEIGHT);
		button.addActionListener(new ActionHandler());
		panel.add(button, setGrids(1, 1));
		return panel;
	}
	
	/**
	 * Appends result Strings to intermediate result textfield.
	 * @param res String to be appended in textfield.
	 */
	public void setResult(String res) {
		intermediateResult.append(res);
	}
	
	/**
	 * Clears textfield of intermediate result.
	 */
	public void emptyResult() {
		intermediateResult.setText("");
	}
	
	/**
	 * Class which handles the actions when buttons are clicked.
	 */
	private class ActionHandler implements ActionListener {

		/**
		 * Action when the button is pressed the save pop up will be shown.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//String text = code.getText();
			//Interpreter interp = new Interpreter();
			//interp.interpret(text);
		}
	}
}
