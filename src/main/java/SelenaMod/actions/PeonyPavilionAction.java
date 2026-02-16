package SelenaMod.actions;

import SelenaMod.powers.SirenPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PeonyPavilionAction extends AbstractGameAction {
    public PeonyPavilionAction() {
        this.actionType = ActionType.WAIT;
        this.duration = 0.1F;
    }

    @Override
    public void update() {
        if (this.duration == 0.1F) {
            //移除所有负面状态
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
                }
            }
            if (SirenPower.IsInSiren()) {
                SirenPower sirenPower = (SirenPower) AbstractDungeon.player.getPower(SirenPower.POWER_ID);
                AbstractDungeon.player.currentHealth = sirenPower.hpOnEnter == -1 ? AbstractDungeon.player.currentHealth : sirenPower.hpOnEnter;
                AbstractDungeon.player.healthBarUpdatedEvent();
                addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SirenPower.POWER_ID));
            }
            this.isDone = true;
        }
        tickDuration();
    }
}
