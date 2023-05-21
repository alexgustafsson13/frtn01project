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

    private int plotterPriority = 5;
    private Parameters param;
    private Regul regul;

    public GUI(Parameters param, Regul regul) {
        this.param = param;
        this.regul = regul;
        this.regul.setGUI(this);
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
	private DoubleField parPhiRefField = new DoubleField(5,3);
	private DoubleField parThetaThreshField = new DoubleField(5,3);
	private DoubleField parPhiDotField = new DoubleField(5,3);
	private DoubleField parThetaDotField = new DoubleField(5,3);
	private JButton applyButton;

	private JRadioButton offModeButton = new JRadioButton("Off");
	private JRadioButton upperModeButton = new JRadioButton("Upper");
	private JRadioButton lowerModeButton = new JRadioButton("Lower");
	private JButton quitButton = new JButton("QUIT");

	private boolean hChanged = false;
	private boolean isInitialized = false;

    public void initializeGUI() {
		// Create main frame.
		frame = new JFrame("Furuta Pendulum GUI");

		// Create a panel for the two plotters.
		plotterPanel = new BoxPanel(BoxPanel.VERTICAL);
		// Create PlotterPanels.
		measPanel = new PlotterPanel(2, plotterPriority);
		measPanel.setYAxis(20.0, -10.0, 2, 2);
		measPanel.setXAxis(10, 5, 5);
		measPanel.setUpdateFreq(10);
		ctrlPanel = new PlotterPanel(1, plotterPriority);
		ctrlPanel.setYAxis(20.0, -10.0, 2, 2);
		ctrlPanel.setXAxis(10, 5, 5);
		ctrlPanel.setUpdateFreq(10);

		plotterPanel.add(measPanel);
		plotterPanel.addFixed(10);
		plotterPanel.add(ctrlPanel);

		// Create panels for the parameter fields and labels, add labels and fields 
		parPanel = new BoxPanel(BoxPanel.HORIZONTAL);
		parLabelPanel = new JPanel();
		parLabelPanel.add(new JLabel("K1(Swingup): "));
		parLabelPanel.setLayout(new GridLayout(0,1));
		parLabelPanel.add(new JLabel("K2(Swingup): "));
		parLabelPanel.add(new JLabel("PhiRef: "));
		parLabelPanel.add(new JLabel("Theta(Threshold): "));
		parLabelPanel.add(new JLabel("PhiDot(Threshold): "));
		parLabelPanel.add(new JLabel("ThetaDot(Threshold): "));
		parFieldPanel = new JPanel();
		parFieldPanel.setLayout(new GridLayout(0,1));
        parFieldPanel.add(parK1Field);
        parFieldPanel.add(parK2Field);
        parFieldPanel.add(parPhiRefField);
        parFieldPanel.add(parThetaThreshField);
        parFieldPanel.add(parPhiDotField);
        parFieldPanel.add(parThetaDotField);

		// Set initial parameter values of the fields
		parK1Field.setValue(param.k1);
		parK2Field.setValue(param.k2);
		parPhiRefField.setValue(param.phiRef);
		parThetaThreshField.setValue(param.thetaThresh);
		parPhiDotField.setValue(param.phiDot);
		parThetaDotField.setValue(param.thetaDot);

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
		parPhiRefField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.phiRef = parPhiRefField.getValue();
				applyButton.setEnabled(true);
			}
		});
		parThetaThreshField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.thetaThresh = parThetaThreshField.getValue();
				applyButton.setEnabled(true);
			}
		});
		parPhiDotField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.phiDot = parPhiDotField.getValue();
				applyButton.setEnabled(true);
			}
		});
        parThetaDotField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				param.thetaDot = parThetaDotField.getValue();
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
				applyButton.setEnabled(false);
			}
		});

		// Create panel with border to hold apply button and parameter panel
		BoxPanel parButtonPanel = new BoxPanel(BoxPanel.VERTICAL);
		parButtonPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		parButtonPanel.addFixed(10);
		parButtonPanel.add(parPanel);
		parButtonPanel.addFixed(10);
		parButtonPanel.add(applyButton);

        // Create panel for parameter fields, labels and apply buttons
		parPanel = new BoxPanel(BoxPanel.HORIZONTAL);
		parPanel.add(parButtonPanel);


		// Create panel for the radio buttons.
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(offModeButton);
		group.add(lowerModeButton);
		group.add(upperModeButton);
        offModeButton.setSelected(true);
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
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regul.shutDown();
                ctrlPanel.stopThread();
                measPanel.stopThread();
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
		leftPanel.add(quitButton, BorderLayout.SOUTH);

		// Create panel for the entire GUI.
		guiPanel = new BoxPanel(BoxPanel.HORIZONTAL);
		guiPanel.add(leftPanel);
		guiPanel.addGlue();
		guiPanel.add(plotterPanel);

		// WindowListener that exits the system if the main window is closed.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				regul.shutDown();
                ctrlPanel.stopThread();
                measPanel.stopThread();
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

        measPanel.start();
        ctrlPanel.start();
		
		isInitialized = true;
	}

    public void run() {
        regul.setMode(Mode.OFF);
		regul.setParameters(param);
        regul.start();
    }

    /** Called by Regul to plot a control signal data point. */
	public synchronized void putControlData(double t, double u) {
		if (isInitialized) {
			ctrlPanel.putData(t, u);
		} else {
			System.out.println("Note: GUI not yet initialized. Ignoring call to putControlData().");
		}
	}

	/** Called by Regul to plot a measurement data point. */
	public synchronized void putMeasurementData(double t, double arm, double pen) {
		if (isInitialized) {
			measPanel.putData(t, arm, pen);
		} else {
			System.out.println("Note: GUI not yet initialized. Ignoring call to putMeasurementData().");
		}
	}
}
