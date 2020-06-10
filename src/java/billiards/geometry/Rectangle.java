package billiards.geometry;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.Objects;

public final class Rectangle implements Project {

    public final Interval intervalX;
    public final Interval intervalY;

    private Rectangle(final Interval intervalX, final Interval intervalY) {
        this.intervalX = intervalX;
        this.intervalY = intervalY;
    }

    // Not necessary that x0 <= x1 and y0 <= y1. It gets sorted automatically
    public static Rectangle create(
        final double x0, final double x1, final double y0, final double y1) {
        final Interval intervalX = Interval.create(x0, x1);
        final Interval intervalY = Interval.create(y0, y1);

        return new Rectangle(intervalX, intervalY);
    }

    public Point center() {
        return Point.create(intervalX.center(), intervalY.center());
    }

    public boolean contains(final double x, final double y) {
        return this.intervalX.contains(x) && this.intervalY.contains(y);
    }

    // http://stackoverflow.com/questions/306316/determine-if-two-rectangles-overlap-each-other
    public static boolean intersects(final Rectangle a, final Rectangle b) {
        return a.intervalX.min <= b.intervalX.max && a.intervalX.max >= b.intervalX.min && a.intervalY.min <= b.intervalY.max && a.intervalY.max >= b.intervalY.min;
    }

    // In C++, where we have value types, I wouldn't worry about this, because
    // the optimizer would take care of the copies. But in Java, we have to
    // allocate memory
    @Override
    public Interval project(final Point axis) {
        final Point a = Point.create(this.intervalX.min, this.intervalY.min);

        final double aDot = Point.dot(a, axis);
        double min = aDot;
        double max = aDot;

        final Point b = Point.create(this.intervalX.min, this.intervalY.max);

        final double bDot = Point.dot(b, axis);
        min = Math.min(min, bDot);
        max = Math.max(max, bDot);

        final Point c = Point.create(this.intervalX.max, this.intervalY.max);

        final double cDot = Point.dot(c, axis);
        min = Math.min(min, cDot);
        max = Math.max(max, cDot);

        final Point d = Point.create(this.intervalX.max, this.intervalY.min);

        final double dDot = Point.dot(d, axis);

        min = Math.min(min, dDot);
        max = Math.max(max, dDot);

        return Interval.create(min, max);
    }

    public Interval projectX() {
        return this.intervalX;
    }

    public Interval projectY() {
        return this.intervalY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.intervalX, this.intervalY);
    }

    @Override
    public boolean equals(final Object obj) {
        final Rectangle other = (Rectangle) obj;
        return this.intervalX.equals(other.intervalX) && this.intervalY.equals(other.intervalY);
    }

    @Override
    public String toString() {
        return this.intervalX + ", " + this.intervalY;
    }
}
