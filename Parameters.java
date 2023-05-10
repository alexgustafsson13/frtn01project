public class Parameters implements Cloneable {
	public double k1, k2, phi1, phi2, phispeed;
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}