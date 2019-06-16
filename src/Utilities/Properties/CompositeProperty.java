package Utilities.Properties;

import Exceptions.WrongLimitError;
import javafx.util.Pair;

import java.util.*;

public class CompositeProperty<T> {
    HashMap<String, MyProperty<T>> properties;
    int limit;
    public CompositeProperty(int limit){
        properties = new HashMap<String, MyProperty<T>>();
        this.limit = limit;
    }
    public void bind(Pair<String, MyProperty<T>>... newProperties){
        for(Pair<String, MyProperty<T>> p : newProperties) {
            MyProperty<T> myProperty = new MyProperty<>();
            p.getValue().bind(myProperty);
            properties.put(p.getKey(), myProperty);
        }
    }
    public void bind(CompositeProperty<T> cp) throws WrongLimitError{
        if(cp.limit != limit) throw new WrongLimitError("Limits are different. Please try again!");
        properties = new HashMap<>();
        for(String str : properties.keySet()) {
            MyProperty<T> myProperty = new MyProperty<>();
            myProperty.bind(cp.properties.get(str));
            properties.put(str, myProperty);
        }
    }
    public void unbind(String name){
        properties.remove(name);
    }
}
