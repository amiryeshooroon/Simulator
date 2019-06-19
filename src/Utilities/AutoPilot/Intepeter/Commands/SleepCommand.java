package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.util.List;

public class SleepCommand implements Command {
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 2, emptyList, TypeArguments.String, TypeArguments.Integer);
    }

    @Override
    public void doCommand(List<Object> args) {
        try {
            Thread.sleep((int)args.get(1));
        } catch (InterruptedException e) {}
    }
}
