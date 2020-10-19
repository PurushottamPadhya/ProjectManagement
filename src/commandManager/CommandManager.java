
// Reference: From the lecture note of Week 10 (Created by Charles) and modified by Purushottam.
package commandManager;
import java.util.ArrayList;
import java.util.List;


public class CommandManager {
	// instance variables
    private static CommandManager instance = null;
    private Stack<Action> stackNormal;
    private Stack<Action> stackReverse;
    private List<String> actionHistory;
    
    // singleton instance
    public static CommandManager getInstance(){
        if(instance != null)
            return instance;
        return new CommandManager();
    }
    // default constructor
    private CommandManager() {
        stackNormal = new Stack<>();
        stackReverse = new Stack<>();
        actionHistory = new ArrayList<>();
    }
    
    // push current object into the stack
    public void execute(Action action){
        action.execute();
        stackNormal.push(action);
        actionHistory.add(action.getName());
        
    }

    // undo method that check if data is on stack and pop the last added object
    public  Object undo() {
        Action a;
    	    if (stackNormal.size() > 0)
    	    {   a = stackNormal.pop();
            a.undo();
            stackReverse.push(a);
            actionHistory.add(a.getName() + " - undo");
            return a.getObject();
        }
    	    return null;
    }
    // redo is opposite to the undo and here not implemented perfectly
    public void redo() {
        Action a = stackReverse.pop();
        if ( a != null)
        	{   a.execute();
            stackNormal.push(a);
            actionHistory.add(a.getName() + " - redo");
        }
    }
    
    // clear the stored stack of Normal
    void clearNormal() {
        stackNormal.clear();
    }
    
    // clear reverse stack object data
    void clearReverse() {
       stackReverse.clear();
    }
    
    // this is to track the history of input either redo or undo
    public List<String> getActionHistory() {
        return actionHistory;
    }
}