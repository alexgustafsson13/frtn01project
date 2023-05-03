public class Control {
  private double sampleTime; //in seconds
  private double penError = 0;
  private double armError = 0;

  private double armRef = 0;
  private double penRef = 0;
  
  private double pi = 3.1415;

  public Control() {
    this.sampleTime = 0.05;
    //set parameters here
  }

  public synchronized double lowerCalculate(double penAngle, double armAngle) {
    penRef = pi;
    penError = (penRef - penAngle) % (2*pi);
    if (penError > pi) {
      penError = penError - 2*pi;
    }

    this.armError = (armRef - armAngle) % (2*pi);
    if (armError > pi) {
      armError = armError - 2*pi;
    }

    //Both errors should be between [-pi, pi]
    
    u = 0; // math here
    return u;
  }

  public synchronized double upperCalculate(double penAngle, double armAngle) {
    penRef = 0;
    penError = (penRef - penAngle) % (2*pi);
    if (penError > pi) {
      penError = penError - 2*pi;
    }

    this.armError = (armRef - armAngle) % (2*pi);
    if (armError > pi) {
      armError = armError - 2*pi;
    }

    //Both errors should be between [-pi, pi]
    
    u = 0; // math here
    return u;
  }

  public synchronized void setArmRef(double angle) {
    this.armRef = angle;
  }

  public synchronized long getHMillis() {
    return (long)(sampleTime * 1000);
  }

}