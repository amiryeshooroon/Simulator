package Utilities.AutoPilot.Intepeter;

import Exceptions.CodeErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Lexer {
    public static String[] lexer(String code) throws CodeErrorException {
        if(code == null || code.equals("")) throw new CodeErrorException();
         List<String> list =  Arrays.stream(code.split("((?<=[+*\\-=/\\x22\\s&|}{<>()])|(?=[+*\\-=/\\x22\\s&|}{<>()]))")).filter(s->s.trim().length() > 0).collect(Collectors.toList());
        for (int i = 0; i < list.size() - 1; i++) {
            if(list.get(i).equals("=") && list.get(i+1).equals("bind")) {
                list.remove(i + 1);
                list.set(i, "= bind");
            }
            else if(list.get(i).equals("\"")){
                StringBuilder stringBuilder = new StringBuilder();
                int j;
                for (j = i + 1; j < list.size();) {
                    if(list.get(j).equals("\"")) {list.remove(j); break;}
                    stringBuilder.append(list.get(j));
                    list.remove(j);
                }
                list.set(i, stringBuilder.toString());
            }
            else if(list.get(i).equals("&") && list.get(i+1).equals("&")) {list.set(i, "&&"); list.remove(i + 1);}
            else if(list.get(i).equals("|") && list.get(i+1).equals("|")) {list.set(i, "||"); list.remove(i + 1);}
            else if(list.get(i).equals("<") && list.get(i+1).equals("=")) {list.set(i, "<="); list.remove(i + 1);}
            else if(list.get(i).equals(">") && list.get(i+1).equals("=")) {list.set(i, ">="); list.remove(i + 1);}
        }
        return list.toArray(new String[1]);
    }
}
