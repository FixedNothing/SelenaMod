package SelenaMod.modifiers;

import SelenaMod.utils.ModHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ReduceCostThisTurnModifier extends AbstractCardModifier {
    public static String ID = ModHelper.makeID(ReduceCostThisTurnModifier.class.getSimpleName());
    public int amount;
    public int count;
    private int active = 0;
    private boolean removed = false;

    public ReduceCostThisTurnModifier() {
        this(1, 1);
    }

    public ReduceCostThisTurnModifier(int amount, int count) {
        this.amount = amount;
        this.count = count;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        ReduceCostThisTurnModifier copy = new ReduceCostThisTurnModifier();
        copy.count = this.count;
        copy.amount = this.amount;
        return copy;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        this.active = Math.min(card.cost, this.amount);
        card.cost = Math.max(0, card.cost - this.amount);
        card.costForTurn = card.cost;
        card.isCostModified = this.active > 0;
    }

//    @Override
//    public boolean onBattleStart(AbstractCard card) {
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                for(AbstractCard card1: AbstractDungeon.player.masterDeck.group){
//                    if(card1.uuid.equals(card.uuid)){
//                        ArrayList<AbstractCardModifier> modifierList= CardModifierManager.getModifiers(card1,ReduceCostModifier.ID);
//                        for(AbstractCardModifier modifier:modifierList){
//                            if(modifier instanceof ReduceCostModifier){
//                                ((ReduceCostModifier) modifier).amount--;
//                                if(((ReduceCostModifier) modifier).amount==0){
//                                    CardModifierManager.removeSpecificModifier(card1,modifier,true);
//                                }
//                            }
//                        }
//                        break;
//                    }
//                }
//                this.isDone=true;
//            }
//        });
//        return true;
//    }

    @Override
    public void onRemove(AbstractCard card) {
        if (!this.removed) {
            super.onRemove(card);
            card.cost += this.active;
            ModHelper.logger.info("remove ReduceCostThisTurnModifier{}:{},current cost is {}", card.name, card.uuid, card.cost);
            card.costForTurn = card.cost;
            card.isCostModified = false;
        }
        this.removed = true;

    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }
}
