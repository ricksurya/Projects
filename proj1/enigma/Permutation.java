package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Rick Surya
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        addCycle(cycles);
        if (checkRepeat()) {
            throw error("Characters may not occur twice");
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        int l = 0;
        if (_cycles != null) {
            l = _cycles.length;
        }
        String[] newCycle = addCycleHelper(cycle);
        int size = newCycle.length + l;
        String[] newCycles = new String[size];
        if (_cycles !=  null) {
            System.arraycopy(_cycles, 0, newCycles, 0, l);
        }
        System.arraycopy(newCycle, 0, newCycles, l, newCycle.length);
        _cycles = newCycles;
    }

    /** Splits ) ( instances to return an array of strings of permutations.
     * @param cycle is the string to be split. */
    static String[] addCycleHelper(String cycle) {
        cycle = cycle.replaceAll("\\s", "");
        cycle = cycle.replace(")(", "-");
        String[] newCycle = cycle.split("-");
        newCycle[0] = newCycle[0].replace("(", "");
        newCycle[newCycle.length - 1] =
                newCycle[newCycle.length - 1].replace(")", "");
        return newCycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int intP = wrap(p);
        char charP = _alphabet.toChar(intP);
        char charResult = permute(charP);
        return _alphabet.toInt(charResult);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int intC = wrap(c);
        char charC = _alphabet.toChar(intC);
        char charResult = invert(charC);
        return _alphabet.toInt(charResult);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char result = p;
        String s = String.valueOf(p);
        for (int i = 0; i < _cycles.length; i++) {
            if (_cycles[i].contains(s)) {
                int index = _cycles[i].indexOf(s);
                if (index == _cycles[i].length() - 1) {
                    result = _cycles[i].charAt(0);
                } else if (_cycles[i].length() > 1) {
                    result = _cycles[i].charAt(index + 1);
                }
            }
        }
        return result;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char result = c;
        String s = String.valueOf(c);
        for (int i = 0; i < _cycles.length; i++) {
            if (_cycles[i].contains(s)) {
                int index = _cycles[i].indexOf(s);
                if (index == 0) {
                    result = _cycles[i].charAt(_cycles[i].length() - 1);
                } else {
                    result = _cycles[i].charAt(index - 1);
                }
            }
        }
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int i = 0;
        for (String c : _cycles) {
            if (c.length() == 1) {
                return false;
            }
            i += c.length();
        }
        return i == _alphabet.size();
    }

    /** Return true iff this permutation is a derangement
     *  and values are in pairs.
     */
    boolean checkPairs() {
        for (String c : _cycles) {
            if (c.length() != 2 && c.length() != 0) {
                return false;
            }
        }
        return true;
    }

    /** Check for repeats of character in cycles.
     * @return returns true iff no repeats. */
    boolean checkRepeat() {
        int[] log = new int[_alphabet.size()];
        for (String s : _cycles) {
            for (int i = 0; i < s.length(); i++) {
                log[_alphabet.toInt(s.charAt(i))] += 1;
            }
        }
        for (int i : log) {
            if (i > 1) {
                return true;
            }
        }
        return false;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Permutation cycles of this permutation. */
    private String[] _cycles;

}
