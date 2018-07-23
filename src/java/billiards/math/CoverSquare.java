package billiards.math;

public class CoverSquare {
	final long numerX;
	final long numerY;
	final byte denom;
	
	// the value is to be interpreted as num / 2^denom
	private CoverSquare(final long numerX, final long numerY, final byte denomPower) {
		this.numerX = numerX;
		this.numerY = numerY;
		this.denom = denomPower;
	}
	
	// initialize (1/2, 1/2) as our center
	public static CoverSquare initial() {
		return new CoverSquare(1, 1, (byte) 1);
	}
	
	public CoverSquare[] subdivide() {
		// order of return: 
		// | 1 2 |
		// | 3 4 |
		final byte newDenom = (byte) (denom + 1);
		final long newNum1X = (2 * numerX) + 1;
		final long newNum2X = (2 * numerX) - 1;
		final long newNum1Y = (2 * numerY) + 1;
		final long newNum2Y = (2 * numerY) - 1;
		final CoverSquare[] fracs = 
			{new CoverSquare(newNum1X, newNum1Y, newDenom), new CoverSquare(newNum2X, newNum1Y, newDenom),
			 new CoverSquare(newNum1X, newNum2Y, newDenom), new CoverSquare(newNum2X, newNum2Y, newDenom)};
		return fracs;
	}
	
	public double radius() {
		return 1 / Math.pow(2, denom);
	}
	
	// return is { Xmin, Xmax, Ymin, Ymax} !
	public double[] bounds() {
		final double rad = Math.PI * this.radius() / 2;
		final double centerX = (Math.PI / 2) * (1 - numerX * this.radius()); //TODO !!
		final double centerY = (Math.PI / 2) * numerY * this.radius();
		final double[] bounds = {centerX - rad, centerX + rad, centerY - rad, centerY + rad};
		return bounds;
	}
	
	public boolean contains(double x, double y) {
		final double[] b = this.bounds();
		return b[0] < x && x < b[1] && b[2] < y && y < b[3];
	}
	
	/*  Could try this too:
	private static long intpow(final long base, final byte exp) {
		if (exp < 1) {
			return 1;
		} else if (exp == (byte) 1) {
			return base;
		} else if ((exp % 2) == 1) {
			return base * intpow(base, (byte) (exp - 1));
		} else {
			return intpow(base * base, (byte) (exp / 2));
		}
	} */
}
