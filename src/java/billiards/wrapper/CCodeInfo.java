package billiards.wrapper;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.List;

// This class must be public for JNA to work
// Make sure the names and orders of the class and fields
// match those in the struct
// The fields here change obviously, so don't make them final
public final class CCodeInfo extends Structure {

    public Pointer points;
    public Pointer sin_equations;
    public Pointer cos_equations;

    @Override
    public List<String> getFieldOrder() {
        return FastList.newListWith("points", "sin_equations", "cos_equations");
    }
}
