package eu.pb4.armorstandeditor.legacy;

import eu.pb4.armorstandeditor.EditorActions;
import eu.pb4.armorstandeditor.util.ArmorStandData;
import eu.pb4.armorstandeditor.util.PlayerExt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

@Deprecated
public interface LegacyPlayerExt {
    Identifier LEGACY_UI = Identifier.of("armor_stand_editor", "use_legacy_ui");

    EditorActions aselegacy$getArmorStandEditorAction();
    void aselegacy$setArmorStandEditorAction(EditorActions action);
    float aselegacy$getArmorStandEditorPower();
    void aselegacy$setArmorStandEditorPower(float power);
    int aselegacy$getArmorStandEditorXYZ();
    void aselegacy$setArmorStandEditorXYZ(int xyz);
    default ArmorStandData aselegacy$getArmorStandEditorData() {
        return ((PlayerExt) this).ase$getArmorStandEditorData();
    }
    default void aselegacy$setArmorStandEditorData(ArmorStandData data) {
        ((PlayerExt) this).ase$setArmorStandEditorData(data);
    }


    static boolean useLegacy(PlayerEntity player) {
        return false;
    }
}
