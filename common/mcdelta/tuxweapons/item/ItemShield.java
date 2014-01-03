package mcdelta.tuxweapons.item;

import mcdelta.core.DeltaCore;
import mcdelta.core.assets.Assets;
import mcdelta.core.client.item.IExtraPasses;
import mcdelta.core.item.ItemDelta;
import mcdelta.core.material.MaterialRegistry;
import mcdelta.core.material.ItemMaterial;
import mcdelta.core.proxy.ClientProxy;
import mcdelta.tuxweapons.TuxWeapons;
import mcdelta.tuxweapons.client.item.RenderShield;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemShield extends ItemDelta implements IExtraPasses
{
    private final String toolName;
    public ItemMaterial toolMaterial;

    @SideOnly(Side.CLIENT)
    protected Icon itemOverlay;

    @SideOnly(Side.CLIENT)
    protected Icon overrideIcon;

    private boolean overrideExists = false;

    public ItemShield(ItemMaterial mat)
    {
        super(TuxWeapons.instance, mat.getName() + "." + "shield", false);

        toolName = "shield";
        toolMaterial = mat;
        maxStackSize = 1;
        setCreativeTab(CreativeTabs.tabTools);

        setMaxDamage((int) (mat.getMaxUses() * 0.5F));

        String weapon = "tool." + toolName;
        String material = "material." + mat.getName();

        if (!StatCollector.func_94522_b(weapon))
        {
            DeltaCore.localizationWarnings.append("- " + weapon + " \n");
        }

        if (!StatCollector.func_94522_b(material))
        {
            DeltaCore.localizationWarnings.append("- " + material + " \n");
        }

        ClientProxy.extraPasses.remove(this);

        if (Assets.isClient())
        {
            MinecraftForgeClient.registerItemRenderer(itemID, new RenderShield());
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        player.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.block;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        itemIcon = ItemDelta.doRegister(mod.id().toLowerCase(), toolName + "_1", register);
        itemOverlay = ItemDelta.doRegister(mod.id().toLowerCase(), toolName + "_2", register);

        overrideExists = Assets.resourceExists(new ResourceLocation(mod.id().toLowerCase(), "textures/items/override/" + toolMaterial.getName().toLowerCase() + "_" + toolName
                + ".png"));

        if (overrideExists)
        {
            overrideIcon = ItemDelta.doRegister(mod.id().toLowerCase(), "override/" + toolMaterial.getName().toLowerCase() + "_" + toolName, register);
        }
    }

    @Override
    public int getPasses(ItemStack stack)
    {
        if (overrideExists)
        {
            return 1;
        }

        return 2;
    }

    @Override
    public Icon getIconFromPass(ItemStack stack, int pass)
    {
        if (overrideExists)
        {
            return overrideIcon;
        }

        if (pass == 2)
        {
            return itemOverlay;
        }

        return itemIcon;
    }

    @Override
    public int getColorFromPass(ItemStack stack, int pass)
    {
        if (overrideExists)
        {
            return 0xffffff;
        }

        if (pass == 2)
        {
            return MaterialRegistry.WOOD.getColor();
        }

        return toolMaterial.getColor();
    }

    @Override
    public boolean getShinyFromPass(ItemStack stack, int pass)
    {
        if ((pass == 1) && toolMaterial.isShinyDefault())
        {
            return true;
        }

        return false;
    }

    @Override
    public String getItemDisplayName(ItemStack stack)
    {
        ItemMaterial mat = toolMaterial;

        String weapon = StatCollector.translateToLocal("tool." + toolName);
        String material = StatCollector.translateToLocal("material." + mat.getName());

        return material + " " + weapon;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack repair, ItemStack gem)
    {
        if ((OreDictionary.getOres(toolMaterial.getOreDictionaryName()) != null) && !OreDictionary.getOres(toolMaterial.getOreDictionaryName()).isEmpty())
        {
            return OreDictionary.itemMatches(OreDictionary.getOres(toolMaterial.getOreDictionaryName()).get(0), gem, false) ? true : super.getIsRepairable(repair, gem);
        }

        return super.getIsRepairable(repair, gem);
    }
}
