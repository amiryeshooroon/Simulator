package Utilities.AutoPilot.Intepeter.Commands;

import Utilities.AutoPilot.Intepeter.InterpterUtilities.StringToArgumentParser;
import Utilities.AutoPilot.Intepeter.Parser;
import Utilities.AutoPilot.Intepeter.SimulatorServer;
import Utilities.AutoPilot.Intepeter.TypeArguments;

import java.util.List;

public class DisconnectCommand implements Command {
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception {
        return StringToArgumentParser.parse(args, idx, 1, emptyList, TypeArguments.String);
    }

    @Override
    public void doCommand(List<Object> args) throws Exception {
        SimulatorServer.getServer().sendString("bye");
        ((OpenDSCommand)Parser.getInstance().getCommandMap().get("openDataServer")).stopTimer();
        SimulatorServer.getServer().stop();
    }
}
