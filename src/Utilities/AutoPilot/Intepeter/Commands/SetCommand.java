package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.util.List;

public class SetCommand implements Command {
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        if(!args[idx].equals("=")) throw new Exception("No Command!");
        return StringToArgumentParser.parse(args, idx - 1, 3, emptyList, TypeArguments.String, TypeArguments.String, TypeArguments.Double) - 1;
    }

    @Override
    public void doCommand(List<Object> args) throws Exception {
        //Parser.getInstance().getLastScope().addVar((String)args.get(0), new RegularVar((String)args.get(0), (double)args.get(2)));
        Parser.getInstance().getLastScope().getVar((String)args.get(0)).set((double)args.get(2));
    }
}
