package mcdelta.tuxweapons.item;

import static mcdelta.core.assets.Assets.*;
import mcdelta.core.EnumMCDMods;
import mcdelta.core.assets.Assets;
import mcdelta.core.item.ItemWeapon;
import mcdelta.core.material.ToolMaterial;
import mcdelta.tuxweapons.entity.EntitySpear;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpear extends ItemWeapon
{
     
     public ItemSpear (ToolMaterial mat)
     {
          super("spear", EnumMCDMods.TUXWEAPONS, mat, 2.0F);
          this.setMaxDamage((int) ((float) mat.getMaxUses() * 0.9F));
     }
     
     
     
     
     @Override
     public EnumAction getItemUseAction (ItemStack stack)
     {
          return EnumAction.bow;
     }
     
     
     
     
     @Override
     public void onPlayerStoppedUsing (ItemStack stack, World world, EntityPlayer player, int par4)
     {
          stack.damageItem(2, player);
          
          int duration = this.getMaxItemUseDuration(stack) - par4;
          
          float charge = duration / 30.0F;
          
          if (charge < 0.1D)
          {
               return;
          }
          
          if (charge > 0.8F)
          {
               charge = 0.8F;
          }
          
          EntitySpear spear = new EntitySpear(world, player, charge, stack.copy());
          
          if (charge == 1.0F)
          {
               spear.setIsCritical(true);
          }
          
          world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.1F - 1.2F) + charge * 0.5F);
          
          if (!player.capabilities.isCreativeMode)
          {
               Assets.clearCurrentItem(player);
          }
          
          if (Assets.isServer())
          {
               world.spawnEntityInWorld(spear);
          }
     }
     
     
     
     
     @Override
     public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
     {
          if (player.capabilities.isCreativeMode || player.inventory.hasItem(this.itemID))
          {
               return super.onItemRightClick(stack, world, player);
          }
          
          return stack;
     }
}