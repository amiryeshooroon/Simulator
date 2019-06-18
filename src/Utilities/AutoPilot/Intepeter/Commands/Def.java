package Utilities.AutoPilot.Intepeter.Commands;

import java.util.List;

public class Def implements Command {
    @Override
    public int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception{
        return 0;
    }

    @Override
    public void doCommand(List<Object> args) {

    }
}
