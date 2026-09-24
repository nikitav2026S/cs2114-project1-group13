package app;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Points {
    private int totalPoints;
    private int currentStreak;
    private int longestStreak;
    private boolean streakState; //true = safe
    private LocalDate lastCompletionDate; //null at first

    public Points() {
        totalPoints = 0;
        currentStreak = 0;
        longestStreak = 0;
        streakState = true;
        lastCompletionDate = null;
    }

    public void awardPoints() {
        earnPoints(1);
    }

    public void earnPoints(int amount) {
        if (amount > 0) {
            totalPoints += amount;
        }
    }

    public void recordCompletionToday() {
        recordCompletion(LocalDate.now());
    }

    //private for JUnit testing days
    void recordCompletion(LocalDate today) {
        if (lastCompletionDate == null) {
            currentStreak = 1;
        } else {
            long gap = ChronoUnit.DAYS.between(lastCompletionDate, today);
            if (gap == 1) {
                currentStreak++;
            } else if (gap > 1) {
                currentStreak = 1;
            }
            // gap == 0: already counted today
        }
        longestStreak = Math.max(longestStreak, currentStreak);
        lastCompletionDate = today;
        streakState = true;
    }

    public void setStreakStatus() {
        updateStatus(LocalDate.now());
    }

    void updateStatus(LocalDate today) {
        if (lastCompletionDate == null) {
            streakState = true;
            return;
        }
        long gap = ChronoUnit.DAYS.between(lastCompletionDate, today);
        if (gap == 0) {
            streakState = true;
        } else if (gap == 1) {
            streakState = false; //in jeapardy
        } else {
            currentStreak = 0; //steak resets, <1 day inactive
            streakState = true;
        }
    }

    public int getTotalPoints() { return totalPoints; }

    public int getCurrentStreak() {
        setStreakStatus();
        return currentStreak;
    }

    public int getLongestStreak() { return longestStreak; }

    public boolean isStreakSafe() {
        setStreakStatus();
        return streakState;
    }
}