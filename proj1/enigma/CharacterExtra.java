package enigma;

import static enigma.EnigmaException.*;


/** An Alphabet consisting of the Unicode characters in a certain range in
 *  order.
 *  @author Rick Surya
 */
class CharacterExtra extends Alphabet {

    /** An alphabet consisting of all characters between in String ALPHA,
     *  inclusive. */
    CharacterExtra(String alpha) {
        _alpha = alpha;
        max = alpha.length() - 1;
    }

    @Override
    int size() {
        return _alpha.length();
    }

    @Override
    boolean contains(char ch) {
        return _alpha.contains(Character.toString(ch));
    }

    @Override
    char toChar(int index) {
        if (index > max || index < 0) {
            throw error("character index out of range");
        }
        return _alpha.charAt(index);
    }

    @Override
    int toInt(char ch) {
        if (!_alpha.contains(Character.toString(ch))) {
            throw error("character out of range");
        }
        return _alpha.indexOf(Character.toString(ch));
    }

    /** Range of characters in this Alphabet. */
    private String _alpha;

    /** The highest integer for the characters in this Alphabet. */
    private int max;

}
