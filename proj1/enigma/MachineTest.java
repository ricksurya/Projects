package enigma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import static enigma.TestUtils.*;


/** The suite of all JUnit tests for the Machine class.
 *  @author Rick Surya
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private MovingRotor iv = new MovingRotor("IV",
            new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", UPPER), "J");
    private MovingRotor iii = new MovingRotor("III",
            new Permutation("(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", UPPER), "V");
    private MovingRotor ii = new MovingRotor("II",
            new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)",
                    UPPER), "E");
    private MovingRotor i = new MovingRotor("I",
            new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)",
                    UPPER), "Q");
    private FixedRotor beta = new FixedRotor("BETA",
            new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER));
    private Reflector b =
            new Reflector("B",
                    new Permutation("(AE) (BN) (CK) "
                            + "(DQ) (FU) (GY) (HW) (IJ) (LO) "
                            + "(MP) (RX) (SZ) (TV)",
                            UPPER));
    private ArrayList<Rotor> allRotors =
            new ArrayList<Rotor>(Arrays.asList(b, beta, i, ii, iii, iv));
    private Machine machine;
    private String[] rotors = {"B", "BETA", "III", "IV", "I"};

    /** Setting machine based on specification */
    private void setMachine(Alphabet alphabet,
                            int numRotors, int pawls,
                            Collection<Rotor> setAllRotors) {
        machine = new Machine(alphabet, numRotors, pawls, allRotors);
    }

    /* ***** TESTS ***** */
    @Test
    public void testInsertRotors() {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(rotors);
        assertEquals("Reflector is wrong",
                machine.getRotor()[0], allRotors.get(0));
        assertEquals("Second rotor is wrong",
                machine.getRotor()[1], allRotors.get(1));
        assertEquals("Third rotor is wrong",
                machine.getRotor()[2], allRotors.get(4));
        assertEquals("Fourth rotor is wrong",
                machine.getRotor()[3], allRotors.get(5));
        assertEquals("Fast rotor is wrong",
                machine.getRotor()[4], allRotors.get(2));
    }

    @Test
    public void testSetRotors() {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(rotors);
        machine.setRotors("AXLE");
        assertEquals("Second rotor has wrong setting",
                'A', machine.getSetting().charAt(1));
        assertEquals("Third rotor has wrong setting",
                'X', machine.getSetting().charAt(2));
        assertEquals("Fourth rotor has wrong setting",
                'L', machine.getSetting().charAt(3));
        assertEquals("Fast rotor has wrong setting",
                'E', machine.getSetting().charAt(4));
    }

    @Test
    public void testConvert() {
        setMachine(UPPER, 5, 3, allRotors);
        machine.insertRotors(rotors);
        machine.setPlugboard(
                new Permutation("(HQ) (EX) (IP) (TR) (BY)", UPPER));
        machine.setRotors("AXLE");
        String hiawathaForward =  "FROMhisshoulderHiawatha".toUpperCase();
        String hiawathaBackward = "QVPQSOKOILPUBKJZPISFXDW".toUpperCase();
        assertEquals("Wrong output",
                "QVPQSOKOILPUBKJZPISFXDW", machine.convert(hiawathaForward));
        machine.setRotors("AXLE");
        assertEquals("Wrong output",
                "FROMhisshoulderHiawatha".toUpperCase(),
                machine.convert(hiawathaBackward));
    }

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1",
                new Permutation("(AB) (CD)", ac));
        Rotor two = new MovingRotor("R2",
                new Permutation("(ABC)", ac), "C");
        Rotor three = new MovingRotor("R3",
                new Permutation("(ABC)", ac), "C");
        Rotor four = new MovingRotor("R4",
                new Permutation("(ABC)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] insertedRotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4,
                3, new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(insertedRotors);
        mach.setRotors(setting);
        assertEquals("AAAA", mach.getSetting());
        mach.convert('A');
        assertEquals("AAAB", mach.getSetting());
        mach.convert('a');
        assertEquals("AAAC", mach.getSetting());
        mach.convert('a');
        assertEquals("AABD", mach.getSetting());
        mach.convert('a');
        assertEquals("AABA", mach.getSetting());
        mach.convert('a');
        assertEquals("AABB", mach.getSetting());
        mach.convert('a');
        assertEquals("AABC", mach.getSetting());
        mach.convert('a');
        assertEquals("AACD", mach.getSetting());
        mach.convert('a');
        assertEquals("Error in double step", "ABDA", mach.getSetting());
    }
}
