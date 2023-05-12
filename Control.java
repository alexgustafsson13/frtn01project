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
    this.sampleTime = 0.05;
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

    deltaTheta = oldTheta - penAngle;

    // Both errors should be between [-pi, pi]

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

    deltaTheta = oldTheta - penAngle;
    deltaPhi = oldPhi - armAngle;

    if (Math.abs(penError) < 0.2 && Math.abs(deltaPhi) < 0.1) {
      u = 0.0; // math here
    } else {
      u = k1
          * Math.signum(
              (Math.cos(penAngle) + deltaTheta * deltaTheta / (6.7 * 6.7) - 1) * deltaTheta * Math.cos(penAngle))
          - k2 * deltaPhi;
    }

    // Both errors should be between [-pi, pi]

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