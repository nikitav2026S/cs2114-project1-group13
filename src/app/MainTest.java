package app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests every method and branch of Main by typing fake input into
 * the menu and checking what gets printed.
 *
 * @author Ayaan Deshmukh
 * @version 2026.09.24
 */
public class MainTest {

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream output;
    private String longTitle;

    /**
     * Saves the real console, redirects output, and builds a
     * 101-character title before each test.
     */
    @Before
    public void setUp() {
        originalIn = System.in;
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            builder.append("a");
        }
        longTitle = builder.toString();
    }


    /**
     * Puts the real console back after each test.
     */
    @After
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }


    /**
     * Sets up typed input, always ending with 0 to exit the menu.
     *
     * @param lines
     *            the lines the user types, in order
     */
    private void typeInput(String... lines) {
        StringBuilder input = new StringBuilder();
        for (String line : lines) {
            input.append(line).append("\n");
        }
        input.append("0\n");
        System.setIn(new ByteArrayInputStream(
            input.toString().getBytes()));
    }


    /**
     * Runs a new Main with the given typed input.
     *
     * @param lines
     *            the lines the user types, in order
     * @return everything the program printed
     */
    private String runWith(String... lines) {
        typeInput(lines);
        new Main().run();
        return output.toString();
    }


    // ----------------------------------------------------------
    // main, run, menu
    // ----------------------------------------------------------

    /**
     * Tests the static main method prints the instructions, the
     * menu, and the goodbye message.
     */
    @Test
    public void testMain() {
        typeInput();
        Main.main(new String[0]);
        String out = output.toString();
        assertTrue(out.contains("=== How to Use To-Do List ==="));
        assertTrue(out.contains("0. Exit to-do list"));
        assertTrue(out.contains("Have fun getting your tasks done!"));
    }


    /**
     * Tests menu numbers that are not options.
     */
    @Test
    public void testMenuOutOfRange() {
        String out = runWith("7", "-1", "100");
        assertTrue(out.contains("Please enter a number between 0 and 6."));
    }


    /**
     * Tests menu input that is not a whole number.
     */
    @Test
    public void testMenuNotANumber() {
        String out = runWith("abc", "", "2.5", "  ");
        assertTrue(out.contains("Please enter a whole number."));
    }


    /**
     * Tests that a number with spaces around it still works.
     */
    @Test
    public void testMenuNumberWithSpaces() {
        String out = runWith("  5  ");
        assertTrue(out.contains("To-Do:"));
        assertFalse(out.contains("Please enter a whole number."));
    }


    // ----------------------------------------------------------
    // Option 1: add
    // ----------------------------------------------------------

    /**
     * Tests adding a valid task, with spaces trimmed.
     */
    @Test
    public void testAddValid() {
        String out = runWith("1", "  Read  ", "5");
        assertTrue(out.contains("Added: Read"));
        assertTrue(out.contains("1. Read"));
    }


    /**
     * Tests adding empty and whitespace-only titles.
     */
    @Test
    public void testAddBlank() {
        String out = runWith("1", "", "1", "   ", "5");
        assertTrue(out.contains("Task can't be blank."));
        assertFalse(out.contains("Added:"));
        assertTrue(out.contains("Nothing yet, you are all caught up!"));
    }


    /**
     * Tests adding a title over 100 characters.
     */
    @Test
    public void testAddTooLong() {
        String out = runWith("1", longTitle);
        assertTrue(out.contains("Your task title is too long"));
        assertFalse(out.contains("Added:"));
    }


    // ----------------------------------------------------------
    // Option 2: edit
    // ----------------------------------------------------------

    /**
     * Tests editing with no tasks.
     */
    @Test
    public void testEditEmpty() {
        String out = runWith("2");
        assertTrue(out.contains("You have no tasks yet."));
    }


    /**
     * Tests editing a task after bad picks: too low, too high, and
     * not a number.
     */
    @Test
    public void testEditValid() {
        String out = runWith("1", "Old", "2", "0", "2", "x", "1",
            "  New  ", "5");
        assertTrue(out.contains("Please enter a number between 1 and 1."));
        assertTrue(out.contains("Please enter a whole number."));
        assertTrue(out.contains("The task has been updated."));
        assertTrue(out.contains("1. New"));
    }


    /**
     * Tests editing a task to a blank title.
     */
    @Test
    public void testEditBlank() {
        String out = runWith("1", "Old", "2", "1", "   ", "5");
        assertTrue(out.contains("The task title can't be blank."));
        assertFalse(out.contains("The task has been updated."));
        assertTrue(out.contains("1. Old"));
    }


    /**
     * Tests editing a task to a title over 100 characters.
     */
    @Test
    public void testEditTooLong() {
        String out = runWith("1", "Old", "2", "1", longTitle);
        assertTrue(out.contains("The task title is too long"));
        assertFalse(out.contains("The task has been updated."));
    }


    // ----------------------------------------------------------
    // Option 3: complete
    // ----------------------------------------------------------

    /**
     * Tests completing with no tasks.
     */
    @Test
    public void testCompleteEmpty() {
        String out = runWith("3");
        assertTrue(out.contains("You have no tasks yet."));
        assertFalse(out.contains("Good job"));
    }


    /**
     * Tests completing a task after an out-of-range pick.
     */
    @Test
    public void testCompleteValid() {
        String out = runWith("1", "Read", "3", "9", "1");
        assertTrue(out.contains("Please enter a number between 1 and 1."));
        assertTrue(out.contains("Good job completing Read!"));
        assertTrue(out.contains("Points: 1"));
        assertTrue(out.contains("Streak: 1"));
    }


    // ----------------------------------------------------------
    // Option 4: remove
    // ----------------------------------------------------------

    /**
     * Tests removing with no tasks.
     */
    @Test
    public void testRemoveEmpty() {
        String out = runWith("4");
        assertTrue(out.contains("You have no tasks yet."));
        assertFalse(out.contains("Removed:"));
    }


    /**
     * Tests removing the second of two tasks.
     */
    @Test
    public void testRemoveValid() {
        String out = runWith("1", "Read", "1", "Gym", "4", "2");
        assertTrue(out.contains("Removed: Gym"));
    }


    // ----------------------------------------------------------
    // Option 5: view tasks
    // ----------------------------------------------------------

    /**
     * Tests viewing when both lists are empty.
     */
    @Test
    public void testViewBothEmpty() {
        String out = runWith("5");
        assertTrue(out.contains("Nothing yet, you are all caught up!"));
        assertTrue(out.contains("Nothing so far, you need to lock in!"));
    }


    /**
     * Tests viewing when every task has been completed.
     */
    @Test
    public void testViewAllCaughtUp() {
        String out = runWith("1", "Read", "3", "1", "5");
        assertTrue(out.contains("Nothing yet, you are all caught up!"));
        assertTrue(out.contains("R\u0336e\u0336a\u0336d\u0336"));
    }


    /**
     * Tests viewing with tasks in both lists.
     */
    @Test
    public void testViewBothLists() {
        String out = runWith("1", "Read", "1", "Gym", "3", "1", "5");
        assertTrue(out.contains("1. Gym"));
        assertTrue(out.contains("R\u0336e\u0336a\u0336d\u0336"));
    }


    // ----------------------------------------------------------
    // Option 6: points and streak
    // ----------------------------------------------------------

    /**
     * Tests the status screen with a safe streak.
     */
    @Test
    public void testStatusSafe() {
        String out = runWith("1", "Read", "3", "1", "6");
        assertTrue(out.contains("Current streak: 1 day(s)"));
        assertTrue(out.contains("Longest streak: 1 day(s)"));
        assertFalse(out.contains("Your streak is about to end"));
    }


    /**
     * Tests the status screen warning when the last completion was
     * yesterday. Main has no way to set that up through the menu,
     * so the test swaps in its own Points object.
     *
     * @throws Exception
     *             if the points field cannot be replaced
     */
    @Test
    public void testStatusInJeopardy() throws Exception {
        Points points = new Points();
        points.recordCompletion(LocalDate.now().minusDays(1));

        typeInput("6");
        Main main = new Main();
        Field field = Main.class.getDeclaredField("points");
        field.setAccessible(true);
        field.set(main, points);
        main.run();

        String out = output.toString();
        assertTrue(out.contains("Current streak: 1 day(s)"));
        assertTrue(out.contains("Your streak is about to end"));
    }
}