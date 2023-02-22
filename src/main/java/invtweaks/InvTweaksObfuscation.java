package invtweaks;

import invtweaks.api.container.ContainerSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Minecraft 1.3 Obfuscation layer
 *
 * @author Jimeo Wan
 */
public class InvTweaksObfuscation {

    private static final Logger log = InvTweaks.log;
    public Minecraft mc;

    public InvTweaksObfuscation(Minecraft mc_) {
        mc = mc_;
    }

    // Minecraft members

    @Nullable
    public static String getNamespacedID(@Nullable String id) {
        if(id == null) {
            return null;
        } else if(id.indexOf(':') == -1) {
            return "minecraft:" + id;
        }
        return id;
    }

    public static int getDisplayWidth() {
        return FMLClientHandler.instance().getClient().displayWidth;
    }

    public static int getDisplayHeight() {
        return FMLClientHandler.instance().getClient().displayHeight;
    }

    public static boolean areItemStacksEqual(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2) {
        return itemStack1.isItemEqual(itemStack2) && itemStack1.getCount() == itemStack2.getCount();
    }

    @NotNull
    public static ItemStack getSlotStack(@NotNull Container container, int i) {
        // Slot
        Slot slot = container.inventorySlots.get(i);
        return (slot == null) ? ItemStack.EMPTY : slot.getStack(); // getStack
    }

    public static int getSlotNumber(Slot slot) {
        try {
            // Creative slots don't set the "slotNumber" property, serve as a proxy for true slots
            if(slot instanceof GuiContainerCreative.CreativeSlot) {
                Slot underlyingSlot = ((GuiContainerCreative.CreativeSlot) slot).slot;
                if(underlyingSlot != null) {
                    return underlyingSlot.slotNumber;
                } else {
                    log.warn("Creative inventory: Failed to get real slot");
                }
            }
        } catch(Exception e) {
            log.warn("Failed to access creative slot number");
        }
        return slot.slotNumber;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public static Slot getSlotAtMousePosition(@Nullable GuiContainer guiContainer) {
        // Copied from GuiContainer
        if(guiContainer != null) {
            Container container = guiContainer.inventorySlots;

            int x = getMouseX(guiContainer);
            int y = getMouseY(guiContainer);
            for(int k = 0; k < container.inventorySlots.size(); k++) {
                Slot slot = container.inventorySlots.get(k);
                if(getIsMouseOverSlot(guiContainer, slot, x, y)) {
                    return slot;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static boolean getIsMouseOverSlot(@Nullable GuiContainer guiContainer, @NotNull Slot slot, int x, int y) {
        // Copied from GuiContainer
        if(guiContainer != null) {
            x -= guiContainer.guiLeft;
            y -= guiContainer.guiTop;
            return x >= slot.xPos - 1 && x < slot.xPos + 16 + 1 && y >= slot.yPos - 1 && y < slot.yPos + 16 + 1;
        } else {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    private static int getMouseX(@NotNull GuiContainer guiContainer) {
        return (Mouse.getEventX() * guiContainer.width) / getDisplayWidth();
    }

    @SideOnly(Side.CLIENT)
    private static int getMouseY(@NotNull GuiContainer guiContainer) {
        return guiContainer.height - (Mouse.getEventY() * guiContainer.height) / getDisplayHeight() - 1;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static int getSpecialChestRowSize(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$rowSize()"
        return 0;
    }

    // EntityPlayer members

    // Static access
    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean isValidChest(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$validChest()"
        return false;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean isLargeChest(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$largeChest()"
        return false;
    }

    // InventoryPlayer members

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean isValidInventory(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$validInventory()"
        return false;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static boolean showButtons(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$showButtons()"
        return false;
    }

    @Contract("!null->_")
    @SuppressWarnings({"unused", "SameReturnValue"})
    public static Map<ContainerSection, List<Slot>> getContainerSlotMap(Container container) {
        // This method gets replaced by the transformer with "return container.invtweaks$slotMap()"
        return null;
    }

    public static boolean isGuiContainer(@Nullable Object o) { // GuiContainer (abstract class)
        return o != null && o instanceof GuiContainer;
    }

    public static boolean isGuiInventoryCreative(@Nullable Object o) { // GuiInventoryCreative
        return o != null && o.getClass().equals(GuiContainerCreative.class);
    }

    public static boolean isGuiInventory(@Nullable Object o) { // GuiInventory
        return o != null && o.getClass().equals(GuiInventory.class);
    }

    public static boolean isGuiButton(@Nullable Object o) { // GuiButton
        return o != null && o instanceof GuiButton;
    }

    // FontRenderer members

    public static boolean isGuiEditSign(@Nullable Object o) {
        return o != null && o.getClass().equals(GuiEditSign.class);
    }

    public static boolean isItemArmor(@Nullable Object o) { // ItemArmor
        return o != null && o instanceof ItemArmor;
    }

    public static boolean isBasicSlot(@Nullable Object o) { // Slot
        return o != null && (o.getClass().equals(Slot.class) || o.getClass().equals(GuiContainerCreative.CreativeSlot.class));
    }

    // Container members

    public static Container getCurrentContainer() {
        Minecraft mc = FMLClientHandler.instance().getClient();
        Container currentContainer = mc.player.inventoryContainer;
        if(InvTweaksObfuscation.isGuiContainer(mc.currentScreen)) {
            currentContainer = ((GuiContainer) mc.currentScreen).inventorySlots;
        }

        return currentContainer;
    }

    // Slot members

    public static boolean areSameItemType(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2) {
        return !itemStack1.isEmpty() && !itemStack2.isEmpty() && (itemStack1.isItemEqual(itemStack2) || (itemStack1.isItemStackDamageable() && itemStack1.getItem() == itemStack2.getItem()));
    }

    public static boolean areItemsStackable(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2) {
        return !itemStack1.isEmpty() && !itemStack2.isEmpty() && itemStack1.isItemEqual(itemStack2) && itemStack1.isStackable() && (!itemStack1.getHasSubtypes() || itemStack1.getItemDamage() == itemStack2.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack1, itemStack2);
    }

    public void addChatMessage(@NotNull String message) {
        if(mc.ingameGUI != null) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
        }
    }

    public EntityPlayer getThePlayer() {
        return mc.player;
    }

    public PlayerControllerMP getPlayerController() {
        return mc.playerController;
    }

    @Nullable
    public GuiScreen getCurrentScreen() {
        return mc.currentScreen;
    }

    public FontRenderer getFontRenderer() {
        return mc.fontRenderer;
    }

    public void displayGuiScreen(GuiScreen parentScreen) {
        mc.displayGuiScreen(parentScreen);
    }

    public GameSettings getGameSettings() {
        return mc.gameSettings;
    }

    public int getKeyBindingForwardKeyCode() {
        return getGameSettings().keyBindForward.keyCode;
    }

    // Classes

    public int getKeyBindingBackKeyCode() {
        return getGameSettings().keyBindBack.keyCode;
    }

    public InventoryPlayer getInventoryPlayer() { // InventoryPlayer
        return getThePlayer().inventory;
    }

    public NonNullList<ItemStack> getMainInventory() {
        return getInventoryPlayer().mainInventory;
    }

    @NotNull
    public ItemStack getHeldStack() {
        return getInventoryPlayer().getItemStack(); // getItemStack
    }

    @NotNull
    public ItemStack getFocusedStack() {
        return getInventoryPlayer().getCurrentItem(); // getCurrentItem
    }

    public int getFocusedSlot() {
        return getInventoryPlayer().currentItem; // currentItem
    }

    public boolean hasTexture(@NotNull ResourceLocation texture) {
        try {
            mc.getResourceManager().getResource(texture);
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    @NotNull
    public ItemStack getOffhandStack() {
        return getInventoryPlayer().offHandInventory.get(0);
    }
}
