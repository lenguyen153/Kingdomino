package ca.mcgill.ecse223.kingdomino.view;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author AdamMigliore
 *
 */
public class KingdominoFrame extends JFrame {
	protected JFrame previousFrame = null;
	protected final Container self = this;
	private final int FRAME_WIDTH = 1024;
	private final int FRAME_HEIGHT = 640;
	protected JPanel mainPanel = new JPanel(new BorderLayout());

	public KingdominoFrame() {
		initializeFrame();
	}

	public KingdominoFrame(JFrame previousFrame) {
		this.previousFrame = previousFrame;
		initializeFrame();
	}

	/**
	 * Initialize frame with initial parameters
	 */
	private void initializeFrame() {
		// Set the frame title
		setTitle("Kingdomino");
		// set to default width and height
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		// centers frame
		setLocationRelativeTo(null);
		// set default operation when closing frame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// resizing
		setResizable(false);
		//Create the GUI
		createGUI();
		//set ContentPane to the mainPanel
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		// set visible
		setVisible(true);
	}
	
	public void createGUI() {
		//Create a GUI here, must be overriden
	}
	
	public int getHeight() {
		return FRAME_HEIGHT;
	}
	
	public int getWidth() {
		return FRAME_WIDTH;
	}

}
