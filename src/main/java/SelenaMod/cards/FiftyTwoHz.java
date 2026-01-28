package SelenaMod.cards;

import SelenaMod.actions.PlayDiscardPailCardAction;
import SelenaMod.actions.PlayDrawPailCardAction;
import SelenaMod.actions.PlayHandCardAction;
import SelenaMod.effects.FiftyTwoHzEffect;
import SelenaMod.modifiers.NotTriggerYourselfModifier;
import SelenaMod.utils.ModHelper;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FiftyTwoHz extends CustomSelenaCard implements OnObtainCard{
    public static String ID= ModHelper.makeID(FiftyTwoHz.class.getSimpleName());

    public FiftyTwoHz() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        this.setDamage(26);
        this.setBlock(26);
        this.setMagic(1);
        this.exhaust=true;
        this.isEthereal=true;
        this.cardsToPreview=new Miracle();
    }

    @Override
    protected void upgradeMethod() {
        this.cardsToPreview.upgrade();
        this.upgradeDescription();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        AbstractDungeon.effectList.add(new FiftyTwoHzEffect());
        addToBot(new MakeTempCardInHandAction(this.cardsToPreview.makeStatEquivalentCopy(),this.magicNumber));
        addCustomDamageAction(abstractMonster, AbstractGameAction.AttackEffect.LIGHTNING);
        addCustomBlockAction();
        if(!CardModifierManager.hasModifier(this,NotTriggerYourselfModifier.ID)){
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    List<AbstractCard> drawCardsHz= AbstractDungeon.player.drawPile.group.stream().filter(card -> card.cardID.equals(FiftyTwoHz.ID)).collect(Collectors.toList());
                    List<AbstractCard> discardCardsHz= AbstractDungeon.player.discardPile.group.stream().filter(card -> card.cardID.equals(FiftyTwoHz.ID)).collect(Collectors.toList());
                    List<AbstractCard> handHz= AbstractDungeon.player.hand.group.stream().filter(card -> card.cardID.equals(FiftyTwoHz.ID)).collect(Collectors.toList());
                    int count=drawCardsHz.size()+discardCardsHz.size()+handHz.size();
                    if(count>0){
                        int rand=AbstractDungeon.cardRandomRng.random(0,count-1);
                        if(rand<drawCardsHz.size()) {
                            AbstractCard card = drawCardsHz.get(rand);
                            CardModifierManager.addModifier(card, new NotTriggerYourselfModifier());
                            addToTop(new PlayDrawPailCardAction(card,abstractMonster));
                        }else if(rand<drawCardsHz.size()+discardCardsHz.size()){
                            AbstractCard card=discardCardsHz.get(rand-drawCardsHz.size());
                            CardModifierManager.addModifier(card, new NotTriggerYourselfModifier());
                            addToTop(new PlayDiscardPailCardAction(card,abstractMonster));
                        }else{
                            AbstractCard card=handHz.get(rand-drawCardsHz.size()-discardCardsHz.size());
                            CardModifierManager.addModifier(card, new NotTriggerYourselfModifier());
                            addToTop(new PlayHandCardAction(card,abstractMonster));
                        }
                    }
                    this.isDone=true;
                }
            });
        }


    }


    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && (CardModifierManager.hasModifier(this, NotTriggerYourselfModifier.ID) || findOtherFiftyTowHz(this) && super.canUse(p, m));
    }

    public static boolean findOtherFiftyTowHz(AbstractCard c){
        List<AbstractCard> cards=new ArrayList<>();
        cards.addAll(AbstractDungeon.player.drawPile.group);
        cards.addAll(AbstractDungeon.player.discardPile.group);
        cards.addAll(AbstractDungeon.player.hand.group);
        return cards.stream().anyMatch(card -> card.cardID.equals(FiftyTwoHz.ID)&&card!=c);
    }

    @Override
    public void onObtainCard() {
        if(ModHelper.GetSaveValue().GetFiftyTwoHz<1){
            ModHelper.GetSaveValue().GetFiftyTwoHz++;
        }
    }

    @SpirePatch(clz = AbstractRoom.class,method = "alterCardRarityProbabilities")
    public static class AbstractRoomPatch{
        @SpirePostfixPatch
        public static void postfix(AbstractRoom __instance, @ByRef int[] ___rareCardChance){
            if(ModHelper.GetSaveValue().GetFiftyTwoHz==0){
                ___rareCardChance[0]*=3;
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class,method = "getRewardCards")
    public static class AbstractDungeonPatch{
        @SpirePostfixPatch
        public static ArrayList<AbstractCard> postfix(ArrayList<AbstractCard> ___result){
            if(ModHelper.GetSaveValue().GetFiftyTwoHz==0){
                ModHelper.logger.info("change rare card to FiftyTwoHz");
                int index=-1;
                for (int i = 0; i < ___result.size(); i++) {
                    if(___result.get(i).rarity==CardRarity.RARE){
                        index=i;
                        break;
                    }
                }
                if(index!=-1){
                    AbstractCard newCard= CardLibrary.getCard(FiftyTwoHz.ID).makeCopy();
                    if(___result.get(index).upgraded){
                        newCard.upgrade();
                    }
                    ___result.set(index,newCard);
                }
            }
            return ___result;
        }
    }


}
