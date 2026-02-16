package SelenaMod.cards;

import SelenaMod.cardEffects.PeonyPavilionEffect;
import SelenaMod.powers.OverridePower;
import SelenaMod.powers.SirenPower;
import SelenaMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;

public class PeonyPavilion extends CustomSelenaCard {
    public static String ID = ModHelper.makeID(PeonyPavilion.class.getSimpleName());

    public PeonyPavilion() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.setMagic(3);
        this.exhaust = true;
    }

    @Override
    protected void upgradeMethod() {
        this.selfRetain = true;
        this.upgradeDescription();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        // 检查是否处于塞壬状态
        if (abstractPlayer.hasPower(SirenPower.POWER_ID)) {
            // 处于塞壬状态：获得4点敏捷和1层幽魂形态
            addToBot(new ApplyPowerAction(abstractPlayer, abstractPlayer,
                    new DexterityPower(abstractPlayer, 4)));
            addToBot(new ApplyPowerAction(abstractPlayer, abstractPlayer,
                    new WraithFormPower(abstractPlayer, 1)));
        } else {
            // 不处于塞壬状态：获得残影
            addToBot(new ApplyPowerAction(abstractPlayer, abstractPlayer,
                    new BlurPower(abstractPlayer, this.magicNumber)));
        }
        addPowerToSelf(new OverridePower(abstractPlayer, 1, new PeonyPavilionEffect(this.cardID)));

    }
}