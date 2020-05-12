package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;

public interface Command {

    boolean execute() throws InterruptFileSaveException;
}
