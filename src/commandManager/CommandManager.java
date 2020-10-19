
// Reference: From the lecture note of Week 10 (Created by Charles) and modified by Purushottam.
package commandManager;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private static CommandManager instance = null;
    private Stack<Action> stackNormal;
    private Stack<Action> stackReverse;
    private List<String> actionHistory;
    public static CommandManager getInstance(){
        if(instance != null)
            return instance;
        return new CommandManager();
    }
    private CommandManager() {
        stackNormal = new Stack<>();
        stackReverse = new Stack<>();
        actionHistory = new ArrayList<>();
    }
    public void execute(Action action){
        action.execute();
        stackNormal.push(action);
        actionHistory.add(action.getName());
        
    }

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
    public void redo() {
        Action a = stackReverse.pop();
        if ( a != null)
        	{   a.execute();
            stackNormal.push(a);
            actionHistory.add(a.getName() + " - redo");
        }
    }
    void clearNormal() {
        stackNormal.clear();
    }
    void clearReverse() {
       stackReverse.clear();
    }
    public List<String> getActionHistory() {
        return actionHistory;
    }
}