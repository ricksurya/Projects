package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Rick Surya
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = new ArrayList<Integer>();
        for (char s : notches.toCharArray()) {
            _notches.add(perm.alphabet().toInt(s));
        }
    }

    @Override
    boolean atNotch() {
        return _notches.contains(setting());
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    /**Notches available in the moving rotor. */
    private ArrayList<Integer> _notches;


}
