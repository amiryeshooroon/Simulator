package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.TypeArguments;
import Utilities.AutoPilot.Intepeter.Variables.RegularVar;

import java.util.List;

public class NewVarCommand implements Command { //called by var command
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 2, emptyList, TypeArguments.String, TypeArguments.String);
    }
    //We put the real value in set but the default is regular variable and his value is 0
    @Override
    public void doCommand(List<Object> args) {
        Parser.getInstance().getLastScope().addVar((String)(args.get(1)), new RegularVar((String)(args.get(1)), 0));
    }
}
