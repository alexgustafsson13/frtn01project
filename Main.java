

public class Main {
  public static void main(String[] args) {
    //start sim
    SimFurutaPendulum sim = new SimFurutaPendulum(0.0, 0.0);


    Control controller = new Control();
    //GUI gui = new GUI();

    Regul regul = new Regul(controller, /*gui,*/ sim);

    //gui.start();
    regul.start();
  }
}