package SelenaMod.actions;

import SelenaMod.cards.SeekFire;
import SelenaMod.modifiers.SeekFireModifier;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Optional;

public class SeekFireAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard thisCard;
    private boolean killed;
    private int upgradeAmount;
    private boolean isLast;

    public SeekFireAction(AbstractMonster target, AbstractCard card, DamageInfo info, int upgradeAmount, boolean isLast) {
        this.thisCard = card;
        this.info = info;
        this.target = target;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.killed = false;
        this.upgradeAmount = upgradeAmount;
        this.isLast = isLast;
    }

    @Override
    public void update() {
        if (this.duration == 0.1F && this.target != null && !this.target.isDeadOrEscaped()) {
            this.target.damage(this.info);
            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower("Minion")) {
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
                this.killed = true;
            }
        }
        tickDuration();
        if (this.isDone) {
            if (thisCard instanceof SeekFire) {
                Optional<AbstractCard> deckCard = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.uuid.equals(thisCard.uuid)).findFirst();
                if (killed) {
                    SeekFireModifier modifier = getSeekFireModifier(thisCard);
                    modifier.addMagic(this.upgradeAmount);
                    if (deckCard.isPresent()) {
                        getSeekFireModifier(deckCard.get()).addMagic(this.upgradeAmount);
                    }
                } else if (this.isLast && !this.target.isDeadOrEscaped()) {
                    SeekFireModifier modifier = getSeekFireModifier(thisCard);
                    modifier.addDamage(this.upgradeAmount);
                    if (deckCard.isPresent()) {
                        getSeekFireModifier(deckCard.get()).addDamage(this.upgradeAmount);
                    }
                }

            }
        }
    }

    private static SeekFireModifier getSeekFireModifier(AbstractCard card) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, SeekFireModifier.ID);
        if (modifiers.isEmpty()) {
            SeekFireModifier modifier = new SeekFireModifier();
            CardModifierManager.addModifier(card, modifier);
            return modifier;
        }
        return (SeekFireModifier) modifiers.get(0);
    }
}
