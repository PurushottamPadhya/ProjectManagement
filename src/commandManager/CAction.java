package commandManager;


public class CAction implements Action {
	
	Object obj;
    private String name;
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