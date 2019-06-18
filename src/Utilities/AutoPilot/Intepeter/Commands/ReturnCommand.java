package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.util.List;

public class ReturnCommand implements Command {
    double returnValue = 0;
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 2, emptyList, TypeArguments.String, TypeArguments.Double);
    }

    @Override
    public void doCommand(List<Object> args) {
        returnValue = (double)args.get(1);
    }

    public double getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(double returnValue) {
        this.returnValue = returnValue;
    }
}
