
public class Main {
  public static void main(String[] args) {
    //start sim
    SimFurutaPendulum sim = new SimFurutaPendulum(0.0, 0.0);


    Control controller = new Control();
    Regul regul = new Regul(controller, sim);

    Parameters param = new Parameters();
    RefParameters refparam = new RefParameters();

    //Set param and refparam
    param.k1 = 1;

    GUI gui = new GUI(param, refparam, regul);
    gui.initializeGUI();
    gui.run();
  }
}