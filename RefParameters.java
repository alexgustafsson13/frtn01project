public class RefParameters implements Cloneable {
	public double phi1, phi2;
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}