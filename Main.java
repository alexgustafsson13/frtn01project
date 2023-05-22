import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.IOChannelException;

public class Main {

  public static boolean init = false;
  public static void main(String[] args) {
    // start sim
    //SimFurutaPendulum sim = new SimFurutaPendulum(3.14, 0.0);

    // creates a controller
    Control controller = new Control();
    // creates a regul with the controller and sim.
    Regul regul = null;
    try {
      regul = new Regul(controller);
      init = true;
    } catch (IOChannelException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    Parameters param = new Parameters();
    //Set initial parameters
    param.k1 = 0.11;
    param.k2 = 0.01;
    param.phiRef = 0;
    param.thetaThresh = 0.40;
    param.phiDot = 0.5;
    param.thetaDot = 1.5;

    // creates a gui that in turn starts the plotter-threads and the regul-thread.
    if (init) {
      GUI gui = new GUI(param, regul);
      gui.initializeGUI();
      gui.run();
    } else System.out.println("IOChannelException");
  }
}