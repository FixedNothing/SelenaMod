package SelenaMod.relics;

import SelenaMod.utils.ModHelper;
import SelenaMod.utils.TextureLoader;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Hamlet extends CustomRelic {
    public static final String ID = ModHelper.makeID(Hamlet.class.getSimpleName());
    private static final Texture TEXTURE = TextureLoader.getTexture(ModHelper.makeRelicImagePath(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(ModHelper.makeRelicOutlinePath(ID));

    public Hamlet() {
        super(ID, TEXTURE, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderHealth")
    public static class renderHealthPatch {
        @SpireInsertPatch(loc = 1019, localvars = {"x", "y"})
        public static SpireReturn<Void> prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
            if (__instance == AbstractDungeon.player && AbstractDungeon.player.hasRelic(Hamlet.ID)) {
                if (__instance.currentBlock != 0 && __instance.hbAlpha != 0.0F) {

                    ReflectionHacks.privateMethod(AbstractCreature.class, "renderBlockIconAndValue", SpriteBatch.class, float.class, float.class).invoke(__instance, sb, x, y);
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderPowerIcons")
    public static class renderPowerIcons {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
            if (__instance == AbstractDungeon.player && AbstractDungeon.player.hasRelic(Hamlet.ID)) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips")
    public static class renderPowerTips {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(AbstractPlayer __instance, SpriteBatch sb) {
            if (__instance == AbstractDungeon.player && AbstractDungeon.player.hasRelic(Hamlet.ID)) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class renderPatch {
        @SpirePrefixPatch
        public static void prefix(AbstractPlayer __instance, SpriteBatch sb) {
            if (AbstractDungeon.player.hasRelic(Hamlet.ID)) {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    card.isFlipped = true;
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class renderPatch2 {
        @SpirePostfixPatch
        public static void postfix(AbstractPlayer __instance, SpriteBatch sb) {
            if (AbstractDungeon.player.hasRelic(Hamlet.ID)) {
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    card.isFlipped = false;
                }
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "render")
    public static class renderPatch3 {
        @SpirePrefixPatch
        public static void prefix(CardGroup __instance, SpriteBatch sb) {
            if (AbstractDungeon.player.hasRelic(Hamlet.ID)) {
                for (AbstractCard card : __instance.group) {
                    card.isFlipped = true;
                }
            }
        }
    }
}
