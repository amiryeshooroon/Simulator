package Utilities.Properties;

import java.util.Observable;
import java.util.Observer;

public class MyProperty<T> extends Observable implements Observer {
    T value;

    public void bind(MyProperty<T> property){
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
        if(!value.equals(t.get())) set(t.get());
    }
}
