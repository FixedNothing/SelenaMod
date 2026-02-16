package SelenaMod.modifiers;

import SelenaMod.utils.ModHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SeekFireModifier extends AbstractCardModifier {
    public static String ID = ModHelper.makeID("SeekFireModifier");
    private int damageAmount;
    private int magicAmount;
    private AbstractCard targetCard;

    public SeekFireModifier(int damageAmount, int magicAmount) {
        this.damageAmount = damageAmount;
        this.magicAmount = magicAmount;
    }

    public SeekFireModifier() {
        this(0, 0);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SeekFireModifier(damageAmount, magicAmount);
    }

    public void addDamage(int amount) {
        this.damageAmount += amount;
        targetCard.baseDamage += amount;
    }

    public void addMagic(int amount) {
        this.magicAmount += amount;
        targetCard.baseMagicNumber += amount;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        this.targetCard = card;
        card.baseDamage += damageAmount;
        card.baseMagicNumber += magicAmount;
    }
}
