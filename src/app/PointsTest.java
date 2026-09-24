package app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests every method and branch of the Points class.
 * All dates are relative to today, because getCurrentStreak()
 * re-checks the streak against the real current date.
 *
 * @author Ayaan Deshmukh
 * @version 2026.09.24
 */
public class PointsTest {

    private Points points;
    private LocalDate today;

    /**
     * Creates a fresh Points object and grabs today's date.
     */
    @Before
    public void setUp() {
        points = new Points();
        today = LocalDate.now();
    }


    /**
     * Tests the starting values.
     */
    @Test
    public void testConstructor() {
        assertEquals(0, points.getTotalPoints());
        assertEquals(0, points.getCurrentStreak());
        assertEquals(0, points.getLongestStreak());
        assertTrue(points.isStreakSafe());
    }


    /**
     * Tests that awardPoints adds one point each time.
     */
    @Test
    public void testAwardPoints() {
        points.awardPoints();
        assertEquals(1, points.getTotalPoints());
        points.awardPoints();
        assertEquals(2, points.getTotalPoints());
    }


    /**
     * Tests earning a positive amount.
     */
    @Test
    public void testEarnPointsPositive() {
        points.earnPoints(5);
        assertEquals(5, points.getTotalPoints());
    }


    /**
     * Tests that zero and negative amounts are ignored.
     */
    @Test
    public void testEarnPointsZeroAndNegative() {
        points.earnPoints(3);
        points.earnPoints(0);
        points.earnPoints(-10);
        points.earnPoints(Integer.MIN_VALUE);
        assertEquals(3, points.getTotalPoints());
    }


    /**
     * Tests the first completion using the real date.
     */
    @Test
    public void testRecordCompletionToday() {
        points.recordCompletionToday();
        assertEquals(1, points.getCurrentStreak());
        assertEquals(1, points.getLongestStreak());
        assertTrue(points.isStreakSafe());
    }


    /**
     * Tests that completions on back-to-back days build a streak.
     */
    @Test
    public void testRecordCompletionConsecutiveDays() {
        points.recordCompletion(today.minusDays(2));
        points.recordCompletion(today.minusDays(1));
        points.recordCompletion(today);
        assertEquals(3, points.getCurrentStreak());
        assertEquals(3, points.getLongestStreak());
    }


    /**
     * Tests that two completions on the same day count once.
     */
    @Test
    public void testRecordCompletionSameDay() {
        points.recordCompletion(today);
        points.recordCompletion(today);
        assertEquals(1, points.getCurrentStreak());
        assertEquals(1, points.getLongestStreak());
    }


    /**
     * Tests that skipping a day restarts the streak at 1 but keeps
     * the longest streak.
     */
    @Test
    public void testRecordCompletionAfterGap() {
        points.recordCompletion(today.minusDays(5));
        points.recordCompletion(today.minusDays(4));
        points.recordCompletion(today);
        assertEquals(1, points.getCurrentStreak());
        assertEquals(2, points.getLongestStreak());
    }


    /**
     * Tests that a date earlier than the last completion does not
     * change the streak.
     */
    @Test
    public void testRecordCompletionEarlierDate() {
        points.recordCompletion(today);
        points.recordCompletion(today.minusDays(1));
        assertEquals(1, points.getLongestStreak());
    }


    /**
     * Tests updateStatus before anything is completed.
     */
    @Test
    public void testUpdateStatusNoCompletions() {
        points.updateStatus(today);
        assertTrue(points.isStreakSafe());
        assertEquals(0, points.getCurrentStreak());
    }


    /**
     * Tests updateStatus on the same day as the last completion.
     */
    @Test
    public void testUpdateStatusSameDay() {
        points.recordCompletion(today);
        points.updateStatus(today);
        assertTrue(points.isStreakSafe());
        assertEquals(1, points.getCurrentStreak());
    }


    /**
     * Tests that one day without a completion puts the streak in
     * jeopardy without resetting it.
     */
    @Test
    public void testUpdateStatusOneDayGap() {
        points.recordCompletion(today.minusDays(1));
        points.updateStatus(today);
        assertFalse(points.isStreakSafe());
        assertEquals(1, points.getCurrentStreak());
    }


    /**
     * Tests that two or more days without a completion resets the
     * current streak but keeps the longest streak.
     */
    @Test
    public void testUpdateStatusLongGap() {
        points.recordCompletion(today.minusDays(3));
        points.recordCompletion(today.minusDays(2));
        points.updateStatus(today);
        assertTrue(points.isStreakSafe());
        assertEquals(0, points.getCurrentStreak());
        assertEquals(2, points.getLongestStreak());
    }


    /**
     * Tests that completing a task makes a streak in jeopardy safe
     * again and extends it.
     */
    @Test
    public void testCompletionSavesStreak() {
        points.recordCompletion(today.minusDays(1));
        points.updateStatus(today);
        assertFalse(points.isStreakSafe());
        points.recordCompletion(today);
        assertTrue(points.isStreakSafe());
        assertEquals(2, points.getCurrentStreak());
    }


    /**
     * Tests the public setStreakStatus method against the real date.
     */
    @Test
    public void testSetStreakStatus() {
        points.recordCompletion(today.minusDays(4));
        points.setStreakStatus();
        assertTrue(points.isStreakSafe());
        assertEquals(0, points.getCurrentStreak());
        assertEquals(1, points.getLongestStreak());
    }
}