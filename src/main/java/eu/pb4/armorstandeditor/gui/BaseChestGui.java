package eu.pb4.armorstandeditor.gui;

import eu.pb4.armorstandeditor.EditorActions;
import eu.pb4.armorstandeditor.util.TextUtils;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.HotbarGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public abstract class BaseChestGui extends SimpleGui {
    protected EditingContext context;
    public BaseChestGui(EditingContext context, ScreenHandlerType type, boolean withPlayerSlots) {
        super(type, context.player, withPlayerSlots);
        this.context = context;
    }

    @Override
    public void onTick() {
        this.checkClosed();
        super.onTick();
    }

    private void checkClosed() {
        if (this.context.checkClosed()) {
            this.close();
        }
    }

    protected void rebuildUi() {
        for (int i = 0; i < this.size; i++) {
            this.clearSlot(i);
        }
        this.buildUi();
    }

    protected GuiElementBuilder closeButton() {
        return new GuiElementBuilder(Items.BARRIER)
                .setName(TextUtils.gui(context.interfaceList.isEmpty() ? "close" : "back"))
                .setRarity(Rarity.COMMON)
                .hideDefaultTooltip()
                .setCallback((x, y, z, c) -> {
                    this.playClickSound();
                    this.openPreviousOrClose();
                });
    }

    protected void openPreviousOrClose() {
        if (this.context == null || this.context.interfaceList.isEmpty()) {
            this.close();
        } else {
            this.switchUi(this.context.interfaceList.removeFirst(), false);
        }
    }

    protected void playClickSound() {
        this.playSound(SoundEvents.UI_BUTTON_CLICK, 0.5f, 1f);
    }

    @Override
    public boolean onClick(int index, ClickType type, SlotActionType action, GuiElementInterface element) {
        if (type == ClickType.DROP || type == ClickType.CTRL_DROP) {
            this.close();
            return true;
        }

        return super.onClick(index, type, action, element);
    }

    protected void addSlot(EditorActions actions, GuiElementBuilder builder) {
        if (actions.canUse(this.player)) {
            this.addSlot(builder);
        }
    }

    protected void setSlot(int slot, EditorActions actions, GuiElementBuilder builder) {
        if (actions.canUse(this.player)) {
            this.addSlot(builder);
        }
    }

    protected abstract void buildUi();

    protected abstract EditingContext.SwitchEntry asSwitchableUi();

    protected GuiElementBuilder baseElement(Item item, String name, boolean selected) {
        var builder = new GuiElementBuilder(item)
                .setName(TextUtils.gui(name).formatted(Formatting.WHITE))
                .hideDefaultTooltip();

        if (selected) {
            builder.glow();
        }

        return builder;
    }

    protected GuiElementBuilder baseElement(Item item, MutableText text, boolean selected) {
        var builder = new GuiElementBuilder(item)
                .setName(text.formatted(Formatting.WHITE))
                .hideDefaultTooltip();

        if (selected) {
            builder.glow();
        }

        return builder;
    }

    protected GuiElementBuilder switchElement(Item item, String name, EditingContext.SwitchableUi ui) {
        return new GuiElementBuilder(item)
                .setName(TextUtils.gui("entry." + name).formatted(Formatting.WHITE))
                .hideDefaultTooltip()
                .setCallback(switchCallback(ui));
    }

    protected GuiElementInterface.ClickCallback switchCallback(EditingContext.SwitchableUi ui) {
        return (x, y, z, c) -> {
            this.playSound(SoundEvents.UI_BUTTON_CLICK, 0.5f, 1f);
            this.switchUi(new EditingContext.SwitchEntry(ui, 0), true);
        };
    }

    public void switchUi(EditingContext.SwitchEntry uiOpener, boolean addSelf) {
        if (uiOpener.currentSlot() == -1) {
            this.close(false);
        }

        var context = this.context;
        if (addSelf) {
            context.interfaceList.addFirst(this.asSwitchableUi());
        }
        this.context = null;
        uiOpener.open(context);
    }

    @Override
    public void onClose() {
        if (this.context != null) {
            this.context.close();
        }
    }

    protected void playSound(RegistryEntry<SoundEvent> sound, float volume, float pitch) {
        this.player.networkHandler.sendPacket(new PlaySoundS2CPacket(sound, SoundCategory.MASTER, this.player.getX(), this.player.getY(), this.player.getZ(), volume, pitch, 0));
    }
}
