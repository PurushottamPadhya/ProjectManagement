package commandManager;

public interface Action {
    void execute();
    Object undo();
    String getName();
    Object getObject();
}
