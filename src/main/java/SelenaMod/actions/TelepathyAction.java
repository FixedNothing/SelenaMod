package SelenaMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;

public class TelepathyAction extends AbstractGameAction {

    public TelepathyAction() {
        this.actionType = ActionType.WAIT;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> cards = new ArrayList<>(AbstractDungeon.player.drawPile.group);
            if (cards.isEmpty()) {
                this.isDone = true;
                return;
            }
            Collections.shuffle(cards);
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group = cards;
            AbstractDungeon.gridSelectScreen.open(group, 1, "", false, false);
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard answer = AbstractDungeon.player.drawPile.getTopCard();
            AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            if (answer == selected) {
                AbstractDungeon.player.drawPile.moveToHand(answer);
                answer.freeToPlayOnce = true;
                AbstractDungeon.player.hand.refreshHandLayout();
                addToTop(new TelepathyAction());
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
        }

        tickDuration();
    }
}
