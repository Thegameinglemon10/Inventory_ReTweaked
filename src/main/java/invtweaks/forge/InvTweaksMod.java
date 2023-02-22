package invtweaks.forge;

import invtweaks.api.IItemTreeListener;
import invtweaks.api.InvTweaksAPI;
import invtweaks.api.SortingMethod;
import invtweaks.api.container.ContainerSection;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.jetbrains.annotations.NotNull;

/**
 * ModLoader entry point to load and configure the mod.
 *
 * @author Jimeo Wan
 * <p>
 * Contact: jimeo.wan (at) gmail (dot) com Website: <a href="https://inventory-tweaks.readthedocs.org/">https://inventory-tweaks.readthedocs.org/</a>
 * Source code: <a href="https://github.com/kobata/inventory-tweaks">GitHub</a> License: MIT
 */
@Mod(modid = "inventorytweaks", dependencies = "required-after:forge@[14.21.0,)", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "", guiFactory = "invtweaks.forge.ModGuiFactory", certificateFingerprint = "55d2cd4f5f0961410bf7b91ef6c6bf00a766dcbe")
public class InvTweaksMod implements InvTweaksAPI {
    @Mod.Instance
    public static InvTweaksMod instance;

    @SidedProxy(clientSide = "invtweaks.forge.ClientProxy", serverSide = "invtweaks.forge.CommonProxy")
    public static CommonProxy proxy;

    // Helper for ASM transform of GuiTextField to disable sorting on focus.
    @SuppressWarnings("unused")
    public static void setTextboxModeStatic(boolean enabled) {
        instance.setTextboxMode(enabled);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverAboutToStart(@NotNull FMLServerAboutToStartEvent e) {
        proxy.serverAboutToStart(e);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverStopped(FMLServerStoppedEvent e) {
        proxy.serverStopped(e);
    }

    @Override
    public void addOnLoadListener(IItemTreeListener listener) {
        proxy.addOnLoadListener(listener);
    }

    @Override
    public boolean removeOnLoadListener(IItemTreeListener listener) {
        return proxy.removeOnLoadListener(listener);
    }

    @Override
    public void setSortKeyEnabled(boolean enabled) {
        proxy.setSortKeyEnabled(enabled);
    }

    @Override
    public void setTextboxMode(boolean enabled) {
        proxy.setTextboxMode(enabled);
    }

    @Override
    public int compareItems(@NotNull ItemStack i, @NotNull ItemStack j) {
        return proxy.compareItems(i, j);
    }

    @Override
    public int compareItems(@NotNull ItemStack i, @NotNull ItemStack j, boolean onlyTreeSort) {
        return proxy.compareItems(i, j, onlyTreeSort);
    }

    @Override
    public void sort(ContainerSection section, SortingMethod method) {
        proxy.sort(section, method);
    }
}
