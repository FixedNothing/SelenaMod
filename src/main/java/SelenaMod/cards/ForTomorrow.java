package SelenaMod.cards;

import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ForTomorrow extends CustomSelenaCard {
    public static String ID = ModHelper.makeID("ForTomorrow");

    public ForTomorrow() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.setDamage(10);
        this.setMagic(1);
        this.cardsToPreview = new Miracle();
        this.isMultiDamage = true;
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeDescription();
        this.cardsToPreview.upgrade();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new DamageAllEnemiesAction(abstractPlayer, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new MakeTempCardInDrawPileAction(this.cardsToPreview, this.magicNumber, false, false, true));
    }
}
