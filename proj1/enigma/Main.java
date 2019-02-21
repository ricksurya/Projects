package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Rick Surya
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        _machine = readConfig();
        if (!_input.hasNext("\\*")) {
            throw error("No setting");
        }
        String next = _input.nextLine().toUpperCase();
        readInput(next);
        boolean first = true;
        while (_input.hasNext()) {
            if (!first) {
                readInput(next);
            }
            first = false;
            next = _input.nextLine().toUpperCase();
            while (!next.contains("*")) {
                printMessageLine(next);
                if (!_input.hasNextLine()) {
                    break;
                }
                next = _input.nextLine().toUpperCase();
            }
        }
    }

    /** Set up the machine based on input.
     * @param input is supposed to be the line of input config. */
    private void readInput(String input) {
        Scanner s = new Scanner(input);
        s.next();
        String[] rotors = new String[_machine.numRotors()];
        String settings;
        try {
            for (int i = 0; i < _machine.numRotors(); i++) {
                rotors[i] = s.next();
            }
            settings = s.next();
        } catch (NoSuchElementException excp) {
            throw error("Wrong input configuration");
        }
        String cycles = "";
        while (s.hasNext("\\(..\\)")) {
            cycles += s.next();
        }
        _machine.insertRotors(rotors);
        _machine.setRotors(settings);
        _machine.setPlugboard(new Permutation(cycles, _alphabet));
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            int numRotors;
            int pawls;
            String alpha = _config.next();
            char firstAlpha = alpha.charAt(0);
            char lastAlpha = alpha.charAt(alpha.length() - 1);
            if (alpha.length() == 3 && alpha.charAt(1) == '-') {
                _alphabet = new CharacterRange(firstAlpha, lastAlpha);
            } else {
                _alphabet = new CharacterExtra(alpha);
            }
            try {
                numRotors = Integer.parseInt(_config.next());
                pawls = Integer.parseInt(_config.next());
            } catch (NumberFormatException e) {
                throw error("numRotors and pawls must be an integer");
            }
            if (numRotors < 2 || pawls < 0 || pawls >= numRotors) {
                throw error("numRotors and pawls is wrong");
            }
            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            if (numRotors > allRotors.size()) {
                throw error("numRotors must be <= allRotors.length");
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            Rotor result = null;
            String name = _config.next();
            String rotorType = _config.next();
            char type = rotorType.charAt(0);
            String cycles = "";
            while (_config.hasNext("\\(.*\\)")) {
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (type == 'M') {
                String notches = rotorType.substring(1);
                result = new MovingRotor(name, perm, notches);
            } else if (type == 'N') {
                result = new FixedRotor(name, perm);
            } else if (type == 'R') {
                result = new Reflector(name, perm);
            } else {
                throw error("Rotor must either be moving, fixed, or reflector");
            }
            return result;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.length() == 0) {
            _output.println();
        } else {
            int i = 0;
            boolean first = true;
            String result = "";
            String temp = "";
            msg = msg.replaceAll("\\s", "");
            for (int j = 0; j < msg.length(); j++) {
                if (i == 0 && !first) {
                    result += _machine.convert(temp);
                    temp = "";
                    result += " ";
                }
                temp += msg.charAt(j);
                i = (i + 1) % 5;
                first = false;
            }
            result += _machine.convert(temp);
            result.trim();
            _output.println(result);
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** The machine used. */
    private Machine _machine;
}
