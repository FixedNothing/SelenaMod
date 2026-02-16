package SelenaMod.relics;

import SelenaMod.cards.Letter;
import SelenaMod.utils.ModHelper;
import SelenaMod.utils.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class PaperAndPen extends CustomRelic {
    public static final String ID = ModHelper.makeID(PaperAndPen.class.getSimpleName());
    private static final Texture TEXTURE = TextureLoader.getTexture(ModHelper.makeRelicImagePath(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(ModHelper.makeRelicOutlinePath(ID));

    public PaperAndPen() {
        super(ID, TEXTURE, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        addToBot(new DrawCardAction(1));
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if(c instanceof Letter){
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, 2)));
        }
    }
}
