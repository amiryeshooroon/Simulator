package Utilities.AutoPilot.Intepeter.Commands;

import Exceptions.CodeErrorException;
import Utilities.AutoPilot.Exceptions.ArgumentErrorException;
import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LoopCommand implements Command {
    ConditionCommand conditionCommand;
    List<String> asString = null;
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 3, emptyList, TypeArguments.String, TypeArguments.Condition, TypeArguments.Block);
    }

    @Override
    public void doCommand(List<Object> args) throws ArgumentErrorException {
        if(asString == null) asString = ((List<String>)args.get(1)).stream().map(Objects::toString).collect(Collectors.toList());
        ConditionCommand conditionCommand = new ConditionCommand();
        String code = (String)args.get(2);
        Parser.getInstance().getLastScope().addInnerScope();
        while(conditionCommand.checkCondition(asString)) {
            try {
                Parser.getInstance().parse(code);
            } catch (CodeErrorException e) {
                e.printStackTrace();
            }
        }
        Parser.getInstance().getLastScope().disposeInnerScope();
    }
}
