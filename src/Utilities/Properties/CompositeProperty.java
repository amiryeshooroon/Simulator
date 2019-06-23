package Utilities.Properties;

import Exceptions.WrongLimitError;
import javafx.util.Pair;
import java.util.*;

public class CompositeProperty<T> implements Observer{
    private HashMap<String, MyProperty<T>> properties;
    private int limit;
    private Runnable onUpdate;
    public CompositeProperty(int limit){
        properties = new HashMap<>();
        this.limit = limit;
        onUpdate = null;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void bind(Pair<String, MyProperty<T>>... newProperties) throws WrongLimitError {
        if(newProperties.length != limit) throw new WrongLimitError("Limits are different. Please try again!");
        for (MyProperty<T> p:
             properties.values()) {
            p.deleteObserver(this);
        }
        properties.clear();
        for(Pair<String, MyProperty<T>> p : newProperties) {
            MyProperty<T> myProperty = new MyProperty<>();
            myProperty.bind(p.getValue());
            myProperty.addObserver(this);
            properties.put(p.getKey(), myProperty);
        }
    }
    public void bind(CompositeProperty<T> cp) throws WrongLimitError{
        if(cp.limit != limit) throw new WrongLimitError("Limits are different. Please try again!");
        for (MyProperty<T> p:
                properties.values()) {
            p.deleteObserver(this);
        }
        properties = new HashMap<>();
        for(String str : cp.properties.keySet()) {
            MyProperty<T> myProperty = new MyProperty<>();
            myProperty.bind(cp.properties.get(str));
            myProperty.addObserver(this);
            properties.put(str, myProperty);
        }
    }
    public void unbind(String name) {
        MyProperty<T> tmp = properties.getOrDefault(name, null);
        if(tmp != null){
            tmp.deleteObserver(this);
            properties.remove(name);
        }
    }
    public T get(String name){
        MyProperty<T> tmp = properties.getOrDefault(name, null);
        if(tmp != null) return tmp.get();
        return null;
    }
    @Override
    public void update(Observable o, Object arg) {

        if(onUpdate != null)
            onUpdate.run();

    }
}
