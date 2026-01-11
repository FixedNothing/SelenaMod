package SelenaMod.cards;

import SelenaMod.interfaces.IOnCardMoved;
import SelenaMod.patches.CardMoveEvent;
import SelenaMod.utils.CardMoveEventBus;
import SelenaMod.utils.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IncomingLetter extends CustomSelenaCard implements IOnCardMoved {
    public static String ID = ModHelper.makeID(IncomingLetter.class.getSimpleName());
    public boolean TURN_FLAG = true;

    public IncomingLetter() {
        super(ID, -2, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        setMagic(1);
        this.cardsToPreview = new Letter();
        CardMoveEventBus.GetInstance().register(this);
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }


    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

//    @Override
//    public void onOtherAddToHand(AbstractCard card) {
//        if(card instanceof Letter&&AbstractDungeon.player.drawPile.contains(this)){
//            this.flash();
//            addToBot(new FetchAction(AbstractDungeon.player.drawPile,c->c==this,null));
//            addToBot(new DrawCardAction(this.magicNumber));
//        }
//    }

    @Override
    public void onCardMoved(CardMoveEvent event) {
        if (event.target == CardGroup.CardGroupType.HAND) {
            if (event.card instanceof Letter && AbstractDungeon.player.drawPile.contains(this) && TURN_FLAG) {
                this.flash();
                this.TURN_FLAG = false;
                addToBot(new FetchAction(AbstractDungeon.player.drawPile, c -> c == this, null));
                addToBot(new DrawCardAction(this.magicNumber));
            }
        }
    }

    @Override
    public void onStartOfTurn() {
        TURN_FLAG = true;
    }
}
