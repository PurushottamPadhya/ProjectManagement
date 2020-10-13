package milestone5;

public interface Action {
    void execute();
    void undo();
    String getName();
    Object getObject();
}
