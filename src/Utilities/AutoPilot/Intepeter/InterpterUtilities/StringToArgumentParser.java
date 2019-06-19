package Utilities.AutoPilot.Intepeter.InterpterUtilities;



import Utilities.AutoPilot.Expression.ExpressionConvertor;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.util.LinkedList;
import java.util.List;

public class StringToArgumentParser {
    public static int parse(String[] args, int idx, int argumentsNeeded, List<Object> emptyList, TypeArguments... argsType) throws Exception{
        int j = idx, k;
        boolean flag;
        List<String> stringList = null;
        for (int i = 0; i < argumentsNeeded && j < args.length; i++) {
            switch (argsType[i]){
                case Character:
                case String:
                    emptyList.add(args[j++]);
                    break;
                case Integer:
                    k = 0;
                    flag = false;
                    stringList = new LinkedList<>();
                    for (; j < args.length ; j++) {
                        boolean flag2 = "+-*/()".contains(args[j]);
                        if(!flag2 && flag) break;
                        else flag = !flag2;
                        stringList.add(args[j]);
                    }
                    emptyList.add((int) ExpressionConvertor.calculatePostfix(ExpressionConvertor.infixToPostfix(stringList)));
                    break;
                case Float:
                case Double:
                    k = 0;
                    flag = false;
                    stringList = new LinkedList<>();
                    for (; j < args.length ; j++) {
                        boolean flag2 = "+-*/()".contains(args[j]);
                        if(!flag2 && flag) break;
                        else flag = !flag2;
                        stringList.add(args[j]);
                    }
                    emptyList.add(ExpressionConvertor.calculatePostfix(ExpressionConvertor.infixToPostfix(stringList)));
                    break;
                case Block:
                    //if !args[j].equals("{") throw exception
                    ++j;
                    int cnt = 0;
                    StringBuilder stringBuilder = new StringBuilder();
                    flag = false;
                    for(; j < args.length && (!args[j].equals("}") || cnt != 0); j++){
                        if(flag) stringBuilder.append(" ");
                        if(args[j].equals("{")) cnt++;
                        else if(args[j].equals("}")) cnt--;
                        stringBuilder.append(args[j]);
                        flag = true;
                    }
                    emptyList.add(stringBuilder.toString());
                    break;
                case Condition:
                    stringList = new LinkedList<>();
                    for(; j < args.length && !args[j].equals("{"); j++){
                        stringList.add(args[j]);
                    }
                    emptyList.add(stringList);
                    break;
            }
        }
        return j - idx;
    }
    public static void main(String[] strings){
        List<Object> list = new LinkedList<>();
        try {
            parse(new String[]{"while", "p", "<=", "6", "{", "var", "x", "=", "0", "if","x","<=","8","{","var","y","=","7","x","=","x","+","1","}","}"}, 0, 3, list, TypeArguments.String, TypeArguments.Condition, TypeArguments.Block);
            for (int i = 0; i < ((List<String>)list.get(1)).size(); i++) {
                System.out.println(((List<String>)list.get(1)).get(i));
            }
            System.out.println((String)list.get(2));
        } catch (Exception e) {
        }
    }
}
