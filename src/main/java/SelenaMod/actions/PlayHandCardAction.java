package SelenaMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PlayHandCardAction extends AbstractGameAction {
    private AbstractCard card;

    public PlayHandCardAction(AbstractCard card, AbstractCreature target) {
        this.card = card;
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (!card.isInAutoplay) {
                AbstractDungeon.player.hand.removeCard(card);
                this.card.applyPowers();
                this.card.calculateCardDamage((AbstractMonster) this.target);
                AbstractDungeon.getCurrRoom().souls.remove(this.card);

                if (AbstractDungeon.player.limbo.group.isEmpty()) {
                    card.target_x = (float) Settings.WIDTH / 2.0F;
                } else {
                    card.target_x = (float) Settings.WIDTH / 2.0F - 200.0F * Settings.xScale;
                }
                card.target_y = (float) Settings.HEIGHT / 2.0F;
                card.targetAngle=0.0F;
                card.targetDrawScale=0.75F;
//                AbstractDungeon.player.limbo.group.add(this.card);

                card.isInAutoplay = true;

                if (this.target == null || this.target.isDeadOrEscaped()) {
                    addToTop(new NewQueueCardAction(card, true, false, true));
                } else {
                    addToTop(new NewQueueCardAction(card, this.target, false, true));
                }
     }
        }
        this.isDone = true;
    }
}
