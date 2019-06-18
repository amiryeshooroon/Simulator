package Utilities.AutoPilot.Intepeter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Lexer {
    public static String[] lexer(String code){
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
    /*
openDataServer 5400 10
connect 127.0.0.1 5402
var breaks = bind "/controls/flight/speedbrake"
var throttle = bind " /controls/engines/current-engine/throttle"
var heading = bind "/instrumentation/heading-indicator/offset-deg"
var airspeed = bind "/instrumentation/airspeed-indicator/indicated-speed-kt"
var roll = bind "/instrumentation/attitude-indicator/indicated-roll-deg"
var pitch = bind "/instrumentation/attitude-indicator/internal-pitch-deg"
var rudder = bind "/controls/flight/rudder"
 var aileron = bind "/controls/flight/aileron"
 var elevator = bind "/controls/flight/elevator"
 var alt = bind "/instrumentation/altimeter/indicated-altitude-ft"
 breaks = 0
 throttle = 1
 var h0 = heading
 while alt < 1000 {
 rudder = (h0 – heading)/20
 aileron = - roll / 70
 elevator = pitch / 50
 print alt
 sleep 250
 }
 print "done"

     */
    public static void main(String[] args){
        long timeNow = System.currentTimeMillis();
//        String[] strings = lexer("openDataServer 5400 10\n" +
////                "connect 127.0.0.1 5402\n" +
////                "var breaks = bind \"/controls/flight/speedbrake\"\n" +
////                "var throttle = bind \" /controls/engines/current-engine/throttle\"\n" +
////                "var heading = bind \"/instrumentation/heading-indicator/offset-deg\"\n" +
////                "var airspeed = bind \"/instrumentation/airspeed-indicator/indicated-speed-kt\"\n" +
////                "var roll = bind \"/instrumentation/attitude-indicator/indicated-roll-deg\"\n" +
////                "var pitch = bind \"/instrumentation/attitude-indicator/internal-pitch-deg\"\n" +
////                "var rudder = bind \"/controls/flight/rudder\"\n" +
////                " var aileron = bind \"/controls/flight/aileron\"\n" +
////                " var elevator = bind \"/controls/flight/elevator\"\n" +
////                " var alt = bind \"/instrumentation/altimeter/indicated-altitude-ft\"\n" +
////                " breaks = 0\n" +
////                " throttle = 1\n" +
////                " var h0 = heading\n" +
////                " while alt < 1000 {\n" +
////                " rudder = (h0 – heading)/20\n" +
////                " aileron = - roll / 70\n" +
////                " elevator = pitch / 50\n" +
////                " print alt\n" +
////                " sleep 250\n" +
////                " }\n" +
////                " print \"done\"");
        String[] strings = lexer("return 730 * 5 - (8+2)");
        for(String str : strings) System.out.println(str);
        System.out.println(System.currentTimeMillis() - timeNow);
    }
}
