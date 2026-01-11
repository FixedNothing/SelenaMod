package SelenaMod.patches;

import SelenaMod.cards.CustomSelenaCard;
import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.helpers.TipHelper.FakeKeywords;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.PowerTipFlavorFields;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class RenderTipPatch {
    private static final String HEADER_STRING = "@STSLIB:FLAVOR@";
    private static final String SELENA_HEADER_STRING = "@SELENA:FLAVOR@";

    @SpirePatch(clz = FlavorText.PassScvTooltip.class, method = "Insert")
    public static class PassScvTooltipPatch {
        @SpirePostfixPatch
        public static void Insert(SingleCardViewPopup __instance, ArrayList<PowerTip> t) {
            AbstractCard card = ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
            if (card instanceof CustomSelenaCard) {
                Optional<PowerTip> tipOptional = t.stream().filter(tip -> tip.header.equals(HEADER_STRING)).findFirst();
                if (tipOptional.isPresent()) {
                    tipOptional.get().header = SELENA_HEADER_STRING;
                    t.remove(tipOptional.get());
                    t.add(0, tipOptional.get());
                }
            }

        }

    }


    @SpirePatch(clz = FlavorText.TipHelperRenderPowerTips.class, method = "Prefix")
    public static class TipHelperRenderPowerTipsPatch {
        @SpirePostfixPatch
        public static void postfix(float[] y, ArrayList<PowerTip> powerTips) {
            for (PowerTip tip : powerTips) {
                if (tip.header.equals(SELENA_HEADER_STRING)) {
                    tip.header = HEADER_STRING;
                }
            }
        }
    }

    @SpirePatch(clz = FakeKeywords.class, method = "Prefix")
    public static class FakeKeywordsPatch {
        @SpirePrefixPatch
        public static void prefix(float x, float[] y, SpriteBatch sb, ArrayList<String> keywords, AbstractCard ___card) {

            AbstractCard card = ___card;
            if (Objects.nonNull(card) && card instanceof CustomSelenaCard) {
                String flavor = FlavorText.AbstractCardFlavorFields.flavor.get(card);
                PowerTip tip = new PowerTip(HEADER_STRING, flavor);
                PowerTipFlavorFields.textColor.set(tip, FlavorText.AbstractCardFlavorFields.textColor.get(card));
                PowerTipFlavorFields.boxColor.set(tip, FlavorText.AbstractCardFlavorFields.boxColor.get(card));
                PowerTipFlavorFields.flavorBoxType.set(tip, FlavorText.AbstractCardFlavorFields.flavorBoxType.get(card));
                PowerTipFlavorFields.boxBot.set(tip, FlavorText.AbstractCardFlavorFields.boxBot.get(card));
                PowerTipFlavorFields.boxMid.set(tip, FlavorText.AbstractCardFlavorFields.boxMid.get(card));
                PowerTipFlavorFields.boxTop.set(tip, FlavorText.AbstractCardFlavorFields.boxTop.get(card));
                BitmapFont flavorFont = ReflectionHacks.getPrivateStatic(FlavorText.class, "flavorFont");
                float BODY_TEXT_WIDTH = ReflectionHacks.getPrivate(null, TipHelper.class, "BODY_TEXT_WIDTH");
                float TIP_DESC_LINE_SPACING = ReflectionHacks.getPrivate(null, TipHelper.class, "TIP_DESC_LINE_SPACING");
                float BOX_EDGE_H = ReflectionHacks.getPrivate(null, TipHelper.class, "BOX_EDGE_H");
                float h = -FontHelper.getSmartHeight(flavorFont, tip.body, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING)
                        - 40.0F * Settings.scale;
                h += BOX_EDGE_H * 3.15F;
                ReflectionHacks.privateStaticMethod(FlavorText.class, "addFlavorTip",
                        float.class, float.class, SpriteBatch.class, PowerTip.class).invoke(new Object[]{x, y[0] + h, sb, tip});
            }
        }

    }

    @SpirePatch(clz = FlavorText.FlavorAfterCustomTooltips.class, method = "Postifx")
    public static class FlavorAfterCustomTooltipsPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(float x, float[] y, SpriteBatch sb, ArrayList<String> keywords) {
            try {
                Field cardField = TipHelper.class.getDeclaredField("card");
                cardField.setAccessible(true);
                AbstractCard card = (AbstractCard) cardField.get((Object) null);
                if (card instanceof CustomSelenaCard) {
                    return SpireReturn.Return();
                }
                return SpireReturn.Continue();
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return SpireReturn.Continue();
        }
    }
}
