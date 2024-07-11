package eu.pb4.armorstandeditor;

import eu.pb4.armorstandeditor.config.ConfigManager;
import me.drex.itsours.claim.AbstractClaim;
import me.drex.itsours.claim.list.ClaimList;
import me.drex.itsours.claim.flags.FlagsManager;
import me.drex.itsours.claim.flags.node.Node;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Optional;

public enum EditorActions {
    OPEN_EDITOR("open_editor"),
    OPEN_ITEM_FRAME_EDITOR("open_item_frame_editor"),
    MOVE("move"),
    ROTATE("move"),

    TOGGLE_PROPERTIES("change_properties"),
    TOGGLE_SIZE("toggle_size"),
    TOGGLE_ARMS("toggle_arms"),
    TOGGLE_VISIBILITY("toggle_visibility"),
    TOGGLE_GRAVITY("toggle_no_gravity"),
    TOGGLE_BASE("toggle_base"),

    MODIFY_POSE("modify_pose"),
    MODIFY_LEFT_ARM("modify_pose"),
    MODIFY_RIGHT_ARM("modify_pose"),
    MODIFY_LEFT_LEG("modify_pose"),
    MODIFY_RIGHT_LEG("modify_pose"),
    MODIFY_HEAD("modify_pose"),
    MODIFY_BODY("modify_pose"),

    RESET_POSE("modify_pose"),
    FLIP_POSE("modify_pose"),

    COPY("copy"),
    PASTE("paste"),
    INVENTORY("inventory"),
    RENAME("rename");


    public final String permission;

    EditorActions(String permission) {
        this.permission = permission;
    }

    public boolean canUse(PlayerEntity player) {
        Optional<AbstractClaim> claim = ClaimList.getClaimAt(player);
        if (!player.getMainHandStack().getItem().equals(Items.FLINT)) {
            return false;
        }
        if (claim.isPresent() && !claim.get().checkAction(player.getUuid(), FlagsManager.INTERACT_ENTITY, Node.registry(Registries.ENTITY_TYPE, EntityType.ARMOR_STAND))){
            player.sendMessage(Text.literal("您無權限調整此物品").formatted(Formatting.RED), true);
            return false;
        }else {
            return Permissions.check(player, "armor_stand_editor.action." + this.permission,
                    ConfigManager.getConfig().configData.allowedByDefault.contains(this.permission) ? 0 : 2
        );
    }
}
}
