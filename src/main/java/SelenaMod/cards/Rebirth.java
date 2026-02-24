package SelenaMod.cards;

import SelenaMod.cardEffects.ExhaustEffect;
import SelenaMod.powers.OverridePower;
import SelenaMod.powers.RebirthPower;
import SelenaMod.powers.SirenPower;
import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Rebirth extends CustomSelenaCard{
    public static String ID= ModHelper.makeID(Rebirth.class.getSimpleName());
    public Rebirth() {
        super(ID, 0, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeDescription();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addPowerToSelf(new RebirthPower(abstractPlayer));
        addToBot(new RemoveSpecificPowerAction(abstractPlayer,abstractPlayer, SirenPower.POWER_ID));
        if (!this.upgraded)
            addTonePower(new OverridePower(abstractPlayer, 1, new ExhaustEffect(this.cardID)), abstractMonster);
    }
}
