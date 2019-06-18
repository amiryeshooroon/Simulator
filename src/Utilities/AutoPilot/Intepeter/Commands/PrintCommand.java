package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.TypeArguments;
import Utilities.AutoPilot.Intepeter.Variables.Var;

import java.util.List;

public class PrintCommand implements Command {
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 2, emptyList, TypeArguments.String, TypeArguments.String);
    }

    @Override
    public void doCommand(List<Object> args) {
        String str = (String)args.get(1);
        Var v;
        if((v=Parser.getInstance().getLastScope().getVar(str))!=null) System.out.println(v.calculate());
        else System.out.println(str);
    }

}
