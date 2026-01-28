package SelenaMod.cards;

import SelenaMod.actions.FlowerTreeAction;
import SelenaMod.actions.PlayHandCardAction;
import SelenaMod.utils.ModHelper;
import SelenaMod.utils.TextureLoader;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FlowerTree extends CustomSelenaCard {
    public static String ID = ModHelper.makeID("FlowerTree");
    private static Texture REPLACE_IMG = TextureLoader.getTexture(ModHelper.makeImgPath("cards", "FlowerTree2"));
    public float textureReplaceDuration = 1.0F;

    public FlowerTree() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.setDamage(24);
        this.setBlock(10);
        this.exhaust = true;
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeDamage(8);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addCustomDamageAction(abstractMonster, AbstractGameAction.AttackEffect.FIRE);
        addToBot(new FlowerTreeAction(this.block));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                FlowerTree.this.textureReplaceDuration -= Gdx.graphics.getDeltaTime();
                if (FlowerTree.this.textureReplaceDuration <= 0.0F) {
                    FlowerTree.this.textureReplaceDuration = 0.0F;
                    this.isDone = true;
                }
            }
        });
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        addToTop(new PlayHandCardAction(this, null));
    }

    @SpireOverride
    public void renderPortrait(SpriteBatch sb) {
        if (this.textureReplaceDuration >= 1.0F)
            SpireSuper.call(sb);
        else {
            Color color1 = ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");
            float originA = color1.a;
            color1.a = this.textureReplaceDuration;
            SpireSuper.call(sb);
            Color color2 = Color.WHITE.cpy();
            color2.a = 1.0F - this.textureReplaceDuration;
            sb.setColor(color2);
            float drawX = this.current_x - 125.0F;
            float drawY = this.current_y - 95.0F;
            sb.draw(REPLACE_IMG, drawX, drawY + 72.0F,
                    125.0F, 23.0F,
                    250.0F, 190.0F,
                    this.drawScale * Settings.scale, this.drawScale * Settings.scale,
                    this.angle, 0, 0, 250, 190,
                    false, false);
            color1.a = originA;
        }

    }
}
