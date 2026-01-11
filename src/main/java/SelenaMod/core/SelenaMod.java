package SelenaMod.core;

import SelenaMod.actions.AskForRemoveIdealismAction;
import SelenaMod.actions.PlayDrawPailCardAction;
import SelenaMod.cards.*;
import SelenaMod.character.Selena;
import SelenaMod.modifiers.ReduceCostModifier;
import SelenaMod.patches.ReturnRandomRelicPatch;
import SelenaMod.potions.DeveloperFluid;
import SelenaMod.potions.LoopLiquid;
import SelenaMod.potions.RedTideSample;
import SelenaMod.relics.*;
import SelenaMod.utils.*;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.PandorasBox;
import com.megacrit.cardcrawl.relics.StrikeDummy;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.megacrit.cardcrawl.core.Settings.language;

@SpireInitializer
public class SelenaMod implements ISubscriber, EditStringsSubscriber, EditKeywordsSubscriber, EditCharactersSubscriber,
        EditCardsSubscriber, EditRelicsSubscriber, PostInitializeSubscriber, OnPlayerTurnStartSubscriber, PostBattleSubscriber,
        PostUpdateSubscriber, PostDungeonInitializeSubscriber, StartActSubscriber {

    public static final Color SELENA_COLOR = new Color(0.8f, 0.8f, 1.0f, 1.0f);
    public static final String SELENA_ATTACK_512 = ModHelper.makeImgPath("512", "bg_attack_512");
    public static final String SELENA_SKILL_512 = ModHelper.makeImgPath("512", "bg_skill_512");
    public static final String SELENA_POWER_512 = ModHelper.makeImgPath("512", "bg_power_512");
    public static final String SELENA_ATTACK_1024 = ModHelper.makeImgPath("1024", "bg_attack_1024");
    public static final String SELENA_SKILL_1024 = ModHelper.makeImgPath("1024", "bg_skill_1024");
    public static final String SELENA_POWER_1024 = ModHelper.makeImgPath("1024", "bg_power_1024");

    public static final String SELENA_SMALL_ORB = ModHelper.makeImgPath("512", "small_orb");
    public static final String SELENA_BIG_ORB = ModHelper.makeImgPath("1024", "card_orb");
    public static final String SELENA_ENERGY_ORB = ModHelper.makeImgPath("1024", "cost_orb");

    public static final String SELENA_CHARACTER_BUTTON = ModHelper.makeImgPath("char", "Character_Button");
    public static final String SELENA_CHARACTER_PORTRAIT = ModHelper.makeImgPath("char", "Character_Portrait");

    public static SaveHelper saveHelper;

    public static boolean LOSE_HP_THIS_TURN = false;

    public static int DAMAGED_THIS_TURN=0;

    public SelenaMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(ModHelper.getSelenaColor(), SELENA_COLOR, SELENA_COLOR, SELENA_COLOR, SELENA_COLOR, SELENA_COLOR, SELENA_COLOR, SELENA_COLOR, SELENA_ATTACK_512, SELENA_SKILL_512, SELENA_POWER_512, SELENA_BIG_ORB, SELENA_ATTACK_1024, SELENA_SKILL_1024, SELENA_POWER_1024, SELENA_ENERGY_ORB, SELENA_SMALL_ORB);

        saveHelper = new SaveHelper();

    }


    public static void initialize() {
        new SelenaMod();
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(ModHelper.MOD_ID).packageFilter(CustomSelenaCard.class)
                .notPackageFilter("SelenaMod.cards.options").setDefaultSeen(true).cards();

        BaseMod.addDynamicVariable(EffectsDynamicVariableManager.instance);
        BaseMod.addDynamicVariable(new SecondMagicVar());


    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Selena(CardCrawlGame.playerName), SELENA_CHARACTER_BUTTON, SELENA_CHARACTER_PORTRAIT);
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "ENG";
        if (language == Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else if (language == Settings.GameLanguage.ENG) {
            //todo: make English translate
            lang = "ZHS";
        } else if (language == Settings.GameLanguage.ZHT) {
            lang = "ZHS";
        }
        String json = Gdx.files.internal(ModHelper.makeLocalizationPath(lang, "keyword")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(StringUtils.lowerCase(ModHelper.MOD_PRE), keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new PaperAndPen(), ModHelper.getSelenaColor());
        BaseMod.addRelicToCustomPool(new GeometricShards(), ModHelper.getSelenaColor());
        ReturnRandomRelicPatch.replaceRelicMap.put(PandorasBox.ID, GeometricShards.ID);
        BaseMod.addRelicToCustomPool(new FriendABrush(), ModHelper.getSelenaColor());
        ReturnRandomRelicPatch.replaceRelicMap.put(StrikeDummy.ID, FriendABrush.ID);
        BaseMod.addRelicToCustomPool(new PaperPenHeart(), ModHelper.getSelenaColor());
        BaseMod.addRelic(new Hamlet(), RelicType.SHARED);
    }

    @Override
    public void receiveEditStrings() {
        String lang = "ENG";
        if (com.megacrit.cardcrawl.core.Settings.language == com.megacrit.cardcrawl.core.Settings.GameLanguage.ZHS) {
            lang = "ZHS";
        } else if (com.megacrit.cardcrawl.core.Settings.language == com.megacrit.cardcrawl.core.Settings.GameLanguage.ENG) {
            // todo：make English translate
            lang = "ZHS";
        } else if (com.megacrit.cardcrawl.core.Settings.language == com.megacrit.cardcrawl.core.Settings.GameLanguage.ZHT) {
            lang = "ZHS";
        }
        BaseMod.loadCustomStringsFile(CardStrings.class, ModHelper.makeLocalizationPath(lang, "card"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, ModHelper.makeLocalizationPath(lang, "character"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, ModHelper.makeLocalizationPath(lang, "power"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, ModHelper.makeLocalizationPath(lang, "relic"));
        BaseMod.loadCustomStringsFile(UIStrings.class, ModHelper.makeLocalizationPath(lang, "uistring"));
        BaseMod.loadCustomStringsFile(EventStrings.class, ModHelper.makeLocalizationPath(lang, "event"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, ModHelper.makeLocalizationPath(lang, "potion"));

    }

    @Override
    public void receivePostInitialize() {
        BaseMod.addPotion(LoopLiquid.class, LoopLiquid.liquidColor, LoopLiquid.hybridColor, LoopLiquid.spotsColor, LoopLiquid.ID);
        BaseMod.addPotion(DeveloperFluid.class, DeveloperFluid.liquidColor, DeveloperFluid.hybridColor, DeveloperFluid.spotsColor, DeveloperFluid.ID);
        BaseMod.addPotion(RedTideSample.class, RedTideSample.liquidColor, RedTideSample.hybridColor, RedTideSample.spotsColor, RedTideSample.ID);
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        LOSE_HP_THIS_TURN = false;
        DAMAGED_THIS_TURN=0;
        CardMoveEventBus.GetInstance().onStartOfTurn();
    }

    private static void updateSelfQuestion() {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            List<AbstractCardModifier> modifierList = CardModifierManager.getModifiers(card, ReduceCostModifier.ID);
            if (!modifierList.isEmpty()) {
                for (AbstractCardModifier modifier : modifierList) {
                    if (modifier instanceof ReduceCostModifier) {
                        ((ReduceCostModifier) modifier).count--;
                        if (((ReduceCostModifier) modifier).count < 0) {
                            CardModifierManager.removeSpecificModifier(card, modifier, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        updateSelfQuestion();
        updateIdealism();
    }

    private void updateIdealism() {
        boolean hasIdealism = false;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.cardID.equals(Idealism.ID)) {
                hasIdealism = true;
                break;
            }
        }
        if (hasIdealism) {
            AbstractDungeon.actionManager.addToTop(new AskForRemoveIdealismAction());
        }
    }

    @Override
    public void receivePostUpdate() {
        if (AbstractDungeon.currMapNode != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player != null) {
            if (AbstractDungeon.actionManager.isEmpty() && !AbstractDungeon.player.isEndingTurn) {
                if (AbstractDungeon.player.drawPile.size() == 1) {
                    if (AbstractDungeon.player.drawPile.getTopCard() instanceof SoloPerformance) {
                        SoloPerformance soloPerformance = (SoloPerformance) AbstractDungeon.player.drawPile.getTopCard();
                        soloPerformance.activeFlag = true;
                        AbstractDungeon.actionManager.addToBottom(new PlayDrawPailCardAction(AbstractDungeon.player.drawPile.getTopCard(), null));
                    }
                }
                if(AbstractDungeon.player.discardPile.size()==1){
                    if(AbstractDungeon.player.discardPile.getTopCard() instanceof Casablanca){
                        Casablanca casablanca=(Casablanca) AbstractDungeon.player.discardPile.getTopCard();
                        AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(casablanca,AbstractDungeon.player.discardPile));
                        AbstractDungeon.actionManager.addToBottom(new ScryAction(casablanca.magicNumber));
                    }
                }
            }
        }
    }

    @Override
    public void receivePostDungeonInitialize() {
        saveHelper.values = new SaveHelper.SaveValue();
    }

    @Override
    public void receiveStartAct() {
        if (AbstractDungeon.id.equals("TheEnding")) {
            List<AbstractCard> cardList = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.cardID.equals(Idealism.ID))
                    .collect(Collectors.toList());
            int index = 0;
            int total = cardList.size();
            for (AbstractCard card : cardList) {
                card.untip();
                card.unhover();
                AbstractCard hero = new Heroism();
                hero.drawScale = hero.targetDrawScale = 0.7F;
                if (card.upgraded) {
                    hero.upgrade();
                }
                float xOffset = AbstractCard.IMG_WIDTH * 0.35F;
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(hero,
                        Settings.WIDTH / 2.0F - xOffset * (total - index), Settings.HEIGHT / 2.0F));
                AbstractDungeon.player.masterDeck.removeCard(card);
            }
        }
    }
}
