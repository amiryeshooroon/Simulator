package Utilities.AutoPilot.Intepeter.Commands;

import Exceptions.UpdateTypes;
import Search.Pair;
import Utilities.AutoPilot.Exceptions.ServerAlreadyAliveException;
import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class OpenDSCommand implements Command {
    private volatile boolean stop;
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception{
        return StringToArgumentParser.parse(args, idx, 3, emptyList, TypeArguments.String, TypeArguments.Integer, TypeArguments.Integer);
    }
    private void runServer(int port, int timesASec){
        BufferedReader br = null;
        boolean sentOnce = false;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(1000);
            while (!stop) {
                try (Socket client = serverSocket.accept()) {
                    br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    while (!stop) {
                        String line = br.readLine();
                        if (line == null) continue;
                        String[] variables = line.split(",");
                        ConcurrentHashMap<String, Double> serverValues = Parser.getInstance().getBindVarValueMap();
                        serverValues.put("/instrumentation/airspeed-indicator/indicated-speed-kt", Double.parseDouble(variables[0]));
                        serverValues.put("/instrumentation/altimeter/indicated-altitude-ft", Double.parseDouble(variables[1]));
                        serverValues.put("/instrumentation/altimeter/pressure-alt-ft", Double.parseDouble(variables[2]));
                        serverValues.put("/instrumentation/attitude-indicator/indicated-pitch-deg", Double.parseDouble(variables[3]));
                        serverValues.put("/instrumentation/attitude-indicator/indicated-roll-deg", Double.parseDouble(variables[4]));
                        serverValues.put("/instrumentation/attitude-indicator/internal-pitch-deg", Double.parseDouble(variables[5]));
                        serverValues.put("/instrumentation/attitude-indicator/internal-roll-deg", Double.parseDouble(variables[6]));
                        serverValues.put("/instrumentation/encoder/indicated-altitude-ft", Double.parseDouble(variables[7]));
                        serverValues.put("/instrumentation/encoder/pressure-alt-ft", Double.parseDouble(variables[8]));
                        serverValues.put("/instrumentation/gps/indicated-altitude-ft", Double.parseDouble(variables[9]));
                        serverValues.put("/instrumentation/gps/indicated-ground-speed-kt", Double.parseDouble(variables[10]));
                        serverValues.put("/instrumentation/gps/indicated-vertical-speed", Double.parseDouble(variables[11]));
                        serverValues.put("/instrumentation/heading-indicator/indicated-heading-deg", Double.parseDouble(variables[12]));
                        serverValues.put("/instrumentation/magnetic-compass/indicated-heading-deg", Double.parseDouble(variables[13]));
                        serverValues.put("/instrumentation/slip-skid-ball/indicated-slip-skid", Double.parseDouble(variables[14]));
                        serverValues.put("/instrumentation/turn-indicator/indicated-turn-rate", Double.parseDouble(variables[15]));
                        serverValues.put("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", Double.parseDouble(variables[16]));
                        serverValues.put("/controls/flight/aileron", Double.parseDouble(variables[17]));
                        serverValues.put("/controls/flight/elevator", Double.parseDouble(variables[18]));
                        serverValues.put("/controls/flight/rudder", Double.parseDouble(variables[19]));
                        serverValues.put("/controls/flight/flaps", Double.parseDouble(variables[20]));
                        serverValues.put("/controls/engines/engine/throttle", Double.parseDouble(variables[21]));
                        serverValues.put("/engines/engine/rpm", Double.parseDouble(variables[22]));
                        if(!sentOnce) {
                            synchronized (this) {
                                notifyAll();
                            }
                            sentOnce = true;
                        }
                        try {
                            Thread.sleep(1000 / timesASec);
                        } catch (InterruptedException e1) {
                        }
                    }
                }catch(SocketTimeoutException e){/*didn't get client in this second*/}
            }
        } catch (IOException e) {}
        if(br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void doCommand(List<Object> args) throws ServerAlreadyAliveException {
        stop = false;
        synchronized (this) {
            new Thread(() -> runServer((int) args.get(1), (int) args.get(2))).start(); //test
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        Parser p = Parser.getInstance();
        p.myModel.setModelChanged();
        p.myModel.notifyObservers(new Integer(1));
        System.out.println("BEFORE ");
        System.out.println("DONEEEEE");

    }
    public void stopTimer(){
        stop = true;
    }
}
