package SelenaMod.cards;

import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Correspondence extends CustomSelenaCard{
    public static String ID= ModHelper.makeID(Correspondence.class.getSimpleName());
    private int letterNum=0;
    public Correspondence() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        this.setMagic(0);
        this.exhaust=true;
//        CardModifierManager.addModifier(this,new RepeatModifier());
        this.selfRetain = true;
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addDrawCardAction();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.calMagicNumber();
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard cardPlayed) {
        if(Letter.isLetterCard(cardPlayed)){
            this.letterNum++;
            calMagicNumber();
        }
    }

    private void calMagicNumber() {
        this.magicNumber = this.baseMagicNumber + this.letterNum / 2;
        this.isMagicNumberModified=this.baseMagicNumber!=this.magicNumber;
    }
}
