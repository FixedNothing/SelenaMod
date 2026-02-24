package SelenaMod.cards;

import SelenaMod.cardEffects.DivineComedyEffect;
import SelenaMod.powers.DivineComedyPower;
import SelenaMod.powers.OverridePower;
import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DivineComedy extends CustomSelenaCard {
    public static String ID = ModHelper.makeID(DivineComedy.class.getSimpleName());

    public DivineComedy() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.setMagic(2);
        this.setSecondMagic(3);
        this.exhaust = true;
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addPowerToSelf(new DivineComedyPower(abstractPlayer, this.secondMagicVar));
        addTonePower(new OverridePower(abstractPlayer, this.magicNumber, new DivineComedyEffect(this.cardID, this.magicNumber)),abstractMonster);
    }
}
