package SelenaMod.patches;

import SelenaMod.utils.CardMoveEventBus;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CardMoveEvent {

    public final CardGroup.CardGroupType source;
    public final CardGroup.CardGroupType target;
    public final AbstractCard card;

    public CardMoveEvent(CardGroup.CardGroupType source, CardGroup.CardGroupType target, AbstractCard card) {
        this.source = source;
        this.target = target;
        this.card = card;
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class CardMoveFlag {
        public static SpireField<Boolean> isMoving = new SpireField<>(() -> false);
        public static SpireField<CardGroup.CardGroupType> cardGroup = new SpireField<>(() -> null);
    }

    @SpirePatches({
            @SpirePatch(clz = CardGroup.class, method = "addToHand"),
            @SpirePatch(clz = CardGroup.class, method = "addToTop"),
            @SpirePatch(clz = CardGroup.class, method = "addToBottom"),
            @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot")
    })
    public static class CardMovePatch {
        public static void Postfix(CardGroup __instance, AbstractCard card) {
            if (!CardMoveFlag.isMoving.get(card) && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE && __instance.type != CardGroup.CardGroupType.UNSPECIFIED) {
                CardGroup.CardGroupType source = CardMoveFlag.cardGroup.get(card);
                CardMoveEventBus.GetInstance().publish(new CardMoveEvent(source, __instance.type, card));
                CardMoveFlag.cardGroup.set(card, __instance.type);
            }
        }
    }

    @SpirePatches({
            @SpirePatch(clz = CardGroup.class, method = "moveToHand", paramtypez = {AbstractCard.class, CardGroup.class}),
            @SpirePatch(clz = CardGroup.class, method = "moveToHand", paramtypez = {AbstractCard.class}),
    })
    public static class SpecialMovePatch1 {
        public static void Prefix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveFlag.isMoving.set(card, true);
            }
        }

        public static void Postfix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveEventBus.GetInstance().publish(new CardMoveEvent(__instance.type, CardGroup.CardGroupType.HAND, card));
                CardMoveFlag.cardGroup.set(card, __instance.type);
                CardMoveFlag.isMoving.set(card, false);
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "empower")
    public static class SpecialMovePatch2 {
        public static void Prefix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveFlag.isMoving.set(card, true);
            }
        }

        public static void Postfix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveEventBus.GetInstance().publish(new CardMoveEvent(__instance.type, null, card));
                CardMoveFlag.cardGroup.set(card, __instance.type);
                CardMoveFlag.isMoving.set(card, false);
            }
        }
    }

    @SpirePatches({
            @SpirePatch(clz = CardGroup.class, method = "moveToBottomOfDeck"),
            @SpirePatch(clz = CardGroup.class, method = "moveToDeck")
    })
    public static class SpecialMovePatch3 {
        public static void Prefix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveFlag.isMoving.set(card, true);
            }
        }

        public static void Postfix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveEventBus.GetInstance().publish(new CardMoveEvent(__instance.type, CardGroup.CardGroupType.DRAW_PILE, card));
                CardMoveFlag.cardGroup.set(card, __instance.type);
                CardMoveFlag.isMoving.set(card, false);
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToDiscardPile")
    public static class SpecialMovePatch4 {
        public static void Prefix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveFlag.isMoving.set(card, true);
            }
        }

        public static void Postfix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveEventBus.GetInstance().publish(new CardMoveEvent(__instance.type, CardGroup.CardGroupType.DISCARD_PILE, card));
                CardMoveFlag.cardGroup.set(card, __instance.type);
                CardMoveFlag.isMoving.set(card, false);
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class SpecialMovePatch5 {
        public static void Prefix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveFlag.isMoving.set(card, true);
            }
        }

        public static void Postfix(CardGroup __instance, AbstractCard card) {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE) {
                CardMoveEventBus.GetInstance().publish(new CardMoveEvent(__instance.type, CardGroup.CardGroupType.EXHAUST_PILE, card));
                CardMoveFlag.cardGroup.set(card, __instance.type);
                CardMoveFlag.isMoving.set(card, false);
            }
        }
    }
}