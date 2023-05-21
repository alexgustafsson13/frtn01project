public class Parameters implements Cloneable {
	public double k1, k2, phiRef, thetaThresh, phiDot, thetaDot;
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException x) {
			return null;
		}
	}
}