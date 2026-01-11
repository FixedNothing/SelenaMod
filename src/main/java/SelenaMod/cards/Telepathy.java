package SelenaMod.cards;

import SelenaMod.actions.TelepathyAction;
import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Telepathy extends CustomSelenaCard {
    public static String ID = ModHelper.makeID("Telepathy");

    public Telepathy() {
        super(ID, 3, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        this.setMagic(1);
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeDescription();
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new TelepathyAction());
    }
}
