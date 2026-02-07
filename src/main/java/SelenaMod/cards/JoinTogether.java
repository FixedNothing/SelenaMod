package SelenaMod.cards;

import SelenaMod.powers.JoinTogetherPower;
import SelenaMod.utils.ModHelper;
import SelenaMod.utils.SelenaEnums;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JoinTogether extends CustomSelenaCard {
    public static String ID = ModHelper.makeID("JoinTogether");

    public JoinTogether() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        this.exhaust=true;
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeDescription();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        if (this.upgraded) {
            addToBot(new MultiGroupSelectAction("", this::callback, 2, false, this::check, CardGroup.CardGroupType.DRAW_PILE, CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DISCARD_PILE));
        } else {
            addToBot(new SelectCardsInHandAction(2, "", false, false, this::check, this::handcallback));
        }
    }

    private boolean check(AbstractCard card) {
        return !card.hasTag(SelenaEnums.JOIN_TOGETHER);
    }

    private void callback(List<AbstractCard> abstractCards, Map<AbstractCard, CardGroup> abstractCardCardGroupMap) {

    }

    private void handcallback(List<AbstractCard> cards) {
        if (cards.size() == 2) {
            ArrayList<AbstractCard> cardsList = new ArrayList<>(cards);
            AbstractCard card = cardsList.get(0);
            AbstractCard finalCard = card;
            Optional<AbstractCard> instance = AbstractDungeon.player.hand.group.stream().filter(c -> c.uuid.equals(finalCard.uuid)).findFirst();
            instance.ifPresent(c -> cardsList.set(0, c));
            card = cardsList.get(1);
            AbstractCard finalCard1 = card;
            instance = AbstractDungeon.player.hand.group.stream().filter(c -> c.uuid.equals(finalCard1.uuid)).findFirst();
            instance.ifPresent(c -> cardsList.set(1, c));
            int totalCost = cardsList.stream().mapToInt(c -> c.costForTurn).sum();
            cardsList.forEach(c -> {
                c.cost = totalCost / 2;
                c.costForTurn = c.cost;
                c.isCostModified = true;
                c.tags.add(SelenaEnums.JOIN_TOGETHER);
            });
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new JoinTogetherPower(AbstractDungeon.player, cardsList.get(0), cardsList.get(1))));
        } else {
            ModHelper.logger.error("What fuck!This is not good.");
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && useCondition(p);
    }

    private boolean useCondition(AbstractPlayer p) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.addAll(p.hand.group);
        if (this.upgraded) {
            cards.addAll(p.drawPile.group);
            cards.addAll(p.discardPile.group);
        }
        return cards.stream().filter(c -> c != this && !c.tags.contains(SelenaEnums.JOIN_TOGETHER)).count() >= 2;
    }
}
