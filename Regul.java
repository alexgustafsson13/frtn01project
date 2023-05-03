public class Regul extends Thread {
  

  private Control controller; 
  //private GUI gui;
  private Param p;
  private SimFurutaPendulum sim;
  private String mode = "OFF"; //"OFF", "UPPER" eller "LOWER" best at det skickas direkt från gui

  private double uMin = -1.0;
	private double uMax = 1.0;

  public Regul(Control c, /*GUI g,*/ SimFurutaPendulum s) {
    this.controller = c;
    //this.gui = g;
    this.sim = s;

  }

  public String getMode(String mode) {
    //reutrn gui.getMode
    return "UPPER";
  }

	private double limit(double u, double umin, double umax) {
		if (u < umin) {
			u = umin;
		} else if (u > umax) {
			u = umax;
		}
		return u;
	}  

  public void run() {
    long t = System.currentTimeMillis();
    
    while (true) {
      // Read inputs
      double penAngle = sim.getThetaAngle();
      double armAngle = sim.getPhiAngle();

      switch ("UPPER" /*gui.getMode()*/) {
        case "OFF": {
          sim.setControlSignal(0);
          break;
        }
        case "UPPER": {
          double u = limit(controller.upperCalculate(penAngle, armAngle), uMin, uMax);
          break;
        }
        case "LOWER": {
          double u = limit(controller.lowerCalculate(penAngle, armAngle), uMin, uMax);
          break;
        }
        default: {
          System.out.println("Illegal mode");
          break;
        }
      }

      sim.setControlSignal(u);
      

      // sleep
      t = t + controller.getHMillis();
      duration = t - System.currentTimeMillis();
      if (duration > 0) {
        try {
          sleep(duration);
        } catch (InterruptedException x) {}
      } else {
        System.out.println("Lagging behind...");
      }
    }
  }
}