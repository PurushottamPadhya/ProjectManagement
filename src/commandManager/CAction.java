package commandManager;

// This class is an implementaion of Action Interface to implement Undo operation
public class CAction implements Action {
	
	Object obj;
    private String name;
    // Create constructor with name and obejct to store
    public CAction(String name, Object obj) {
        this.name = name;
        this.obj = obj;
    }
    @Override
    public void execute() {
        System.out.println("Executing Action " + name);
    }
    @Override
    public Object undo() {
    	
    	
        System.out.println("Undo Action " + name);
        
        return obj;
    }
    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public Object getObject() {
    	return this.obj;
    }
}