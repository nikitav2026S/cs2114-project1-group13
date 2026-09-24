package app;

import java.util.ArrayList;

public class Task {
    
    private static final int MAX_TITLE_LENGTH = 100;
    
    private ArrayList<String> toDoTasks;
    private ArrayList<String> completedTasks;
    
    public Task() {
        toDoTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        
    }
    
    /**
     * Adds a new task to the to-do list.
     * Returns false (and adds nothing) if the title is null, empty, or
     * just whitespace, that's Main's cue to print the "can't be blank"
     * warning from the spec's validation table.
     */
    
    public boolean addTask(String title) {
        if(title == null || title.trim().isEmpty()) {
            return false;
            
        }
        
        String cleaned = title.trim();
        if (cleaned.length() > MAX_TITLE_LENGTH) {
            cleaned = cleaned.substring(0, MAX_TITLE_LENGTH);
                
        }
        
        toDoTasks.add(cleaned);
        return true;
    }
    
    /**
     * Replaces the title of the to-do task at index.
     * Returns false if the index is out of range or the new title is blank.
     */
    
    public boolean editTask(int index, String newTitle) 
    {
        if(newTitle == null || newTitle.trim().isEmpty())
        {
            return false;
        }
        if(index < 0 || index >= toDoTasks.size()) {
            return false;
            
        }
        
        String cleaned = newTitle.trim();
        if (cleaned.length() > MAX_TITLE_LENGTH) {
            cleaned = cleaned.substring(0, MAX_TITLE_LENGTH);
            
        }
        
        toDoTasks.set(index, cleaned);
        return true;
        
    }
    
    /**
     * Removes the to-do task at index without completing it.
     * Returns the removed title, or null if the index was invalid.
     */
    
    public String removeTask(int index) {
        if (index < 0 || index >= toDoTasks.size()) {
            return null;
        }
        return toDoTasks.remove(index);
        
    }
    
    /**
     * Moves the to-do task at index into the completed list.
     * Returns the completed title (so Main can print it), or null if the
     * index was invalid. This is where a task "moves lists" instead of
     * just getting a completed flag flipped on it.
     */
    
    public String completeTask(int index) {
        if (index < 0 || index >= toDoTasks.size()) {
            return null;
            
        }
        String finished = toDoTasks.remove(index);
        completedTasks.add(finished);
        return finished;
        
    }
    
    /** Returns the to-do task at index, or null if the index is invalid. */
    
    public String getTask(int index) {
        if (index < 0 || index >= toDoTasks.size()) {
            return null;
            
        }
        return toDoTasks.get(index);
        
    }
    
    public int getToDoCount() {
        return toDoTasks.size();
        
    }
    
    public int getCompletedCount() {
        return completedTasks.size();
        
    }
    
    /** True when there are no active to-do tasks left. */
    public boolean isEmpty() {
        return toDoTasks.isEmpty();
        
    }
    
    public ArrayList<String> getToDoTasks() {
        return toDoTasks;
        
    }
    
    public ArrayList<String> getCompletedTasks() {
        return completedTasks;
    }
    
    
}
