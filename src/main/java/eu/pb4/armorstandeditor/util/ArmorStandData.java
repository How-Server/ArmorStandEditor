package eu.pb4.armorstandeditor.util;

import eu.pb4.armorstandeditor.mixin.ArmorStandEntityAccessor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.EulerAngle;

public class ArmorStandData {
    public float yaw = -1;
    public boolean noGravity = false;
    public boolean hidePlate = false;
    public boolean small = false;
    public boolean showArms = false;
    public boolean invisible = false;
    public EulerAngle headRotation = new EulerAngle(0,0,0);
    public EulerAngle bodyRotation = new EulerAngle(0,0,0);
    public EulerAngle leftArmRotation = new EulerAngle(0,0,0);
    public EulerAngle rightArmRotation = new EulerAngle(0,0,0);
    public EulerAngle leftLegRotation = new EulerAngle(0,0,0);
    public EulerAngle rightLegRotation = new EulerAngle(0,0,0);
    public boolean customNameVisible = false;
    public Text customName = null;

    public boolean hasInventory = false;
    public ItemStack headItem = ItemStack.EMPTY;
    public ItemStack feetItem = ItemStack.EMPTY;
    public ItemStack legsItem = ItemStack.EMPTY;
    public ItemStack chestItem = ItemStack.EMPTY;
    public ItemStack mainHandItem = ItemStack.EMPTY;
    public ItemStack offhandItem = ItemStack.EMPTY;
    public int disabledSlots = 0;
    public float scale = 1;

    public ArmorStandData() {}

    public ArmorStandData(ArmorStandEntity armorStand) {
        this.copyFrom(armorStand);
    }

    public void copyFrom(ArmorStandEntity armorStand) {
        ArmorStandEntityAccessor asea = (ArmorStandEntityAccessor) armorStand;
        this.yaw = armorStand.getYaw();
        this.noGravity = armorStand.hasNoGravity();
        this.hidePlate = armorStand.shouldHideBasePlate();
        this.small = armorStand.isSmall();
        this.showArms = armorStand.shouldShowArms();
        this.invisible = armorStand.isInvisible();
        this.headRotation = armorStand.getHeadRotation();
        this.bodyRotation = armorStand.getBodyRotation();
        this.leftArmRotation = armorStand.getLeftArmRotation();
        this.rightArmRotation = armorStand.getRightArmRotation();
        this.leftLegRotation = armorStand.getLeftLegRotation();
        this.rightLegRotation = armorStand.getRightLegRotation();

        this.scale = armorStand.getScale();

        this.customNameVisible = armorStand.isCustomNameVisible() && armorStand.hasCustomName();
        if (armorStand.getCustomName() != null) {
            this.customName = armorStand.getCustomName().copy();
        }

        this.hasInventory = true;
        this.headItem = armorStand.getEquippedStack(EquipmentSlot.HEAD).copy();
        this.chestItem = armorStand.getEquippedStack(EquipmentSlot.CHEST).copy();
        this.legsItem = armorStand.getEquippedStack(EquipmentSlot.LEGS).copy();
        this.feetItem = armorStand.getEquippedStack(EquipmentSlot.FEET).copy();
        this.mainHandItem = armorStand.getEquippedStack(EquipmentSlot.MAINHAND).copy();
        this.offhandItem = armorStand.getEquippedStack(EquipmentSlot.OFFHAND).copy();


        this.disabledSlots = asea.getDisabledSlots();
    }

    public void apply(ArmorStandEntity armorStand, boolean modifyInventory) {
        ArmorStandEntityAccessor asea = (ArmorStandEntityAccessor) armorStand;
        if (this.yaw != -1) {
            armorStand.setYaw(this.yaw);
            double posX = armorStand.getX();
            double posY = armorStand.getY();
            double posZ = armorStand.getZ();
            armorStand.updatePositionAndAngles(posX, posY, posZ, this.yaw, 0);
        }
        armorStand.setNoGravity(this.noGravity);
        armorStand.noClip = this.noGravity;
        asea.callSetHideBasePlate(this.hidePlate);
        asea.callSetSmall(this.small);
        asea.callSetShowArms(this.showArms);
        armorStand.setInvisible(this.invisible);
        armorStand.setHeadRotation(this.headRotation);
        armorStand.setBodyRotation(this.bodyRotation);
        armorStand.setLeftArmRotation(this.leftArmRotation);
        armorStand.setRightArmRotation(this.rightArmRotation);
        armorStand.setLeftLegRotation(this.leftLegRotation);
        armorStand.setRightLegRotation(this.rightLegRotation);

        armorStand.setCustomNameVisible(this.customNameVisible);
        armorStand.setCustomName(this.customName);
        asea.setDisabledSlots(this.disabledSlots);

        armorStand.getAttributeInstance(EntityAttributes.GENERIC_SCALE).setBaseValue(scale);

        if (modifyInventory && this.hasInventory) {
            armorStand.equipStack(EquipmentSlot.HEAD, this.headItem);
            armorStand.equipStack(EquipmentSlot.CHEST, this.chestItem);
            armorStand.equipStack(EquipmentSlot.LEGS, this.legsItem);
            armorStand.equipStack(EquipmentSlot.FEET, this.feetItem);
            armorStand.equipStack(EquipmentSlot.MAINHAND, this.mainHandItem);
            armorStand.equipStack(EquipmentSlot.OFFHAND, this.offhandItem);
        }
    }
}
