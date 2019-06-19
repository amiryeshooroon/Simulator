package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Exceptions.ArgumentErrorException;
import Utilities.AutoPilot.Intepeter.*;
import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Variables.Var;
import Utilities.AutoPilot.Intepeter.InterpterUtilities.Utilities;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConditionCommand implements Command {
    List<String> asString = null;
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 3, emptyList, TypeArguments.String, TypeArguments.Condition, TypeArguments.Block);
    }
    @Override
    public void doCommand(List<Object> args) throws Exception {
        if(asString == null)
            asString = ((List<Object>)args.get(1)).stream().map(Object::toString).collect(Collectors.toList());
        Parser.getInstance().getLastScope().addInnerScope();
        if(checkCondition(asString)) Parser.getInstance().parse((String)args.get(2));
        Parser.getInstance().getLastScope().disposeInnerScope();

    }
    public boolean checkCondition(List<String> conditionParts) throws ArgumentErrorException{
        int size = conditionParts.size();
        int before = -1;
        Var v;
        StringBuilder stringBuilder = new StringBuilder();
        ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("JavaScript");
        for(int i=0;i<size;i++){
            String aPart = conditionParts.get(i);
            if(Utilities.checkDouble(aPart)) {
                if(before == 0) throw new ArgumentErrorException("Argument Error");
                stringBuilder.append(aPart);
                before = 0;
            }
            else if("+-*/()".contains(aPart)){
                if(before == 1 || before == -1) throw new ArgumentErrorException("Argument Error");
                stringBuilder.append(aPart);
                before = 1;
            }
            else if(Arrays.asList("<=", ">=", "<", ">", "==", "!", "!=", "||", "&&").contains(aPart)){
                if(before == 2 || before == 1) throw new ArgumentErrorException("Argument Error");
                stringBuilder.append(aPart);
                before = 2;
            }
            else if((v = Utilities.getVar(aPart))!=null){
                if(before == 3 || before == 0) throw new ArgumentErrorException("Argument Error");
                stringBuilder.append(v.calculate());
                before = 3;
            }

        }
        try {
            return (boolean)engine.eval(stringBuilder.toString());
        } catch (ScriptException e) {
            throw new ArgumentErrorException("Argument Error");
        }
    }

    public static void main(String... args) throws Exception {
        ConditionCommand conditionCommand = new ConditionCommand();
        conditionCommand.doCommand(Arrays.asList("if", Arrays.asList("5", "*", "10", "-","2","<","1","||","5","<=","10"), "test"));
    }
}
