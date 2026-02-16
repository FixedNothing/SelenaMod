package SelenaMod.powers;

import SelenaMod.cards.Letter;
import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;
import java.util.stream.Collectors;

public class SirenPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makeID(SirenPower.class.getSimpleName());
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public int hpOnEnter = -1;
    public SirenPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        ModHelper.initPower(this);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        this.hpOnEnter = this.owner.currentHealth;
    }

    public static boolean IsInSiren() {
        return AbstractDungeon.player.powers.stream().anyMatch(p -> p instanceof SirenPower);
    }

    @Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            List<AbstractCard> letters = AbstractDungeon.player.hand.group.stream().filter(c -> c instanceof Letter).collect(Collectors.toList());
            if (!letters.isEmpty()) {
                AbstractCard card = letters.get(AbstractDungeon.cardRandomRng.random(letters.size() - 1));
                addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
            } else {
                addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, GameActionManager.turn, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

//                addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, GameActionManager.turn));
            }
        }
    }
}
