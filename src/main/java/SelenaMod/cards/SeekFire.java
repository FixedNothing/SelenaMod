package SelenaMod.cards;

import SelenaMod.actions.SeekFireAction;
import SelenaMod.modifiers.SeekFireModifier;
import SelenaMod.utils.ModHelper;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SeekFire extends CustomSelenaCard {
    public static String ID = ModHelper.makeID("SeekFire");

    public SeekFire() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.setDamage(2);
        this.setMagic(2);
        this.setSecondMagic(1);
        this.exhaust = true;
        CardModifierManager.addModifier(this, new SeekFireModifier());
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeDescription();
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        for (int i = 0; i < this.magicNumber; i++) {
            addToBot(new SeekFireAction(abstractMonster, this, new DamageInfo(abstractPlayer, this.damage, DamageInfo.DamageType.NORMAL), this.secondMagicVar, i == this.magicNumber - 1));
        }
    }
}
