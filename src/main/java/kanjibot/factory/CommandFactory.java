package kanjibot.factory;

import kanjibot.commands.*;

public class CommandFactory {
    public Command createCommand(String command) {
        return switch (command) {
            case "start" -> new StartCommand();
            case "help" -> new HelpCommand();
            default -> new UnknownCommand();
        };
    }
}
