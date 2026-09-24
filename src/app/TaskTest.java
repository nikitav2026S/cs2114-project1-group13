package app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests every method and branch of the Task class (the to-do list).
 *
 * @author Ayaan Deshmukh
 * @version 2026.09.24
 */
public class TaskTest {

    private Task task;
    private String maxTitle;
    private String longTitle;

    /**
     * Creates a fresh Task and two test titles before each test:
     * one exactly 100 characters and one 101 characters.
     */
    @Before
    public void setUp() {
        task = new Task();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            builder.append("a");
        }
        maxTitle = builder.toString();
        longTitle = maxTitle + "b";
    }


    /**
     * Tests that a new Task starts with empty lists.
     */
    @Test
    public void testConstructor() {
        assertEquals(0, task.getToDoCount());
        assertEquals(0, task.getCompletedCount());
        assertTrue(task.isEmpty());
        assertTrue(task.getToDoTasks().isEmpty());
        assertTrue(task.getCompletedTasks().isEmpty());
    }


    /**
     * Tests adding a normal title.
     */
    @Test
    public void testAddTaskValid() {
        assertTrue(task.addTask("Read chapter 4"));
        assertEquals(1, task.getToDoCount());
        assertEquals("Read chapter 4", task.getTask(0));
        assertFalse(task.isEmpty());
    }


    /**
     * Tests that extra whitespace is trimmed when adding.
     */
    @Test
    public void testAddTaskTrims() {
        assertTrue(task.addTask("   Gym   "));
        assertEquals("Gym", task.getTask(0));
    }


    /**
     * Tests that a null title is rejected.
     */
    @Test
    public void testAddTaskNull() {
        assertFalse(task.addTask(null));
        assertEquals(0, task.getToDoCount());
    }


    /**
     * Tests that empty and whitespace-only titles are rejected.
     */
    @Test
    public void testAddTaskBlank() {
        assertFalse(task.addTask(""));
        assertFalse(task.addTask("     "));
        assertEquals(0, task.getToDoCount());
    }


    /**
     * Tests that a title over 100 characters is cut to 100.
     */
    @Test
    public void testAddTaskTooLong() {
        assertTrue(task.addTask(longTitle));
        assertEquals(100, task.getTask(0).length());
        assertEquals(maxTitle, task.getTask(0));
    }


    /**
     * Tests that a title of exactly 100 characters is kept whole.
     */
    @Test
    public void testAddTaskExactlyMax() {
        assertTrue(task.addTask(maxTitle));
        assertEquals(maxTitle, task.getTask(0));
    }


    /**
     * Tests editing a task at a valid index, including trimming.
     */
    @Test
    public void testEditTaskValid() {
        task.addTask("Old title");
        assertTrue(task.editTask(0, "  New title  "));
        assertEquals("New title", task.getTask(0));
        assertEquals(1, task.getToDoCount());
    }


    /**
     * Tests that an edited title over 100 characters is cut to 100.
     */
    @Test
    public void testEditTaskTooLong() {
        task.addTask("Old title");
        assertTrue(task.editTask(0, longTitle));
        assertEquals(maxTitle, task.getTask(0));
    }


    /**
     * Tests that editing a negative index fails.
     */
    @Test
    public void testEditTaskNegativeIndex() {
        task.addTask("Old title");
        assertFalse(task.editTask(-1, "New title"));
        assertEquals("Old title", task.getTask(0));
    }


    /**
     * Tests that editing an index equal to the size fails.
     */
    @Test
    public void testEditTaskIndexTooHigh() {
        task.addTask("Old title");
        assertFalse(task.editTask(1, "New title"));
        assertEquals("Old title", task.getTask(0));
    }


    /**
     * Tests removing a task at a valid index.
     */
    @Test
    public void testRemoveTaskValid() {
        task.addTask("First");
        task.addTask("Second");
        assertEquals("First", task.removeTask(0));
        assertEquals(1, task.getToDoCount());
        assertEquals("Second", task.getTask(0));
        assertEquals(0, task.getCompletedCount());
    }


    /**
     * Tests that removing a negative index returns null.
     */
    @Test
    public void testRemoveTaskNegativeIndex() {
        task.addTask("First");
        assertNull(task.removeTask(-1));
        assertEquals(1, task.getToDoCount());
    }


    /**
     * Tests that removing an index equal to the size returns null.
     */
    @Test
    public void testRemoveTaskIndexTooHigh() {
        task.addTask("First");
        assertNull(task.removeTask(1));
        assertEquals(1, task.getToDoCount());
    }


    /**
     * Tests that completing a task moves it to the completed list.
     */
    @Test
    public void testCompleteTaskValid() {
        task.addTask("First");
        task.addTask("Second");
        assertEquals("First", task.completeTask(0));
        assertEquals(1, task.getToDoCount());
        assertEquals(1, task.getCompletedCount());
        assertEquals("First", task.getCompletedTasks().get(0));
        assertEquals("Second", task.getTask(0));
    }


    /**
     * Tests that completing a negative index returns null.
     */
    @Test
    public void testCompleteTaskNegativeIndex() {
        task.addTask("First");
        assertNull(task.completeTask(-1));
        assertEquals(1, task.getToDoCount());
        assertEquals(0, task.getCompletedCount());
    }


    /**
     * Tests that completing an index equal to the size returns null.
     */
    @Test
    public void testCompleteTaskIndexTooHigh() {
        task.addTask("First");
        assertNull(task.completeTask(1));
        assertEquals(1, task.getToDoCount());
        assertEquals(0, task.getCompletedCount());
    }


    /**
     * Tests getTask for valid, negative, and too-high indexes.
     */
    @Test
    public void testGetTask() {
        task.addTask("First");
        assertEquals("First", task.getTask(0));
        assertNull(task.getTask(-1));
        assertNull(task.getTask(1));
    }


    /**
     * Tests that isEmpty goes back to true after the last task leaves.
     */
    @Test
    public void testIsEmptyAfterComplete() {
        task.addTask("Only task");
        assertFalse(task.isEmpty());
        task.completeTask(0);
        assertTrue(task.isEmpty());
    }


    /**
     * Tests that getTodoTasks returns tasks in the order added.
     */
    @Test
    public void testGetTodoTasks() {
        task.addTask("First");
        task.addTask("Second");
        assertEquals(2, task.getToDoTasks().size());
        assertEquals("First", task.getToDoTasks().get(0));
        assertEquals("Second", task.getToDoTasks().get(1));
    }


    /**
     * Tests that getCompletedTasks returns tasks in completion order.
     */
    @Test
    public void testGetCompletedTasks() {
        task.addTask("First");
        task.addTask("Second");
        task.completeTask(1);
        task.completeTask(0);
        assertEquals(2, task.getCompletedTasks().size());
        assertEquals("Second", task.getCompletedTasks().get(0));
        assertEquals("First", task.getCompletedTasks().get(1));
    }


    // ----------------------------------------------------------
    // Bad input cases
    // ----------------------------------------------------------

    /**
     * Tests that tabs and newlines alone count as a blank title.
     */
    @Test
    public void testAddTaskOnlyTabsAndNewlines() {
        assertFalse(task.addTask("\t\n  \t"));
        assertEquals(0, task.getToDoCount());
    }


    /**
     * Tests that a long title with padding is trimmed before it
     * is cut, so the padding does not use up the 100 characters.
     */
    @Test
    public void testAddTaskLongWithPadding() {
        assertTrue(task.addTask("     " + longTitle + "     "));
        assertEquals(maxTitle, task.getTask(0));
    }


    /**
     * Tests that a rejected add does not disturb existing tasks.
     */
    @Test
    public void testAddTaskBadInputKeepsList() {
        task.addTask("First");
        assertFalse(task.addTask(null));
        assertFalse(task.addTask("   "));
        assertEquals(1, task.getToDoCount());
        assertEquals("First", task.getTask(0));
    }


    /**
     * Tests that titles with symbols and numbers are accepted as-is.
     */
    @Test
    public void testAddTaskSpecialCharacters() {
        assertTrue(task.addTask("Buy eggs & milk @ 5pm! #2"));
        assertEquals("Buy eggs & milk @ 5pm! #2", task.getTask(0));
    }


    /**
     * Tests that editing to a null or blank title is rejected.
     */
    @Test
    public void testEditTaskBlank() {
        task.addTask("Old title");
        assertFalse(task.editTask(0, null));
        assertFalse(task.editTask(0, ""));
        assertFalse(task.editTask(0, " \t "));
        assertEquals("Old title", task.getTask(0));
    }


    /**
     * Tests that every index-based method handles an empty list.
     */
    @Test
    public void testEmptyListIndexZero() {
        assertNull(task.getTask(0));
        assertNull(task.removeTask(0));
        assertNull(task.completeTask(0));
        assertFalse(task.editTask(0, "New title"));
        assertEquals(0, task.getCompletedCount());
    }


    /**
     * Tests extreme index values on every index-based method.
     */
    @Test
    public void testExtremeIndexes() {
        task.addTask("First");
        int low = Integer.MIN_VALUE;
        int high = Integer.MAX_VALUE;
        assertNull(task.getTask(low));
        assertNull(task.getTask(high));
        assertNull(task.removeTask(low));
        assertNull(task.removeTask(high));
        assertNull(task.completeTask(low));
        assertNull(task.completeTask(high));
        assertFalse(task.editTask(low, "New title"));
        assertFalse(task.editTask(high, "New title"));
        assertEquals(1, task.getToDoCount());
        assertEquals("First", task.getTask(0));
    }


    /**
     * Tests that an index that was valid becomes invalid once the
     * list shrinks, so a task cannot be completed twice.
     */
    @Test
    public void testCompleteSameIndexTwice() {
        task.addTask("Only task");
        assertEquals("Only task", task.completeTask(0));
        assertNull(task.completeTask(0));
        assertEquals(1, task.getCompletedCount());
    }


    /**
     * Tests that a removed task cannot be removed again.
     */
    @Test
    public void testRemoveSameIndexTwice() {
        task.addTask("Only task");
        assertEquals("Only task", task.removeTask(0));
        assertNull(task.removeTask(0));
        assertEquals(0, task.getToDoCount());
    }
}