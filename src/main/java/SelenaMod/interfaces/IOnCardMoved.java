package SelenaMod.interfaces;

import SelenaMod.patches.CardMoveEvent;

public interface IOnCardMoved {
    void onCardMoved(CardMoveEvent event);

    void onStartOfTurn();
}
