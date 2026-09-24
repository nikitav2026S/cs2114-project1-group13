package arraybag;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point and user-interaction layer for the to-do list app.
 * Main owns the Scanner, holds one Task and one Points object, and
 * translates menu choices into calls on those two classes. It does not
 * store any task or point data itself — that all lives in Task/Points.
 */
public class Main {

    private static final int MAX_TITLE_LENGTH = 100; // TODO: confirm the real limit for the spec sheet

    private Scanner scanner;
    private Task task;
    private Points points;

    public Main() {
        scanner = new Scanner(System.in);
        task = new Task();
        points = new Points();
    }

    public static void main(String[] args) {
        new Main().run();
    }

    /** Runs the menu loop until the user chooses to quit. */
    public void run() {
        printInstructions();
        boolean run = true;

        while (run) {
            printMenu();
            int action = userInt("Enter your choice: ");

            if (action == 1) {
                handleAddTask();
                break;
            }
            if (action == 2) {
                handleEditTask();
                break;
            }
            if (action == 3) {
                handleAddTask();
                break;
            }
            if (action == 4) {
                handleAddTask();
                break;
            }
            if (action == 5) {
                handleAddTask();
                break;
            }
            if (action == 6) {
                handleAddTask();
                break;
            }
            if (action == 0) {
                handleAddTask();
                break;
            }
                 
                   
                case 3:
                    handleCompleteTask();
                    break;
                case 4:
                    handleRemoveTask();
                    break;
                case 5:
                    displayTasks();
                    break;
                case 6:
                    displayStatus();
                    break;
                case 0:
                    run = false;
                    System.out.println("Go get your tasks done!");
                    break;
                default:
                    System.out.println("Please enter a number between 0 and 6.");
            
        }

        scanner.close();
    }

    // ----- Display -----

    private void printInstructions() {
        System.out.println("=== To-Do List ===");
        System.out.println("Add tasks, complete them, and build a daily streak.");
        System.out.println("Choose a menu option by typing its number.");
        System.out.println();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add task");
        System.out.println("2. Edit task");
        System.out.println("3. Complete task");
        System.out.println("4. Remove task");
        System.out.println("5. View tasks");
        System.out.println("6. View points and streak");
        System.out.println("0. Quit");
    }

    private void displayStatus() {
        System.out.println();
        System.out.println("Points: " + points.getTotalPoints());
        System.out.println("Current streak: " + points.getCurrentStreak() + " day(s)");
        System.out.println("Longest streak: " + points.getLongestStreak() + " day(s)");
        if (!points.isStreakSafe()) {
            System.out.println("Your streak is in jeopardy — complete a task today!");
        }
    }

    private void displayTasks() {
        ArrayList<String> toDo = task.getToDoTasks();
        ArrayList<String> completed = task.getCompletedTasks();

        System.out.println();
        System.out.println("To-Do:");
        if (toDo.isEmpty()) {
            System.out.println("  (nothing yet)");
        } else {
            for (int i = 0; i < toDo.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + toDo.get(i));
            }
        }

        System.out.println("Completed:");
        if (completed.isEmpty()) {
            System.out.println("  (nothing yet)");
        } else {
            for (String title : completed) {
                System.out.println("  " + strikethrough(title));
            }
        }
    }

    /** Renders text with a strikethrough effect for completed tasks. */
    private String strikethrough(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(c).append('\u0336');
        }
        return sb.toString();
    }

    // ----- Handlers -----

    private void handleAddTask() {
        System.out.print("Enter task: ");
        String title = scanner.nextLine();

        if (title.trim().isEmpty()) {
            System.out.println("Task can't be blank.");
            return;
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            System.out.println("Task is too long (max " + MAX_TITLE_LENGTH + " characters).");
            return;
        }

        task.addTask(title.trim());
        System.out.println("Added: " + title.trim());
    }

    private void handleEditTask() {
        if (task.isEmpty()) {
            System.out.println("You have no tasks yet.");
            return;
        }

        displayTasks();
        int index = readTaskIndex("Which task would you like to edit? ", task.getToDoCount());

        System.out.print("Enter the new text: ");
        String newTitle = scanner.nextLine();

        if (newTitle.trim().isEmpty()) {
            System.out.println("Task can't be blank.");
            return;
        }
        if (newTitle.length() > MAX_TITLE_LENGTH) {
            System.out.println("Task is too long (max " + MAX_TITLE_LENGTH + " characters).");
            return;
        }

        task.editTask(index, newTitle.trim());
        System.out.println("Task updated.");
    }

    private void handleRemoveTask() {
        if (task.isEmpty()) {
            System.out.println("You have no tasks yet.");
            return;
        }

        displayTasks();
        int index = readTaskIndex("Which task would you like to remove? ", task.getToDoCount());
        String removed = task.removeTask(index);
        System.out.println("Removed: " + removed);
    }

    private void handleCompleteTask() {
        if (task.isEmpty()) {
            System.out.println("You have no tasks yet.");
            return;
        }

        displayTasks();
        int index = readTaskIndex("Which task would you like to complete? ", task.getToDoCount());
        String completedTitle = task.completeTask(index);

        points.awardPoints();
        points.recordCompletionToday();

        System.out.println("Good job! Completed: " + completedTitle);
        System.out.println("Points: " + points.getTotalPoints() + " | Streak: " + points.getCurrentStreak());
    }

    // ----- Input helpers -----

    /** Re-prompts until the user types a whole number. */
    private int userInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    /** Re-prompts until the user picks a valid 1-based task number, then returns it 0-based. */
    private int readTaskIndex(String prompt, int listSize) {
        while (true) {
            int oneBased = readInt(prompt);
            if (oneBased >= 1 && oneBased <= listSize) {
                return oneBased - 1;
            }
            System.out.println("Please enter a number between 1 and " + listSize + ".");
        }
    }
}