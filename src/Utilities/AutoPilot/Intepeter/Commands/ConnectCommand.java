package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.SimulatorServer;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.util.List;

public class ConnectCommand implements Command {
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 3, emptyList, TypeArguments.String, TypeArguments.String, TypeArguments.Integer);
    }

    @Override
    public void doCommand(List<Object> args) {
        String ip = (String)args.get(1);
        int port = (Integer) args.get(2);
        SimulatorServer.getServer().open(ip, port);
    }
}
