package Utilities.AutoPilot.Intepeter.InterpterUtilities;

import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.Variables.Var;

public class Utilities {
    public static boolean checkDouble(String str){
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
    public static Var getVar(String string){
        return Parser.getInstance().getLastScope().getVar(string);
    }
}
