import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.stopwatch.Stopwatch;
import components.stopwatch.Stopwatch1;

/**
 * Program to test static methods {@code generateElements} and
 * {@code nextWordOrSeparator}.
 *
 * @author Put your name here
 *
 */
public final class NextWordOrSeparatorTest {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private NextWordOrSeparatorTest() {
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";
        // Ensure charSet is cleared
        charSet.clear();
        // Iterate through each char and add to charSet
        for (int i = 0; i < str.length(); i++) {
            char currChar = str.charAt(i);
            if (!charSet.contains(currChar)) {
                charSet.add(str.charAt(i));
            }
        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        String wordOrSeparator = "";

        if (separators.contains(text.charAt(position))) {
            wordOrSeparator = Character.toString(text.charAt(position));
        } else {
            boolean searching = true;
            int index = position;
            while (searching && index < text.length()) {
                if (separators.contains(text.charAt(index))) {
                    searching = false;
                } else {
                    index++;
                }
            }
            wordOrSeparator = text.substring(position, index);
        }
        return wordOrSeparator;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        /*
         * Define separator characters for test
         */
        final String separatorStr = " \t,.:;";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorStr, separatorSet);
        /*
         * Open input and output streams
         */
        SimpleReader in = new SimpleReader1L();
        SimpleReader inFile = new SimpleReader1L("data/war-and-peace.txt");
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Ask for test cases
         */
        out.println();
        out.print("New test case (y/n)? ");
        String response = in.nextLine();
        while (response.equals("y")) {
            /*
             * Output heading
             */
            out.print("Test case: ");
            String testStr = inFile.nextLine();
            out.println();
            out.println("----Next test case----");
            out.println();
            /*
             * Process test case
             */
            int position = 0;
            Stopwatch clock = new Stopwatch1();
            clock.start();
            while (position < testStr.length()) {
                String token = nextWordOrSeparator(testStr, position,
                        separatorSet);
                if (separatorSet.contains(token.charAt(0))) {
                    out.print("  Separator: <");
                } else {
                    out.print("  Word: <");
                }
                out.println(token + ">");
                position += token.length();
            }
            clock.stop();
            /*
             * Ask user whether to continue
             */
            out.println();
            out.println(
                    "Time elapsed: " + ((double) clock.elapsed()) / 1000 + "s");
            out.print("New test case (y/n)? ");
            response = in.nextLine();
        }
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }
}