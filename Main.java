
public class Main {
  public static void main(String[] args) {
    // start sim
    SimFurutaPendulum sim = new SimFurutaPendulum(3.14, 0.0);

    Control controller = new Control();
    Regul regul = new Regul(controller, sim);

    Parameters param = new Parameters();

    //Set param and refparam
    param.k1 = 0.1;
    param.k2 = 0.01;
    param.phiRef = 0;
    param.thetaThresh = 0.2;
    param.phiDot = 0.5;
    param.thetaDot = 2;

    GUI gui = new GUI(param, regul);
    gui.initializeGUI();
    gui.run();
  }
}