package Utilities.AutoPilot.Intepeter.Variables;

public class RegularVar extends Var {
    double value;

    public RegularVar(String name, double value) {
        super(name);
        this.value = value;
    }

    @Override
    public double calculate() {
        return value;
    }

    @Override
    public void set(Double value) {
        this.value = value;
    }
}
