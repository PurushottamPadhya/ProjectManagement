package commandManager;
// Action interface to execute, undo operation
public interface Action {
    void execute();
    Object undo();
    String getName();
    Object getObject();
}
