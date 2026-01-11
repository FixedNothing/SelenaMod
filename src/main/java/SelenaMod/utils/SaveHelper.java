package SelenaMod.utils;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.google.gson.JsonElement;

public class SaveHelper implements CustomSavable<SaveHelper.SaveValue> {

    public SaveValue values = new SaveValue();

    public SaveHelper() {
        BaseMod.addSaveField(ModHelper.MOD_ID, this);
    }

    @Override
    public SaveValue onSave() {
        return null;
    }

    @Override
    public void onLoad(SaveValue saveValue) {

    }

    @Override
    public JsonElement onSaveRaw() {
        return ModHelper.gson.toJsonTree(this.values);
    }

    @Override
    public void onLoadRaw(JsonElement value) {
        if (value != null) {
            this.values = ModHelper.gson.fromJson(value, SaveValue.class);
        }
    }

    public static class SaveValue {
        public int GetFiftyTwoHz=-1;//-1未激活，0激活，1以结束
        public int TelepathyWrongGuessCount = 0;
    }
}
