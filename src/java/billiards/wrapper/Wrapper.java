package billiards.wrapper;

import billiards.codeseq.CodePair;

import com.sun.jna.Native;

import java.util.Optional;

public final class Wrapper {

    static {
        // name of the C++ library
        Native.register("backend");
    }

    // Strings are very nice, because they don't have a size builtin like int or long or whatever
    private static native int load_code_info(String codeSeqString, String initialAnglesString, CCodeInfo cCodeInfo);
    private static native void cleanup_code_info(CCodeInfo cCodeInfo);

    public static Optional<CodeInfo> loadCodeInfo(final CodePair codePair) {

        final String codeSeqString = codePair.sequence.toString();
        final String initialAnglesString = codePair.angles.toString();

        final CCodeInfo cCodeInfo = new CCodeInfo();
        final int rval = load_code_info(codeSeqString, initialAnglesString, cCodeInfo);

        if (rval == 1) {
            final String points = cCodeInfo.points.getString(0);
            final String sinEquations = cCodeInfo.sin_equations.getString(0);
            final String cosEquations = cCodeInfo.cos_equations.getString(0);

            // Only release the resources after we have converted the
            // CCodeInfo to a CodeInfo
            cleanup_code_info(cCodeInfo);

            final CodeInfo codeInfo = new CodeInfo(points, sinEquations, cosEquations);

            return Optional.of(codeInfo);
        } else if (rval == 0) {
            // failure
            return Optional.empty();
        } else {
            throw new RuntimeException("unknown return value " + rval);
        }
    }
}
