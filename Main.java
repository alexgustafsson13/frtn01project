
public class Main {
  public static void main(String[] args) {
    // start sim
    SimFurutaPendulum sim = new SimFurutaPendulum(3.14, 0.0);

    // creates a controller
    Control controller = new Control();
    // creates a regul with the controller and sim.
    Regul regul = new Regul(controller, sim);


    Parameters param = new Parameters();
    //Set parameters
    param.k1 = 0.1;
    param.k2 = 0.01;
    param.phiRef = 0;
    param.thetaThresh = 0.15;
    param.phiDot = 0.5;
    param.thetaDot = 1.5;

    // creates a gui that in turn starts the plotter-threads and the regul-thread.
    GUI gui = new GUI(param, regul);
    gui.initializeGUI();
    gui.run();
  }
}