public class Regul extends Thread {

  private Control controller;
  // private GUI gui;
  private SimFurutaPendulum sim;
  private Mode mode = Mode.OFF; // "OFF", "UPPER" eller "LOWER" best at det skickas direkt från gui

  private double u = 0;
  private double uMin = -1.0;
  private double uMax = 1.0;
  private Parameters param;
  private RefParameters refparam;
  private Boolean running;

  public Regul(Control c, /* GUI g, */ SimFurutaPendulum s) {
    this.controller = c;
    // this.gui = g;
    this.sim = s;

  }

  public void setParameters(Parameters param) {
    this.param = (Parameters) param.clone();
    controller.updateParams(this.param);
  }

  public void setRefParameters(RefParameters refparam) {
    this.refparam = (RefParameters) refparam.clone();
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  private double limit(double u, double umin, double umax) {
    if (u < umin) {
      u = umin;
    } else if (u > umax) {
      u = umax;
    }
    return u;
  }

  public void shutDown() {
    running = false;
    System.out.println("shuting down");
  }

  public void run() {
    running = true;
    long duration;
    long t = System.currentTimeMillis();

    while (running) {
      // Read inputs
      double penAngle = sim.getThetaAngle();
      double armAngle = sim.getPhiAngle();

      switch ("UPPER" /* gui.getMode() */) {
        case "OFF": {
          sim.setControlSignal(0);
          break;
        }
        case "UPPER": {
          u = limit(controller.upperCalculate(penAngle, armAngle), uMin, uMax);
          break;
        }
        case "LOWER": {
          u = limit(controller.lowerCalculate(penAngle, armAngle), uMin, uMax);
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
        } catch (InterruptedException x) {
        }
      } else {
        System.out.println("Lagging behind...");
      }
    }
  }
}