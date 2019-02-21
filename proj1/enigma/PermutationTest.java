package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Rick Surya
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkAddCycleHelper() {
        String cycle = "";
        String[] expected = {""};
        assertArrayEquals(expected, Permutation.addCycleHelper(cycle));

        cycle = "(abc) (de) (f)";
        expected = new String[] {"abc", "de", "f"};
        assertArrayEquals(expected, Permutation.addCycleHelper(cycle));
    }

    @Test
    public void checkPermute() {
        String cycle = "(ABCD) (EF) (G)";
        perm = new Permutation(cycle, UPPER);
        assertEquals("Wrong permutation", 'E', perm.permute('F'));
        assertEquals("If char is not in cycles, char maps to itself",
                'Z', perm.permute('Z'));
    }

    @Test
    public void checkInvert() {
        String cycle = "(ABCD) (EF) (G)";
        perm = new Permutation(cycle, UPPER);
        assertEquals("Wrong invert", 'C', perm.invert('D'));
        assertEquals("If char is not in cycles, char maps to itself",
                'Z', perm.invert('Z'));
    }

    @Test
    public void checkDerangementPairs() {
        String cycle = "(ABCD) (EF) (G)";
        perm = new Permutation(cycle, UPPER);
        assertFalse("The permutation is not in derangement and in pairs",
                (perm.checkPairs() && perm.derangement()));
        String cycle1 = "(AB) (CD) (EF)";
        perm = new Permutation(cycle1, UPPER);
        assertFalse("The permutation is not in derangement",
                (perm.checkPairs() && perm.derangement()));
        String cycle2 =
                "(AE) (BN) (CK) (DQ) (FU) (GY) "
                        + "(HW) (IJ) (LO) (MP) (RX) (SZ) (TV)";
        perm = new Permutation(cycle2, UPPER);
        assertTrue("The permutation is in derangement and in pairs",
                (perm.checkPairs() && perm.derangement()));
        String cycle3 = "";
        perm = new Permutation(cycle3, UPPER);
        assertTrue("The permutation is empty", perm.checkPairs());
    }
}
