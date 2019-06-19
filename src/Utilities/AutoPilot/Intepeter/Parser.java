package Utilities.AutoPilot.Intepeter;

import Exceptions.CodeErrorException;
import Utilities.AutoPilot.Expression.Function;
import Utilities.AutoPilot.Intepeter.Commands.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//import javax.activation.CommandMap;

public class Parser {
    private HashMap<String, Command> commandMap;
    private Deque<Scope> scopes;
    private ConcurrentHashMap<String, Double> bindVarValueMap;
    private HashMap<String, Function> globalFuncitonMap;
    public HashMap<String, Command> getCommandMap() {
        return commandMap;
    }
    public Scope getLastScope() {
        return scopes.getLast();
    }
    public Scope addScope() {
        scopes.addLast(new Scope());
        return scopes.getLast();
    }
    public void removeLastScope(){
        scopes.pollLast();
    }
    public ConcurrentHashMap<String, Double> getBindVarValueMap() {
        return bindVarValueMap;
    }
    public HashMap<String, Function> getGlobalFuncitonMap() {
        return globalFuncitonMap;
    }

    private static class ParserHolder{
        public static final Parser p = new Parser();
    }
    public static Parser getInstance(){
        return ParserHolder.p;
    }
    //String[] code;
    private Parser(){
        commandMap = new HashMap<>();
        commandMap.put("print", new PrintCommand());
        commandMap.put("openDataServer", new OpenDSCommand());
        commandMap.put("var", new NewVarCommand());
        commandMap.put("connect", new ConnectCommand());
        commandMap.put("while", new LoopCommand());
        commandMap.put("if", new ConditionCommand());
        commandMap.put("sleep", new SleepCommand());
        commandMap.put("=", new SetCommand());
        commandMap.put("= bind", new SetBindCommand());
        commandMap.put("return", new ReturnCommand());
        commandMap.put("disconnect", new DisconnectCommand());
        bindVarValueMap = new ConcurrentHashMap<>();
        bindVarValueMap.put("/instrumentation/airspeed-indicator/indicated-speed-kt", 0.0);
        bindVarValueMap.put("/instrumentation/altimeter/indicated-altitude-ft", 0.0);
        bindVarValueMap.put("/instrumentation/altimeter/pressure-alt-ft", 0.0);
        bindVarValueMap.put("/instrumentation/attitude-indicator/indicated-pitch-deg", 0.0);
        bindVarValueMap.put("/instrumentation/attitude-indicator/indicated-roll-deg", 0.0);
        bindVarValueMap.put("/instrumentation/attitude-indicator/internal-pitch-deg", 0.0);
        bindVarValueMap.put("/instrumentation/attitude-indicator/internal-roll-deg", 0.0);
        bindVarValueMap.put("/instrumentation/encoder/indicated-altitude-ft", 0.0);
        bindVarValueMap.put("/instrumentation/encoder/pressure-alt-ft", 0.0);
        bindVarValueMap.put("/instrumentation/gps/indicated-altitude-ft", 0.0);
        bindVarValueMap.put("/instrumentation/gps/indicated-ground-speed-kt", 0.0);
        bindVarValueMap.put("/instrumentation/gps/indicated-vertical-speed", 0.0);
        bindVarValueMap.put("/instrumentation/heading-indicator/indicated-heading-deg", 0.0);
        bindVarValueMap.put("/instrumentation/magnetic-compass/indicated-heading-deg", 0.0);
        bindVarValueMap.put("/instrumentation/slip-skid-ball/indicated-slip-skid", 0.0);
        bindVarValueMap.put("/instrumentation/turn-indicator/indicated-turn-rate", 0.0);
        bindVarValueMap.put("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", 0.0);
        bindVarValueMap.put("/controls/flight/aileron", 0.0);
        bindVarValueMap.put("/controls/flight/elevator", 0.0);
        bindVarValueMap.put("/controls/flight/rudder", 0.0);
        bindVarValueMap.put("/controls/flight/flaps", 0.0);
        bindVarValueMap.put("/controls/engines/engine/throttle", 0.0);
        bindVarValueMap.put("/engines/engine/rpm", 0.0);
        scopes = new ArrayDeque<>();
        scopes.addLast(new Scope());
    }
    public double parse(String codeStr) throws CodeErrorException {
        String[] code = Lexer.lexer(codeStr);
        ((ReturnCommand)commandMap.get("return")).setReturnValue(0);
        int len = code.length;
        Command command;
        List<Object> argumentList;
        for(int i = 0; i < len;){
            // commandMap.getOrDefault(code[i], stackScope.getLast().getOrDefault(code[i], new ErrorCodeCommand());
            argumentList = new ArrayList<>();
            if((command = commandMap.get(code[i])) != null){
                if(command instanceof ReturnCommand){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    i += command.getArguments(code, i, argumentList);
                } catch (Exception e) {
                    e.printStackTrace();
                    //check what to do
                }
                try {
                    command.doCommand(argumentList);
                } catch (Exception e) {
                    throw new CodeErrorException();
                }
                if(command instanceof ReturnCommand){
//                    removeLastScope();

                    return ((ReturnCommand)commandMap.get("return")).getReturnValue();
                }
            }
            else ++i;
        }
        return ((ReturnCommand)commandMap.get("return")).getReturnValue();
    }

}
