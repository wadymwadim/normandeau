package billiards.viewer;

import billiards.codeseq.CodePair;
import billiards.codeseq.TriplePair;
import billiards.math.CoverSquare;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import java.util.Map;

import javafx.scene.paint.Color;

public final class HashTriple {

    private final MutableMap<CoverSquare, CodePair> stableMap;
    private final MutableMap<CoverSquare, TriplePair> tripleMap;
    private final MutableMap<CoverSquare, Color> colorMap;

    public HashTriple() {
        stableMap = new UnifiedMap<>();
        tripleMap = new UnifiedMap<>();
        colorMap = new UnifiedMap<>();
    }

    public void addStables(final Map<CoverSquare, CodePair> otherMap, final Color color) {
        this.stableMap.putAll(otherMap);
        for (final CoverSquare rect : otherMap.keySet()) {
            this.colorMap.put(rect, color);
        }
    }

    public void addTriples(final Map<CoverSquare, TriplePair> otherMap, final Color color) {
        this.tripleMap.putAll(otherMap);
        for (final CoverSquare rect : otherMap.keySet()) {
            this.colorMap.put(rect, color);
        }
    }

    public void clear() {
        stableMap.clear();
        tripleMap.clear();
        colorMap.clear();
    }

    public MutableList<CoverSquare> stableEntrySet() {
        final MutableList<CoverSquare> entries = new FastList<>();
        stableMap.entrySet().forEach(rect -> entries.add(rect.getKey()));
        return entries;
    }

    public MutableList<CoverSquare> tripleEntrySet() {
        final MutableList<CoverSquare> entries = new FastList<>();
        tripleMap.entrySet().forEach(rect -> entries.add(rect.getKey()));
        return entries;
    }

    public CodePair getStable(final CoverSquare rect) {
        return stableMap.get(rect);
    }

    public TriplePair getTriple(final CoverSquare rect) {
        return tripleMap.get(rect);
    }

    public Color getColor(final CoverSquare rect) {
        return colorMap.get(rect);
    }

    public void put(final CoverSquare rect, final CodePair stable) {
        stableMap.put(rect, stable);
    }

    public void put(final CoverSquare rect, final TriplePair triple) {
        tripleMap.put(rect, triple);
    }

    public void put(final CoverSquare rect, final Color color) {
        colorMap.put(rect, color);
    }
}
