package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.TypeArguments;
import Utilities.AutoPilot.Intepeter.Variables.BindVar;

import java.util.List;

public class SetBindCommand implements Command { //called by =bind
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx-1, 3, emptyList, TypeArguments.String, TypeArguments.String, TypeArguments.String) - 1;
    }

    @Override
    public void doCommand(List<Object> args) {
        Parser.getInstance().getLastScope().addVar((String)args.get(0), new BindVar((String)args.get(0), (String)args.get(2)));
    }
}
