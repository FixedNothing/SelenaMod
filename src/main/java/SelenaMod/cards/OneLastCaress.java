package SelenaMod.cards;

import SelenaMod.utils.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.List;

public class OneLastCaress extends CustomSelenaCard {
    public static String ID = ModHelper.makeID("OneLastCaress");
    private static final CardStrings cardString = CardCrawlGame.languagePack.getCardStrings(ID);

    public OneLastCaress() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, SelfOrEnemyTargeting.SELF_OR_ENEMY);
        this.setMagic(18);
        this.setSecondMagic(99);
        this.selfRetain = true;
        this.isEthereal = true;
        this.exhaust = true;
    }

    @Override
    protected void upgradeMethod() {
        this.upgradeMagicNumber(4);
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == AbstractDungeon.player) {
            addToBot(new HealAction(target, target, this.magicNumber));
        } else {
            addToBot(new DamageAction(target, new DamageInfo(abstractPlayer, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }

        addToBot(new ApplyPowerAction(target, abstractPlayer, new StrengthPower(target, -this.secondMagicVar), -this.secondMagicVar, true, AbstractGameAction.AttackEffect.NONE));
        if (!target.hasPower(ArtifactPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(target, abstractPlayer, new GainStrengthPower(target, this.secondMagicVar), this.secondMagicVar, true, AbstractGameAction.AttackEffect.NONE));

        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null) {
            return true;
        }
        int life = target.currentHealth;
        List<AbstractCreature> creatureList = ModHelper.GetAllCreatures(true);
        for (AbstractCreature creature : creatureList) {
            if (creature.currentHealth < life) {
                this.cantUseMessage = cardString.EXTENDED_DESCRIPTION[0];
                return false;
            }
        }
        return super.canUse(p, m);
    }
}
