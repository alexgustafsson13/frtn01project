import se.lth.control.realtime.*;

public class Regul extends Thread {

  public Control controller;
  private SimFurutaPendulum sim;
  private Mode mode;

  private double u = 0;
  private double uMin = -1.0;
  private double uMax = 1.0;
  private Parameters param;
  private Boolean running;
  private GUI gui;
  private long startTime;

  private AnalogIn armAngle;
  private AnalogIn penAngle;
  private AnalogOut output;
  

  public Regul(Control c, SimFurutaPendulum s) throws IOChannelException {
    this.controller = c;
    // this.gui = g;
    this.sim = s;
    this.armAngle = new AnalogIn(0);
    this.penAngle = new AnalogIn(1);
    this.output = new AnalogOut(0);
  }

  //Sets parameters and updates the controller accordingly.
  public synchronized void setParameters(Parameters param) {
    this.param = (Parameters) param.clone();
    controller.updateParams(this.param);
  }

  //Sets the initial mode as OFF
  public synchronized void setMode(Mode mode) {
    this.mode = mode;
    controller.setStatusOff();
  }

  //Limits the controllsignal
  private double limit(double u, double umin, double umax) {
    if (u < umin) {
      u = umin;
    } else if (u > umax) {
      u = umax;
    }
    return u;
  }

  // Gives a number in modulo 2pi between -pi and pi
  private double bound(double x) {
    x = x % (2*Math.PI);
    if  (x > Math.PI) {
      x =  x - 2 * Math.PI;
    }
    if (x < -Math.PI) {
      x =  x + 2 * Math.PI;
    }
    return x;
  }

  //Stops the while-loop running the regul.
  public void shutDown() {
    running = false;
    System.out.println("shuting down");
  }

  //Method called by starting the regul-thread.
  public void run() {
    running = true;
    long duration;
    startTime = System.currentTimeMillis();
    long t = System.currentTimeMillis();
    double pen = 0;
    double arm = 0;

    while (running) {
      // Read inputs
      //double pen = sim.getThetaAngle();
      //double arm = sim.getPhiAngle();
      try {
        pen = penAngle.get();
        arm = armAngle.get();
        switch (mode) {
          case OFF: {
            u = 0;
            break;
          }
          case UPPER: {
            u = limit(controller.upperCalculate(pen, arm), uMin, uMax);
            break;
          }
          case LOWER: {
            u = limit(controller.lowerCalculate(pen, arm), uMin, uMax);
            break;
          }
          default: {
            System.out.println("Illegal mode");
            break;
          }
        }
        
      output.set(u);
      } catch (IOChannelException e) {
        e.printStackTrace();;
      }

      //Sends the controlsignal and adds the data to the plotter
      //sim.setControlSignal(u);
      putDataInGUI(arm, pen, u);

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

  //Puts the data in the plotters
  private void putDataInGUI(double arm, double pen, double ctrlSignal) {
    double timestamp = (double) (System.currentTimeMillis() - startTime) / 1000.0;
<<<<<<< Updated upstream
    System.out.println("Time: " + timestamp + "\nArm: " + armAngle + "\nPen: " + penAngle + "\nCtrl: " + ctrlSignal);
    gui.putMeasurementData(timestamp, bound(armAngle), bound(penAngle - 0.1), bound(param.phiRef));
=======
    System.out.println("Time: " + timestamp + "\nArm: " + arm + "\nPen: " + pen + "\nCtrl: " + ctrlSignal);
    gui.putMeasurementData(timestamp, arm, pen);
>>>>>>> Stashed changes
    gui.putControlData(timestamp, ctrlSignal);
  }

  //Adds a GUI reference to the regul.
  public void setGUI(GUI gui) {
    this.gui = gui;
  }
}