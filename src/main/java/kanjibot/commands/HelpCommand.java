package kanjibot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand implements Command {
    @Override
    public SendMessage execute(Update update) {
        SendMessage message = new SendMessage();
        message.setText("Help");
        return message;
    }
}
