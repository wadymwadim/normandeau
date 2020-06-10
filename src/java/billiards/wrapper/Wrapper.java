package billiards.wrapper;

import billiards.codeseq.CodePair;
import billiards.codeseq.CodeSequence;
import billiards.codeseq.InitialAngles;
import billiards.math.CoverSquare;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

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

    private static native Pointer check_square(long numerX, long numerY, long denom, String code_seq, String angles, String coverDir);
    private static native void free_string(Pointer ptr);

    public static Optional<String> checkSquare(final CoverSquare square, final CodeSequence codeSeq, final InitialAngles angles, final String coverDir) {

        final long numerX = square.numerX;
        final long numerY = square.numerY;
        final long denom = 1 << square.denom;

        final Pointer ptr = check_square(numerX, numerY, denom, codeSeq.toString(), angles.toString(), coverDir);

        final String string = ptr.getString(0);

        free_string(ptr);

        if (string == null) {
            return Optional.empty();
        } else {
            return Optional.of(string);
        }
    }
}
