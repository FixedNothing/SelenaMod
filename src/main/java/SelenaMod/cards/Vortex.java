package SelenaMod.cards;

import SelenaMod.actions.VortexAction;
import SelenaMod.utils.ModHelper;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Vortex extends CustomSelenaCard{
    public static String ID= ModHelper.makeID("Vortex");
    public static AbstractCard[] PREVIEW_CARDS = {new BurningVow(), new DearYou()};
    public Vortex() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        for (AbstractCard card : PREVIEW_CARDS) {
            MultiCardPreview.add(this, card.makeCopy());
        }
        this.exhaust = true;
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeDescription();
        MultiCardPreview.clear(this);
        for (AbstractCard card : PREVIEW_CARDS) {
            AbstractCard cpy = card.makeCopy();
            cpy.upgrade();
            MultiCardPreview.add(this, cpy);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new VortexAction(this.upgraded));
    }
}
