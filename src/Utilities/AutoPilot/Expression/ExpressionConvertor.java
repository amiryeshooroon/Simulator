package Utilities.AutoPilot.Expression;

import Utilities.AutoPilot.Intepeter.Variables.Var;
import Utilities.AutoPilot.Intepeter.InterpterUtilities.Utilities;
import Utilities.AutoPilot.Intepeter.Variables.Var;
import java.util.*;

public class ExpressionConvertor {
    public static String infixToPostfix(List<String> split){
        ArrayList<String> splitArray = new ArrayList<>();
        splitArray.addAll(split);
        replaceVariables(splitArray); removeOperators(splitArray); checkMinuses(splitArray);
        split = splitArray;
        Queue<String> queue = new LinkedList<String>();
        Stack<String> stack = new Stack<String>();
        Var v = null;
        //String[] split = expression.split("(?<=[-+*/()])|(?=[-+*/()])");
        for (int i=0;i<split.size();i++){
            if (isDouble(split.get(i))){
                queue.add(split.get(i));
            }
            else{
                switch(split.get(i)) {
                    case "/":
                    case "*":
                    case "(":
                        stack.push(split.get(i));
                        break;
                    case "+":
                        while (!stack.empty() && (!stack.peek().equals("("))){
                            queue.add(stack.pop());
                        }
                        stack.push(split.get(i));
                        break;
                    case "-":
                        while (!stack.empty() && (!stack.peek().equals("("))){
                            queue.add(stack.pop());
                        }
                        stack.push(split.get(i));
                        break;
                    case ")":
                        while (!stack.peek().equals("(")){
                            queue.add(stack.pop());
                        }
                        stack.pop();
                        break;
                }
            }
        }
        while(!stack.isEmpty()){
            queue.add(stack.pop());
        }
        StringBuilder sb = new StringBuilder();
        for(String str : queue) sb.append(str).append(",");
        return sb.toString();
    }

    private static boolean isDouble(String val){
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double calculatePostfix(String postfix){
        Stack<Expression> stackExp = new Stack<Expression>();
        Var v = null;
        String[] expressions = postfix.split(",");
        for(String str : expressions) {
            if (isDouble(str)){ //add isVar
                stackExp.push(new Number(Double.parseDouble(str)));
            }
            else if((v = Utilities.getVar(str)) != null) stackExp.push(v); //add isFunction and we need to think how it's go to work
            else{
                Expression right = stackExp.pop();
                Expression left = stackExp.pop();
                switch(str) {
                    case "/":
                        stackExp.push(new Div(left, right));
                        break;
                    case "*":
                        stackExp.push(new Mult(left, right));
                        break;
                    case "+":
                        stackExp.push(new Plus(left, right));
                        break;
                    case "-":
                        stackExp.push(new Minus(left, right));
                        break;
                }
            }
        }
        return Math.floor((stackExp.pop().calculate() * 1000)) /1000;
    }
    private static void replaceVariables(ArrayList<String> split){
        Var v;
        for(int i=0;i<split.size();i++){
            if((v=Utilities.getVar(split.get(i)))!=null){
                if(v.calculate()>=0) split.set(i, String.valueOf(v.calculate()));
                else {
                    split.add(i++, "-");
                    split.set(i, String.valueOf(-v.calculate()));
                }
            }
        }
    }
    private static void removeOperators(ArrayList<String> split){
        for(int i=0;i<split.size();){
            if(Arrays.asList("+", "-").contains(split.get(i))) {
                int cnt=1, startIndex=i;
                boolean flag = !split.get(i++).equals("-"); //false if -
                while (Arrays.asList("+", "-").contains(split.get(i))) {
                    if(flag && split.get(i).equals("-")) flag = false; //+-
                    else if(!flag && split.get(i).equals("-")) flag = true; //--
                    //else if(flag && split.get(i).equals("+")) flag = true; //++
                    //else if(!flag && split.get(i).equals("+")) flag = false; //-+
                    ++cnt; ++i;
                }
                for(int j=0;j<cnt;j++, split.remove(startIndex));
                if(startIndex==0 || split.get(startIndex-1).equals("(")) {
                    if (!flag) split.add(0, "-");
                }
                else split.add(startIndex, flag ? "+" : "-");

            }
            else i++;
        }
    }
    private static void checkMinuses(ArrayList<String> split){
        for(int i=0;i<split.size();i++){
            if(split.get(i).equals("-")){
                if(i==0) { //- at the beginning
                    split.remove(0);
                    split.set(0, "-" + split.get(0));
                }
                else if(split.get(i-1).equals("*") || split.get(i-1).equals("/") || split.get(i-1).equals("(")){
                    split.remove(i);
                    split.set(i, "-" + split.get(i));
                }

            }
        }
    }
    public static void main(String... args){
        String str = infixToPostfix(Arrays.asList("100", "*", "(", "-","+","-", "5", "/", "10", ")", "-", "-","7"));
        System.out.println(calculatePostfix(str));
    }
}
