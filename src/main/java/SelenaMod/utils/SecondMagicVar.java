package SelenaMod.utils;

import SelenaMod.cards.CustomSelenaCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondMagicVar extends DynamicVariable {
    public static String ID= ModHelper.makeID(SecondMagicVar.class.getSimpleName());

    @Override
    public String key() {
        return ID;
    }

    @Override
    public boolean isModified(AbstractCard abstractCard) {
        if (abstractCard instanceof CustomSelenaCard) {
            return ((CustomSelenaCard) abstractCard).isSecondMagicModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard abstractCard) {
        if(abstractCard instanceof CustomSelenaCard){
            return ((CustomSelenaCard) abstractCard).secondMagicVar;
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard abstractCard) {
        if(abstractCard instanceof CustomSelenaCard){
            return ((CustomSelenaCard) abstractCard).baseSecondMagicVar;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard abstractCard) {
        AbstractCard card=abstractCard.makeCopy();
//        card.upgrade();
        return baseValue(abstractCard)!=baseValue(card);
    }


    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof CustomSelenaCard) {
            ((CustomSelenaCard) card).isSecondMagicModified = v;
        }
    }

}
