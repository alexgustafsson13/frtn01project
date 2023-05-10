import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import se.lth.control.*;
import se.lth.control.plot.*;

enum Mode {
    UPPER,
    LOWER,
    OFF
}

public class GUI {

    private int priority;
    private Parameters param;
    private RefParameters refparam;
    private float ref;
    private Mode mode;
    private Regul regul;

    public GUI(Parameters param, RefParameters refparam, Regul regul) {
        this.param = param;
        this.refparam = refparam;
        this.regul = regul;
    }

	// Declarartion of main frame.
	private JFrame frame;

	// Declarartion of panels.
	private BoxPanel guiPanel, plotterPanel, parPanel;
	private JPanel parLabelPanel, parFieldPanel, buttonPanel, somePanel, leftPanel;
	private PlotterPanel measPanel, ctrlPanel;

	// Declaration of components.

	private DoubleField parK1Field = new DoubleField(5,3);
	private DoubleField parK2Field = new DoubleField(5,3);
	private DoubleField parPhi1Field = new DoubleField(5,3);
	private DoubleField parPhi2Field = new DoubleField(5,3);
	private DoubleField parPhispeedField = new DoubleField(5,3);
	private DoubleField parRefPhi1Field = new DoubleField(5,3);
	private DoubleField parRefPhi2Field = new DoubleField(5,3);
	private JButton applyButton;

	private JRadioButton offModeButton = new JRadioButton("Off");
	private JRadioButton upperModeButton = new JRadioButton("Upper");
	private JRadioButton lowerModeButton = new JRadioButton("Lower");
	private JButton stopButton = new JButton("STOP");

	private boolean hChanged = false;
	private boolean isInitialized = false;

    public void initializeGUI() {
		// Create main frame.
		frame = new JFrame("Furuta Pendulum GUI");

		// Create a panel for the two plotters.
		plotterPanel = new BoxPanel(BoxPanel.VERTICAL);
		// Create PlotterPanels.
		measPanel = new PlotterPanel(2, priority);
		measPanel.setYAxis(20.0, -10.0, 2, 2);
		measPanel.setXAxis(10, 5, 5);
		measPanel.setUpdateFreq(10);
		ctrlPanel = new PlotterPanel(1, priority);
		ctrlPanel.setYAxis(20.0, -10.0, 2, 2);
		ctrlPanel.setXAxis(10, 5, 5);
		ctrlPanel.setUpdateFreq(10);

		plotterPanel.add(measPanel);
		plotterPanel.addFixed(10);
		plotterPanel.add(ctrlPanel);

		// Create panels for the parameter fields and labels, add labels and fields 
		parPanel = new BoxPanel(BoxPanel.HORIZONTAL);
		parLabelPanel = new JPanel();
		parLabelPanel.add(new JLabel("K1: "));
		parLabelPanel.setLayout(new GridLayout(0,1));
		parLabelPanel.add(new JLabel("K2: "));
		parLabelPanel.add(new JLabel("Phi1: "));
		parLabelPanel.add(new JLabel("Phi2: "));
		parLabelPanel.add(new JLabel("PhiSpeed: "));
		parFieldPanel = new JPanel();
		parFieldPanel.setLayout(new GridLayout(0,1));
        parFieldPanel.add(parK1Field);
        parFieldPanel.add(parK2Field);
        parFieldPanel.add(parPhi1Field);
        parFieldPanel.add(parPhi2Field);
        parFieldPanel.add(parPhispeedField);
        parFieldPanel.add(parRefPhi1Field);
        parFieldPanel.add(parRefPhi2Field); 

		// Set initial parameter values of the fields
		parK1Field.setValue(param.k1);
		parK2Field.setValue(param.k2);
		parPhi1Field.setValue(param.phi1);
		parPhi2Field.setValue(param.phi2);
		parPhispeedField.setValue(param.phispeed);
		parRefPhi1Field.setValue(refparam.phi1);
		parRefPhi2Field.setValue(refparam.phi2);
        // Set minimum någonstans?

		// Add action listeners to the fields
		parK1Field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.k1 = parK1Field.getValue();
				applyButton.setEnabled(true);
			}
		});
		parK2Field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.k2 = parK2Field.getValue();
				applyButton.setEnabled(true);
			}
		});
		parPhi1Field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.phi1 = parPhi1Field.getValue();
				applyButton.setEnabled(true);
			}
		});
		parPhi2Field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.phi2 = parPhi2Field.getValue();
				applyButton.setEnabled(true);
			}
		});
		parPhispeedField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.phispeed = parPhispeedField.getValue();
				applyButton.setEnabled(true);
			}
		});
        parRefPhi1Field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refparam.phi1 = parPhispeedField.getValue();
				applyButton.setEnabled(true);
			}
		});
        parRefPhi2Field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refparam.phi2 = parPhispeedField.getValue();
				applyButton.setEnabled(true);
			}
		});

		// Add label and field panels to parameter panel
		parPanel.add(parLabelPanel);
		parPanel.addGlue();
		parPanel.add(parFieldPanel);
		parPanel.addFixed(10);

		// Create apply button and action listener.
		applyButton = new JButton("Apply");
		applyButton.setEnabled(false);
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regul.setParameters(param);
				regul.setRefParameters(refparam);
				applyButton.setEnabled(false);
			}
		});

		// Create panel with border to hold apply button and parameter panel
		BoxPanel innerParButtonPanel = new BoxPanel(BoxPanel.VERTICAL);
		innerParButtonPanel.setBorder(BorderFactory.createTitledBorder("Inner Parameters"));
		innerParButtonPanel.addFixed(10);
		innerParButtonPanel.add(parPanel);
		innerParButtonPanel.addFixed(10);
		innerParButtonPanel.add(applyButton);

		// Create panel for the radio buttons.
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(offModeButton);
		group.add(lowerModeButton);
		group.add(upperModeButton);
		// Button action listeners.
		offModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                regul.setMode(Mode.OFF);
			}
		});
		lowerModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                regul.setMode(Mode.LOWER);
			}
		});
		upperModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                regul.setMode(Mode.UPPER);
			}
		});
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regul.shutDown();
				measPanel.stopThread();
				ctrlPanel.stopThread();
				System.exit(0);
			}
		});

		// Add buttons to button panel.
		buttonPanel.add(offModeButton, BorderLayout.NORTH);
		buttonPanel.add(lowerModeButton, BorderLayout.CENTER);
		buttonPanel.add(upperModeButton, BorderLayout.SOUTH);

		// Panel for parameter panel and radio buttons
		somePanel = new JPanel();
		somePanel.setLayout(new BorderLayout());
		somePanel.add(parPanel, BorderLayout.CENTER);
		somePanel.add(buttonPanel, BorderLayout.SOUTH);

		// Create panel holding everything but the plotters.
		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(somePanel, BorderLayout.CENTER);
		leftPanel.add(stopButton, BorderLayout.SOUTH);

		// Create panel for the entire GUI.
		guiPanel = new BoxPanel(BoxPanel.HORIZONTAL);
		guiPanel.add(leftPanel);
		guiPanel.addGlue();
		guiPanel.add(plotterPanel);

		// WindowListener that exits the system if the main window is closed.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				regul.shutDown();
				measPanel.stopThread();
				ctrlPanel.stopThread();
				System.exit(0);
			}
		});

		// Set guiPanel to be content pane of the frame.
		frame.getContentPane().add(guiPanel, BorderLayout.CENTER);

		// Pack the components of the window.
		frame.pack();

		// Position the main window at the screen center.
		Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension fd = frame.getSize();
		frame.setLocation((sd.width-fd.width)/2, (sd.height-fd.height)/2);

		// Make the window visible.
		frame.setVisible(true);
		
		isInitialized = true;
	}
}
