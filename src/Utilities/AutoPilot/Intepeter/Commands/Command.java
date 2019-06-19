package Utilities.AutoPilot.Intepeter.Commands;

import java.util.List;

public interface Command {
    int getArguments(String[] args, int idx, List<Object> emptyList) throws Exception;
    void doCommand(List<Object> args) throws Exception;
}
