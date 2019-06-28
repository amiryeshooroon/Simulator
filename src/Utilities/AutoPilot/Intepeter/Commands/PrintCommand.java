package Utilities.AutoPilot.Intepeter.Commands;

import Notifications.DisplayToConsole;
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
        Parser.getInstance().myModel.setModelChanged();
        if((v=Parser.getInstance().getLastScope().getVar(str))!=null)
            Parser.getInstance().myModel.notifyObservers(new DisplayToConsole(String.valueOf(v.calculate())));
        else Parser.getInstance().myModel.notifyObservers(new DisplayToConsole(str));
    }

}
