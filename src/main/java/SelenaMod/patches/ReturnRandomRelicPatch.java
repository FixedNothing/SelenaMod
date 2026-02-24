package SelenaMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.HashMap;
import java.util.Map;

public class ReturnRandomRelicPatch {
    public static final Map<String, String> replaceRelicMap = new HashMap<>();

    @SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
    public static class InitializeRelicListPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon __instance) {
            for (int i = 0; i < AbstractDungeon.commonRelicPool.size(); i++) {
                if (replaceRelicMap.containsKey(AbstractDungeon.commonRelicPool.get(i))) {
                    AbstractDungeon.commonRelicPool.set(i, replaceRelicMap.get(AbstractDungeon.commonRelicPool.get(i)));
                }
            }
            for (int i = 0; i < AbstractDungeon.uncommonRelicPool.size(); i++) {
                if (replaceRelicMap.containsKey(AbstractDungeon.uncommonRelicPool.get(i))) {
                    AbstractDungeon.uncommonRelicPool.set(i, replaceRelicMap.get(AbstractDungeon.uncommonRelicPool.get(i)));
                }
            }
            for (int i = 0; i < AbstractDungeon.rareRelicPool.size(); i++) {
                if (replaceRelicMap.containsKey(AbstractDungeon.rareRelicPool.get(i))) {
                    AbstractDungeon.rareRelicPool.set(i, replaceRelicMap.get(AbstractDungeon.rareRelicPool.get(i)));
                }
            }
        }
    }
}
