import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;

public class Test {
    public static void main(String[] args) {
        try {
                AnalogOut u = new AnalogOut(40);
                AnalogIn a = new AnalogIn(41);
                AnalogIn b = new AnalogIn(42);
            while (true) {
                System.out.println("A:"+a.get());
                System.out.println("B:"+b.get());
                u.set(0.01);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
