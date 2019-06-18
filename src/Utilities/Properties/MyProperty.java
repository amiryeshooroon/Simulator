package Utilities.Properties;

import java.util.Observable;
import java.util.Observer;

public class MyProperty<T> extends Observable implements Observer {
    T value;
    public MyProperty(T val){
        value = val;
    }
    public MyProperty(){
        value = null;
    }
    public void bind(MyProperty<T> property){
        value = property.value;
        property.addObserver(this);
    }
    public void set(T value){
        this.value = value;
        setChanged();
        notifyObservers();
    }
    public T get(){
        return value;
    }

    @Override
    public void update(Observable o, Object arg) {
        MyProperty<T> t = (MyProperty<T>)o;
        if(value == null || !value.equals(t.get()))
            set(t.get());
    }
}
