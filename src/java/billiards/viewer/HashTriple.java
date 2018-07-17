package billiards.viewer;

import billiards.codeseq.CodePair;
import billiards.codeseq.TriplePair;
import billiards.geometry.Rectangle;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import java.util.Map;

import javafx.scene.paint.Color;

public final class HashTriple {

    private final MutableMap<Rectangle, CodePair> stableMap;
    private final MutableMap<Rectangle, TriplePair> tripleMap;
    private final MutableMap<Rectangle, Color> colorMap;

    public HashTriple() {
        stableMap = new UnifiedMap<>();
        tripleMap = new UnifiedMap<>();
        colorMap = new UnifiedMap<>();
    }

    public void addStables(final Map<Rectangle, CodePair> otherMap, final Color color) {
        this.stableMap.putAll(otherMap);
        for (final Rectangle rect : otherMap.keySet()) {
            this.colorMap.put(rect, color);
        }
    }

    public void addTriples(final Map<Rectangle, TriplePair> otherMap, final Color color) {
        this.tripleMap.putAll(otherMap);
        for (final Rectangle rect : otherMap.keySet()) {
            this.colorMap.put(rect, color);
        }
    }

    public void clear() {
        stableMap.clear();
        tripleMap.clear();
        colorMap.clear();
    }

    public MutableList<Rectangle> stableEntrySet() {
        final MutableList<Rectangle> entries = new FastList<>();
        stableMap.entrySet().forEach(rect -> entries.add(rect.getKey()));
        return entries;
    }

    public MutableList<Rectangle> tripleEntrySet() {
        final MutableList<Rectangle> entries = new FastList<>();
        tripleMap.entrySet().forEach(rect -> entries.add(rect.getKey()));
        return entries;
    }

    public CodePair getStable(final Rectangle rect) {
        return stableMap.get(rect);
    }

    public TriplePair getTriple(final Rectangle rect) {
        return tripleMap.get(rect);
    }

    public Color getColor(final Rectangle rect) {
        return colorMap.get(rect);
    }

    public void put(final Rectangle rect, final CodePair stable) {
        stableMap.put(rect, stable);
    }

    public void put(final Rectangle rect, final TriplePair triple) {
        tripleMap.put(rect, triple);
    }

    public void put(final Rectangle rect, final Color color) {
        colorMap.put(rect, color);
    }
}
