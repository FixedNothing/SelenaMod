package SelenaMod.utils;

import SelenaMod.interfaces.IOnCardMoved;
import SelenaMod.patches.CardMoveEvent;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class CardMoveEventBus {
    Set<IOnCardMoved> weakSet =
            Collections.newSetFromMap(new WeakHashMap<>());

    private static CardMoveEventBus INSTANCE = new CardMoveEventBus();

    public void register(IOnCardMoved register) {
        weakSet.add(register);
    }

    public void unregister(IOnCardMoved register) {
        weakSet.remove(register);
    }

    public void publish(CardMoveEvent event) {
        for (IOnCardMoved card : weakSet) {
            card.onCardMoved(event);
        }
    }

    public static CardMoveEventBus GetInstance() {
        return INSTANCE;
    }

    public void onStartOfTurn() {
        for (IOnCardMoved moved : this.weakSet) {
            moved.onStartOfTurn();
        }
    }
}
