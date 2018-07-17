package billiards.geometry;

import org.eclipse.collections.api.list.ImmutableList;

import java.util.Objects;

// Directed line segment from start -> end
public final class LineSegment implements Project {

    public final Point start;
    public final Point end;

    private LineSegment(final Point start, final Point end) {
        this.start = start;
        this.end = end;
    }

    public static LineSegment create(final ImmutableList<Point> points) {

        if (points.size() != 2) {
            throw new RuntimeException("points do not have size 2 in LineSegment");
        }

        final Point start = points.get(0);
        final Point end = points.get(1);

        return new LineSegment(start, end);
    }

    public boolean intersects(final ConvexPolygon poly) {
        return !(poly.separatingAxis(this) || this.separatingAxis(poly));
    }

    // The SAT also works for a line segment
    public boolean intersects(final Rectangle rect) {
        final Interval rectProjectX = rect.projectX();
        final Interval lineProjectX = this.projectX();

        if (!Interval.intersects(rectProjectX, lineProjectX)) {
            return false;
        }

        final Interval rectProjectY = rect.projectY();
        final Interval lineProjectY = this.projectY();

        if (!Interval.intersects(rectProjectY, lineProjectY)) {
            return false;
        }

        return !this.separatingAxis(rect);
    }

    public boolean separatingAxis(final Project figure) {
        // Vector from start to end
        // The order and perpendicular vector are rather arbitrary
        // as long as they lie on the same line
        final Point segment = this.end.sub(this.start);
        final Point axis = Point.create(-segment.y, segment.x);

        final Interval figureProject = figure.project(axis);
        final Interval lineProject = this.project(axis);

        // If they do not intersect, there is a separating axis. If they
        // do intersect, there is not a separating axis
        return !Interval.intersects(figureProject, lineProject);
    }

    @Override
    public Interval project(final Point axis) {
        final double startDot = Point.dot(this.start, axis);
        final double endDot = Point.dot(this.end, axis);

        return Interval.create(startDot, endDot);
    }

    public Interval projectX() {
        return Interval.create(this.start.x, this.end.x);
    }

    public Interval projectY() {
        return Interval.create(this.start.y, this.end.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }

    @Override
    public boolean equals(final Object obj) {
        final LineSegment other = (LineSegment) obj;
        return this.start.equals(other.start) && this.end.equals(other.end);
    }

    @Override
    public String toString() {
        return this.start + " -> " + this.end;
    }
}
