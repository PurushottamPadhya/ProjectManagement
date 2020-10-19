package commandManager;

import java.util.LinkedList;
// Generic stack class that takes object of type T is Generic
import java.util.List;
class Stack<T> {
    private List<T> dataCollection;
    Stack() {
        dataCollection = new LinkedList<>();
    }
    void push(T item) {
        dataCollection.add(dataCollection.size(), item);
    }
    T pop() {
        if(dataCollection.size() > 0)
            return dataCollection.remove(dataCollection.size()-1);
        else
            return null;
    }
    public int size()
    {
    	    return dataCollection.size();
    }
    void clear() {
        dataCollection.clear();
    }
}