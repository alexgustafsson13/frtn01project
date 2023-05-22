import se.lth.control.realtime.AnalogIn;

public class Control {
  private double sampleTime; // in seconds
  private double thetaError = 0;
  private double phiError = 0;

  private double phiRef = 0;
  private double thetaRef = 0;

  private double oldTheta = 0;
  private double thetaDot = 0;

  private double oldPhi = 0;
  private double phiDot = 0;

  private AnalogIn inPhiDot;
  private AnalogIn inThetaDot;

  private double pi = Math.PI;
  
  public Parameters param;

  private double u = 0;

  private Status status;

  //Enums used for internal reference.
  enum Status {
    ON,
    OFF,
    SWINGUP
}

  public Control() {
    this.sampleTime = 0.004;
    this.status = Status.OFF;
    try {
      this.inPhiDot = new AnalogIn(42);
      this.inThetaDot = new AnalogIn(43);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Updates the parameters.
  public synchronized void updateParams(Parameters param) {
    this.param = (Parameters) param.clone();
    this.phiRef = param.phiRef;
  }

  //Calculates the error of theta and phi.
  private void errorCalculate(double theta, double phi) {
    thetaError = (thetaRef - theta) % (2 * pi);
    if  (thetaError > pi) {
      thetaError =  thetaError - 2 * pi;
    }
    if (thetaError < -pi) {
      thetaError =  thetaError + 2 * pi;
    }

    phiError = (phiRef - phi) % (2 * pi);
    if (phiError > pi) {
      phiError = phiError - 2 * pi;
    }
    if (phiError < -pi) {
      phiError =  phiError + 2 * pi;
    }
  }

  //Tells the controller to switch off.
  public synchronized void setStatusOff() {
    this.status = Status.OFF;
  }

  //Called when in lower mode to determine the controlsignal.
  private void checkLowerStatus() {
    if (Math.abs(thetaError) < param.thetaThresh + 1 && Math.abs(thetaDot) < param.thetaDot + 3) {
      this.status = Status.ON;
    }

    if (Math.abs(thetaError) > (param.thetaThresh + 0.4) && Math.abs(thetaDot) > (param.thetaDot + 1)) {
      this.status = Status.OFF;
    }
  }

  //Called when in upper mode to determine the controlsignal.
  private void checkUpperStatus() {
    if (Math.abs(thetaError) < param.thetaThresh && Math.abs(thetaDot) < param.thetaDot) {
      this.status = Status.ON;
    } else if (Math.abs(phiDot) < param.phiDot) {
      this.status = Status.SWINGUP;
    } 

    if (Math.abs(thetaError) > (param.thetaThresh + 0.2) && status != Status.SWINGUP) {
      this.status = Status.OFF;
    }
  }


  //Calculates the controlsignal when in Lower-Mode
  public synchronized double lowerCalculate(double theta, double phi) {
    thetaRef = 0;

    errorCalculate(theta, phi);
    checkLowerStatus();

    try {
      thetaDot = inThetaDot.get(); //(theta - oldTheta)/ sampleTime;
      phiDot = inPhiDot.get(); //(phi - oldPhi)/ sampleTime;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (status == Status.ON) { // 0.02 0.012 0.08 0.0020
      u = (thetaError * 0.3 + thetaDot * -0.02 +  phiError * 0.07 + phiDot * -0.02);
    } else {
      u = 0;
    }
    System.out.println("error:" + thetaError);
    
    //System.out.println("thetaDot: " + thetaDot);
    //System.out.println("phiDot: " + phiDot);
    //System.out.println("theta: " + thetaError);
    //System.out.println("phi: " + phiError);
    
    oldPhi = phi;
    oldTheta = theta;
    //System.out.println("u: " + u);
    return u;
  }


  //Calculates the control-signal when in Upper-Mode
  public synchronized double upperCalculate(double theta, double phi) {
    thetaRef = Math.PI;

    errorCalculate(theta, phi);
    checkUpperStatus();
    try {
      thetaDot = inThetaDot.get(); //(theta - oldTheta)/ sampleTime;
      phiDot = inPhiDot.get(); //(phi - oldPhi)/ sampleTime;
    } catch (Exception e) {
      e.printStackTrace();
    }
    

    if (status == Status.ON) {
      u = -(thetaError * 0.65 + thetaDot * -0.045 +  phiError * 0.05 + phiDot * -0.03);
    } else if (status == Status.SWINGUP) {
      u = param.k1 * Math.signum(
          (Math.cos(thetaError+Math.PI) + ((thetaDot * thetaDot) / (5 * 6.7 * 6.7)) - 1) * thetaDot * Math.cos(thetaError+Math.PI))
          - param.k2 * phiDot;
    } else {
      u = 0;
    }
    System.out.println("error:" + thetaError);
    oldPhi = phi;
    oldTheta = theta;
    return u;
  }

  //Sets the phi-reference for the arm.
  public synchronized void setArmRef(double angle) {
    this.phiRef = angle;
  }

  //Returns the time.
  public synchronized long getHMillis() {
    return (long) (sampleTime * 1000);
  }

}