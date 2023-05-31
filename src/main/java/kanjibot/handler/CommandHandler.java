package kanjibot.handler;

import kanjibot.commands.Command;
import kanjibot.factory.CommandFactory;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CommandHandler {
    private final CommandFactory commandFactory;
    private final TelegramLongPollingBot bot;

    public CommandHandler(TelegramLongPollingBot bot) {
        this.bot = bot;
        this.commandFactory = new CommandFactory();
    }

    public void handleCommand(Update update) {
        String fullCommand = update.getMessage().getText().substring(1); // removes the "/"
        String commandText;

        if (fullCommand.contains("@")) {
            commandText = fullCommand.split("@")[0];
        } else {
            commandText = fullCommand;
        }

        Command command = commandFactory.createCommand(commandText);

        if (command != null) {
            SendMessage message = command.execute(update);
            message.setChatId(update.getMessage().getChatId().toString());

            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
