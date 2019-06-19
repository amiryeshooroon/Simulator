package Utilities.AutoPilot.Intepeter.Variables;

import Utilities.AutoPilot.Exceptions.NotConnectedToServerException;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.SimulatorServer;

public class BindVar extends Var {
    String bindVal;

    public BindVar(String name, String bindVal) {
        super(name);
        this.bindVal = bindVal;
    }

    @Override
    public double calculate() {
        return Parser.getInstance().getBindVarValueMap().get(bindVal);
    }

    @Override
    public void set(Double value) throws NotConnectedToServerException {
        SimulatorServer.getServer().setVariable(bindVal, value);
        Parser.getInstance().getBindVarValueMap().put(bindVal, value);
        //need to send to server
    }
}
