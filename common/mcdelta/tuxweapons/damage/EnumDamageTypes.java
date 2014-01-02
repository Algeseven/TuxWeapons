package mcdelta.tuxweapons.damage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcdelta.core.item.ItemDelta;
import mcdelta.core.material.ToolMaterial;
import mcdelta.tuxweapons.item.ItemTW;
import net.minecraft.item.Item;

public enum EnumDamageTypes
{
     SLASHER (new String[]
     { "spider", "cavespider", "zombie", "pigzombie", "slime", "witch" }),
     BASHER (new String[]
     { "ghast", "blaze", "creeper", "skeleton", "silverfish", "lavaslime" }),
     GOLDEN (new String[]
     { "enderman" });
     
     public List<Item>   effc_item   = new ArrayList();
     public List<String> effc_entity = new ArrayList();
     
     
     static
     {
          for (ToolMaterial mat : ToolMaterial.mats)
          {
               SLASHER.effc_item.add(ItemDelta.swords.get(mat));
               SLASHER.effc_item.add(ItemTW.battleaxes.get(mat));
               SLASHER.effc_item.add(ItemTW.spears.get(mat));
               
               BASHER.effc_item.add(ItemDelta.axes.get(mat));
               BASHER.effc_item.add(ItemTW.hammers.get(mat));
               BASHER.effc_item.add(ItemTW.maces.get(mat));
          }
     }
     
     
     
     
     EnumDamageTypes ()
     {
          
     }
     
     
     
     
     EnumDamageTypes (String[] arr)
     {
          this();
          effc_entity.addAll(Arrays.asList(arr));
     }
}
