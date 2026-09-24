package app;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

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
                handleCompleteTask();
                break;
            }
            if (action == 4) {
                handleRemoveTask();
                break;
            }
            if (action == 5) {
                displayTasks();
                break;
            }
            if (action == 6) {
                displayStatus();
                break;
            }
            if (action == 0) {
                run = false;
                System.out.println("Have fun getting your tasks done!");
                break;
            }
            System.out.println("Please enter a number between 0 and 6.");
        }

        scanner.close();
    }



    private void printInstructions() {
        System.out.println("=== How to Use To-Do List ===");
        System.out.println("Add tasks, complete them, and build a daily streak.");
        System.out.println("Choose an action by typing its number.");
        System.out.println();
    }


    private void printMenu() {
        System.out.println();
        System.out.println("1. Add a task");
        System.out.println("2. Edit a task");
        System.out.println("3. Complete a task");
        System.out.println("4. Remove a task");
        System.out.println("5. View your tasks");
        System.out.println("6. View your points and streak");
        System.out.println("0. Exit to-do list");
    }


    private void displayStatus() {
        System.out.println();
        System.out.println("Points: " + points.getTotalPoints());
        System.out.println("Current streak: " + points.getCurrentStreak()
            + " day(s)");
        System.out.println("Longest streak: " + points.getLongestStreak()
            + " day(s)");
        if (!points.isStreakSafe()) {
            System.out.println( "Your streak is about to end, complete a task to keep it going!");
        }
    }


    private void displayTasks() {
        ArrayList<String> todo = task.getToDoTasks();
        ArrayList<String> completed = task.getCompletedTasks();
        System.out.println();
        System.out.println("To-Do:");
        if (todo.isEmpty()) {
            System.out.println("Nothing yet, you are all caught up!");
        }
        else {
            for (int i = 0; i < todo.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + todo.get(i));
            }
        }

        System.out.println("Completed:");
        if (completed.isEmpty()) {
            System.out.println("Nothing so far, you need to lock in!");
        }
        else {
            for (String title : completed) {
                System.out.println("  " + strikethrough(title));
            }
        }
    }

  
    private String strikethrough(String text) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            result += text.charAt(i);
            result += '\u0336';
        }
        return result;
    }


    private void handleAddTask() {
        System.out.print("Enter the task title: ");
        String title = scanner.nextLine();

        if (title.trim().isEmpty()) {
            System.out.println("Task can't be blank.");
            return;
        }
        if (title.length() > 100) {
            System.out.println("Your task title is too long (max  100 characters).");
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
        int index = readTaskIndex("Which task would you like to edit? ", task
            .getToDoCount());

        System.out.print("Enter the new title: ");
        String newTitle = scanner.nextLine();

        if (newTitle.trim().isEmpty()) {
            System.out.println("The task title can't be blank.");
            return;
        }
        if (newTitle.length() > 100) {
            System.out.println("The task title is too long (max 100 characters).");
            return;
        }

        task.editTask(index, newTitle.trim());
        System.out.println("The task has been updated.");
    }


    private void handleRemoveTask() {
        if (task.isEmpty()) {
            System.out.println("You have no tasks yet.");
            return;
        }

        displayTasks();
        int index = readTaskIndex("Which task would you like to remove? ", task
            .getToDoCount());
        String removed = task.removeTask(index);
        System.out.println("Removed: " + removed);
    }


    private void handleCompleteTask() {
        if (task.isEmpty()) {
            System.out.println("You have no tasks yet.");
            return;
        }

        displayTasks();
        int index = readTaskIndex("Which task would you like to complete? ",
            task.getToDoCount());
        String titleCompleted = task.completeTask(index);

        points.awardPoints();
        points.recordCompletionToday();

        System.out.println("Good job completing " + titleCompleted + "!");
        System.out.println("Points: " + points.getTotalPoints());
        System.out.println("Streak: " + points.getCurrentStreak());
    }


    private int userInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private int readTaskIndex(String prompt, int listsize) {
        while (true) {
            int num = userInt(prompt);
            if (num >= 1 && num <= listsize) {
                return num - 1;
            }
            System.out.println("Please enter a number between 1 and " + listsize
                + ".");
        }
    }
}
