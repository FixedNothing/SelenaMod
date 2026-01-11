package SelenaMod.powers;

import SelenaMod.utils.ModHelper;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class DearYouPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makeID(DearYouPower.class.getSimpleName());
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DearYouPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        ModHelper.initPower(this);
    }

    public static boolean canMakeTempCard(AbstractCard c) {
        return !(AbstractDungeon.player.hasPower(DearYouPower.POWER_ID) && (c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(strings.DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        addToBot(new SelectCardsInHandAction(this.amount, "", true, true, c -> true, this::callback));
    }

    private void callback(List<AbstractCard> cards) {
        for (AbstractCard c : cards) {
            AbstractDungeon.player.hand.moveToExhaustPile(c);
        }
        addToTop(new HealAction(this.owner, this.owner, cards.size() * 2));
        cards.clear();
    }

    //todo:不能印卡效果再弄把
    @SpirePatch(clz = MakeTempCardAtBottomOfDeckAction.class, method = "update")
    public static class PurityPatch1 {
        public static SpireReturn<Void> Prefix(MakeTempCardAtBottomOfDeckAction $this) {
            if (!DearYouPower.canMakeTempCard(new Madness())) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MakeTempCardInDiscardAction.class, method = "update")
    public static class PurityPatch2 {
        public static SpireReturn<Void> Prefix(MakeTempCardInDiscardAction $this, AbstractCard ___c) {
            if (!DearYouPower.canMakeTempCard(___c)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = "update")
    public static class PurityPatch3 {
        public static SpireReturn<Void> Prefix(MakeTempCardInDrawPileAction $this, AbstractCard ___cardToMake) {
            if (!DearYouPower.canMakeTempCard(___cardToMake)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, method = "update")
    public static class PurityPatch4 {
        public static SpireReturn<Void> Prefix(MakeTempCardInHandAction $this, AbstractCard ___c) {
            if (!DearYouPower.canMakeTempCard(___c)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "<ctor>", paramtypez = {AbstractCard.class})
    public static class PurityPatch5 {
        public static SpireReturn<Void> Prefix(ShowCardAndAddToHandEffect $this, AbstractCard card) {
            if (!DearYouPower.canMakeTempCard(card)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                ReflectionHacks.setPrivate($this, ShowCardAndAddToHandEffect.class, "card", new Madness());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "<ctor>", paramtypez = {AbstractCard.class, float.class, float.class})
    public static class PurityPatch5_1 {
        public static SpireReturn<Void> Prefix(ShowCardAndAddToHandEffect $this, AbstractCard card, float offsetX, float offsetY) {
            if (!DearYouPower.canMakeTempCard(card)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                ReflectionHacks.setPrivate($this, ShowCardAndAddToHandEffect.class, "card", new Madness());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "<ctor>", paramtypez = {AbstractCard.class})
    public static class PurityPatch6 {
        public static SpireReturn<Void> Prefix(ShowCardAndAddToDiscardEffect $this, AbstractCard card) {
            if (!DearYouPower.canMakeTempCard(card)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                ReflectionHacks.setPrivate($this, ShowCardAndAddToDiscardEffect.class, "card", new Madness());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "<ctor>", paramtypez = {AbstractCard.class, float.class, float.class})
    public static class PurityPatch6_2 {
        public static SpireReturn<Void> Prefix(ShowCardAndAddToDiscardEffect $this, AbstractCard card, float x, float y) {
            if (!DearYouPower.canMakeTempCard(card)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                ReflectionHacks.setPrivate($this, ShowCardAndAddToDiscardEffect.class, "card", new Madness());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
    public static class PurityPatch6_3 {
        public static void Prefix(ShowCardAndAddToDiscardEffect $this, @ByRef AbstractCard[] ___card) {
            if (___card[0] == null)
                ___card[0] = (AbstractCard) new Madness();
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "<ctor>", paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class})
    public static class PurityPatch7 {
        public static SpireReturn<Void> Prefix(ShowCardAndAddToDrawPileEffect $this, AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom) {
            if (!DearYouPower.canMakeTempCard(srcCard)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                ReflectionHacks.setPrivate($this, ShowCardAndAddToDrawPileEffect.class, "card", new Madness());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "<ctor>", paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class PurityPatch7_2 {
        public static SpireReturn<Void> Prefix(ShowCardAndAddToDrawPileEffect $this, AbstractCard srcCard, boolean randomSpot, boolean toBottom) {
            if (!DearYouPower.canMakeTempCard(srcCard)) {
                AbstractDungeon.player.getPower(DearYouPower.POWER_ID).flash();
                $this.isDone = true;
                ReflectionHacks.setPrivate($this, ShowCardAndAddToDrawPileEffect.class, "card", new Madness());
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = "<ctor>", paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
    public static class ShowCardObtainLogPatch1 {
        public static void Prefix(ShowCardAndObtainEffect $this, AbstractCard card, float x, float y, boolean convergeCards) {
            try {
                throw new Exception();
            } catch (Exception ex) {
                StringWriter buffer = new StringWriter();
                PrintWriter writer = new PrintWriter(buffer);
                ex.printStackTrace(writer);
                return;
            }
        }
    }

}
