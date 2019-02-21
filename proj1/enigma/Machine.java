package enigma;

import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Rick Surya
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _pawls = pawls;
        if (numRotors <= 1 || pawls < 0 || pawls >= numRotors) {
            throw error("numRotors must be > 1 and 0 <= pawls < numRotors");
        }
        _numRotors = numRotors;
        _allRotors = allRotors.toArray(new Rotor[numRotors]);
        _rotors = new Rotor[numRotors];
        _plugboard = new Permutation("", alpha);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.length; j++) {
                String rotorName = rotors[i];
                if (rotorName.equals(_allRotors[j].name().toUpperCase())) {
                    _rotors[i] = _allRotors[j];
                }
            }
        }
        if (rotors.length != _rotors.length) {
            throw error("There is a misnamed rotor.");
        }
        checkInsertAmount();
        checkInsertOrder();
    }

    /** Checks whether the inserted rotors are correct.
     */
    void checkInsertAmount() {
        int[] counter = new int[2];
        for (Rotor r : _rotors) {
            if (r instanceof Reflector) {
                counter[0] += 1;
            } else if (r instanceof  MovingRotor) {
                counter[1] += 1;
            }
        }
        if (counter[0] != 1) {
            throw error("Number of reflectors must be 1.");
        }
        if (counter[1] != numPawls()) {
            throw error("Number of pawls != number of moving rotors.");
        }
        if (!(_rotors[0] instanceof  Reflector)) {
            throw error("First rotor must be a reflector");
        }
    }

    /** Checks whether the order of inserted rotors are correct.
     */
    void checkInsertOrder() {
        for (int i = 1; i <= numPawls(); i++) {
            if (!(_rotors[numRotors() - i] instanceof MovingRotor)) {
                throw error("Moving rotors must be at the right");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("Settings must be a string of #Rotors - 1");
        }

        for (int i = 0; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw error("Settings must be contained in alphabet");
            }
        }

        for (int i = 1; i < numRotors(); i++) {
            _rotors[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        if (!plugboard.checkPairs()) {
            throw error("Permutation for plugboard must be in pairs");
        }
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int result;
        boolean[] canRotate = new boolean[numRotors()];
        canRotate[numRotors() - 1] = true;
        for (int i = numRotors() - 2; i > 0; i--) {
            if (_rotors[i].rotates() && _rotors[i + 1].atNotch()) {
                canRotate[i] = true;
                canRotate[i + 1] = true;
            }
        }
        for (int i = 0; i < canRotate.length; i++) {
            if (canRotate[i]) {
                _rotors[i].advance();
            }
        }

        result = _plugboard.permute(c);
        for (int i = _numRotors - 1; i >= 0; i--) {
            result = _rotors[i].convertForward(result);
        }
        for (int i = 1; i < _numRotors; i++) {
            result  = _rotors[i].convertBackward(result);
        }
        result = _plugboard.permute(result);
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            result += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Returns the current setting of the machine. */
    String getSetting() {
        String result = "";
        for (int i = 0; i < numRotors(); i++) {
            result += _alphabet.toChar(_rotors[i].setting());
        }
        return result;
    }

    /** Returns _rotors of the machine. */
    Rotor[] getRotor() {
        return _rotors;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of pawls. */
    private int _pawls;

    /** Number of rotors. */
    private int _numRotors;
    /** All rotors available in the machine. */
    private Rotor[] _allRotors;

    /** Rotors used in the machine, in order. */
    private Rotor[] _rotors;

    /** Plugboard used in the machine. */
    private Permutation _plugboard;
}
