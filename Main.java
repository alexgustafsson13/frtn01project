
public class Main {
  public static void main(String[] args) {
    // start sim
    SimFurutaPendulum sim = new SimFurutaPendulum(3.14, 0.0);

    Control controller = new Control();
    Regul regul = new Regul(controller, sim);

    Parameters param = new Parameters();
    RefParameters refparam = new RefParameters();
    GUI gui = new GUI(param, refparam, regul);
    gui.initializeGUI();
    gui.run();
  }
}