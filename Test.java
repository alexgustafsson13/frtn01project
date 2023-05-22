import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;

public class Test {
    public static void main(String[] args) {
        try {
                AnalogOut u = new AnalogOut(0);
                AnalogIn a = new AnalogIn(0);
                AnalogIn b = new AnalogIn(1);
                AnalogIn c = new AnalogIn(2);
                AnalogIn d = new AnalogIn(3);
                AnalogIn e = new AnalogIn(4);
                AnalogIn f = new AnalogIn(5);
                AnalogIn g = new AnalogIn(6);
                AnalogIn h = new AnalogIn(7);
            while (true) {
                System.out.println("A:"+a.get());
                System.out.println("B:"+b.get());
                System.out.println("C:"+c.get());
                System.out.println("D:"+d.get());
                System.out.println("E:"+e.get());
                System.out.println("F:"+f.get());
                System.out.println("G:"+g.get());
                System.out.println("H:"+h.get());
                u.set(0.01);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
