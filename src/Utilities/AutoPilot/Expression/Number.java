package Utilities.AutoPilot.Expression;

public class Number implements Expression {
    double value;

    public Number(double value) {
        this.value = value;
    }
    @Override
    public double calculate() {
        return value;
    }
}
