public class Control {
  private double sampleTime; // in seconds
  private double penError = 0;
  private double armError = 0;

  private double armRef = 0;
  private double penRef = 0;

  private double oldTheta = 0;
  private double deltaTheta = 0;

  private double oldPhi = 0;
  private double deltaPhi = 0;

  private double pi = 3.1415;
  private double k1 = 0;
  private double k2 = 0;

  private double u;

  public Control() {
    this.sampleTime = 0.004;
    // set parameters here
  }

  public synchronized void updateParams(Parameters param) {
    this.k1 = param.k1;
    this.k2 = param.k2;
  }

  public synchronized void updateRefParams(RefParameters refParam) {
    this.armRef = refParam.phi1;
    this.penRef = refParam.phi2;
  }

  public synchronized double lowerCalculate(double penAngle, double armAngle) {
    penRef = pi;
    penError = (penRef - penAngle) % (2 * pi);
    if (penError > pi) {
      penError = penError - 2 * pi;
    }

    this.armError = (armRef - armAngle) % (2 * pi);
    if (armError > pi) {
      armError = armError - 2 * pi;
    }
    // Both errors should be between [-pi, pi]

    deltaTheta = oldTheta - penAngle;

    

    if (Math.abs(penError) < 0.2 && Math.abs(deltaPhi) < 0.1) {
      u = 0.0; // math here
    } else {
      u = 0.0;
    }

    oldTheta = penAngle;
    return u;
  }

  public synchronized double upperCalculate(double penAngle, double armAngle) {
    penRef = 0;
    penError = (penRef - penAngle) % (2 * pi);
    if (penError > pi) {
      penError = penError - 2 * pi;
    }

    this.armError = (armRef - armAngle) % (2 * pi);
    if (armError > pi) {
      armError = armError - 2 * pi;
    }

    deltaTheta = penAngle - oldTheta;
    deltaPhi = armAngle - oldPhi;

    if (Math.abs(penError) < 0.5 && Math.abs(deltaTheta) < 1) {
      System.out.println(penError + " " + armError);
      u = -(penError * 6.236 + deltaTheta * 1.139 + armError * 0.1238 + deltaPhi * 0.2150);
    } else {
      u = k1 * Math.signum(
          (Math.cos(penError) + ((deltaTheta * deltaTheta) / (2 * 6.7 * 6.7)) - 1) * deltaTheta * Math.cos(penError))
          - k2 * deltaPhi;
    }

    oldPhi = armAngle;
    oldTheta = penAngle;
    return u;
  }

  public synchronized void setArmRef(double angle) {
    this.armRef = angle;
  }

  public synchronized long getHMillis() {
    return (long) (sampleTime * 1000);
  }

}