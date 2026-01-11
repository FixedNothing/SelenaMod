package SelenaMod.cards;

import SelenaMod.powers.SirenPower;
import SelenaMod.utils.ModHelper;
import SelenaMod.utils.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public class OutOfControl extends CustomSelenaCard {
    public static String ID = ModHelper.makeID(OutOfControl.class.getSimpleName());
    private static Texture NewPaintBg = TextureLoader.getTexture(ModHelper.makeImgPath("cards", "OutOfControlBG"));
    private static Texture NewPaintBG_BIG = TextureLoader.getTexture(ModHelper.makeImgPath("cards", "OutOfControlBG_BIG"));
    public OutOfControl() {
        super(ID, -2, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.isEthereal = true;
    }

    @Override
    protected void upgradeMethod() {

    }


    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (!SirenPower.IsInSiren()) {
            this.addPowerToSelf(new SirenPower(AbstractDungeon.player));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @SpireOverride
    public void renderCardBg(SpriteBatch sb, float x, float y) {


        sb.setColor(Color.WHITE.cpy());
        sb.draw(NewPaintBg, x - NewPaintBg.getWidth() / 2.0F, y - NewPaintBg.getHeight() / 2.0F,
                NewPaintBg.getWidth() / 2.0F, NewPaintBg.getHeight() / 2.0F, NewPaintBg.getWidth(), NewPaintBg.getHeight(),
                this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, NewPaintBg.getWidth(), NewPaintBg.getHeight(), false, false);
//        SpireSuper.call(sb,x,y);

    }

    @SpireOverride
    public void renderPortrait(SpriteBatch sb) {

    }

    @SpireOverride
    public void renderType(SpriteBatch sb) {

    }

    @SpireOverride
    public void renderBannerImage(SpriteBatch sb, float x, float y) {

    }

    @SpireOverride
    public void renderPortraitFrame(SpriteBatch sb, float x, float y) {

    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBack")
    public static class RenderCardBackPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof OutOfControl) {
                RenderCardBackInSingleCardView(sb, ___card);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    public static void RenderCardBackInSingleCardView(SpriteBatch sb, AbstractCard ___card) {
        sb.setColor(Color.WHITE.cpy());
        sb.draw(NewPaintBG_BIG, Settings.WIDTH / 2.0F - NewPaintBG_BIG.getWidth() / 2.0F,
                Settings.HEIGHT / 2.0F - NewPaintBG_BIG.getHeight() / 2.0F,
                NewPaintBG_BIG.getWidth() / 2.0F, NewPaintBG_BIG.getHeight() / 2.0F,
                NewPaintBG_BIG.getWidth(), NewPaintBG_BIG.getHeight(),
                Settings.scale,
                Settings.scale, 0.0F, 0, 0, NewPaintBG_BIG.getWidth(), NewPaintBG_BIG.getHeight(),
                false, false);

//        sb.draw(REPLACE_IMG, drawX, drawY + 72.0F,
//                125.0F, 23.0F,
//                250.0F, 190.0F,
//                this.drawScale * Settings.scale, this.drawScale * Settings.scale,
//                this.angle, 0, 0, 250, 190,
//                false, false);
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderPortrait")
    public static class RenderPortraitPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof OutOfControl) {
                RenderPortraitInSingleCardView(sb, ___card);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static void RenderPortraitInSingleCardView(SpriteBatch sb, AbstractCard card) {

        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBanner")
    public static class RenderCardBannerPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof OutOfControl) {
                RenderCardBannerInSingleCardView();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static void RenderCardBannerInSingleCardView() {

        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText")
    public static class RenderCardTypeTextPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof OutOfControl) {
                RenderCardTypeTextInSingleCardView();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static void RenderCardTypeTextInSingleCardView() {

        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
    public static class RenderFramePatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> prefix(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof OutOfControl) {
                RenderFrameInSingleCardView();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static void RenderFrameInSingleCardView() {

        }
    }
}
