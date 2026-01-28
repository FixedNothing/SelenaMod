package SelenaMod.cards;

import SelenaMod.actions.NaiveProphecyAction;
import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NaiveProphecy extends CustomSelenaCard {
    public static String ID = ModHelper.makeID(NaiveProphecy.class.getSimpleName());

    public NaiveProphecy() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.setMagic(5);
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeMagicNumber(2);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new NaiveProphecyAction(this.magicNumber));
    }
}
