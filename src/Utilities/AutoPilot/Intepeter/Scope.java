package Utilities.AutoPilot.Intepeter;

import Utilities.AutoPilot.Intepeter.Variables.Var;

import java.util.HashMap;

public class Scope {
    private HashMap<String, Var> scope;
    private Scope innerScope;
    public Scope(){
        scope = new HashMap<>();
    }
    public void addVar(String str, Var var){if(innerScope == null) scope.put(str, var); else innerScope.addVar(str, var);}
    public void addInnerScope(){if(innerScope == null) innerScope = new Scope(); else innerScope.addInnerScope();}
    public boolean disposeInnerScope(){if(innerScope == null) return false; if(!innerScope.disposeInnerScope()) innerScope = null; return true;}
    public Var getVar(String nameVar){
        Var var = null;
        if(innerScope != null) var = innerScope.getVar(nameVar);
        if(var == null) return scope.get(nameVar);
        return var;
    }
}
