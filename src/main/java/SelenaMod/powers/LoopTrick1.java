package SelenaMod.powers;

import SelenaMod.modifiers.ReduceCostThisTurnModifier;
import SelenaMod.utils.ModHelper;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.bothInterfaces.OnCreateCardInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LoopTrick1 extends AbstractPower implements OnCreateCardInterface {
    public static final String POWER_ID = ModHelper.makeID(LoopTrick1.class.getSimpleName());
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LoopTrick1(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        ModHelper.initPower(this);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(strings.DESCRIPTIONS[0], this.amount);
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void onInitialApplication() {
        CardGroup[] groups = new CardGroup[]{AbstractDungeon.player.drawPile, AbstractDungeon.player.discardPile, AbstractDungeon.player.hand};
        for (CardGroup g : groups) {
            for (AbstractCard card : g.group) {
                CardModifierManager.addModifier(card, new ReduceCostThisTurnModifier());
            }
        }
    }

    @Override
    public void onRemove() {
//        CardGroup[] groups = new CardGroup[]{AbstractDungeon.player.drawPile, AbstractDungeon.player.discardPile, AbstractDungeon.player.hand};
//        for (CardGroup g : groups) {
//            for (AbstractCard card : g.group) {
//                CardModifierManager.removeModifiersById(card, ReduceCostThisTurnModifier.ID, false);
//            }
//        }
    }

    @Override
    public void onCreateCard(AbstractCard abstractCard) {
        CardModifierManager.addModifier(abstractCard, new ReduceCostThisTurnModifier());
    }
}
