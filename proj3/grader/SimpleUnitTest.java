package grader;

import ucb.junit.textui;


/** Unit tests for the graph package.
 *  @author P. N. Hilfinger
 */
public class SimpleUnitTest {

    /** Run all JUnit tests in the graph package. */
    public static void main(String... ignored) {
        System.exit(textui.runClasses(grader.GraphTest.class,
                                      grader.LabeledGraphTest.class));
    }

}
