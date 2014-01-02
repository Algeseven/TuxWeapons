package mcdelta.tuxweapons;

import static mcdelta.tuxweapons.damage.EnumDamageTypes.BASHER;
import static mcdelta.tuxweapons.damage.EnumDamageTypes.GOLDEN;
import static mcdelta.tuxweapons.damage.EnumDamageTypes.SLASHER;
import mcdelta.core.DeltaCore;
import mcdelta.core.ModDelta;
import mcdelta.core.assets.Assets;
import mcdelta.core.client.particle.EnumParticles;
import mcdelta.core.network.PacketHandler;
import mcdelta.tuxweapons.block.BlockTW;
import mcdelta.tuxweapons.config.TWSettings;
import mcdelta.tuxweapons.damage.DamageModifier;
import mcdelta.tuxweapons.entity.EntityBolt;
import mcdelta.tuxweapons.entity.EntityDynamite;
import mcdelta.tuxweapons.entity.EntityEMPGrenade;
import mcdelta.tuxweapons.entity.EntityGrappHook;
import mcdelta.tuxweapons.entity.EntityKnife;
import mcdelta.tuxweapons.entity.EntitySpear;
import mcdelta.tuxweapons.entity.EntityTWFireball;
import mcdelta.tuxweapons.event.EventEnchants;
import mcdelta.tuxweapons.event.EventFOVModifier;
import mcdelta.tuxweapons.event.EventItemInfo;
import mcdelta.tuxweapons.handlers.PlayerTracker;
import mcdelta.tuxweapons.handlers.TickHandler;
import mcdelta.tuxweapons.item.ItemTW;
import mcdelta.tuxweapons.network.PacketSpawnParticle;
import mcdelta.tuxweapons.network.PacketThrowablePickup;
import mcdelta.tuxweapons.proxy.TWCommonProxy;
import mcdelta.tuxweapons.specials.enchant.EnchantmentTW;
import mcdelta.tuxweapons.specials.potions.Potions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod (modid = TuxWeaponsCore.MOD_ID, name = "TuxWeapons", useMetadata = true, version = "1.1a")
@NetworkMod (clientSideRequired = true, serverSideRequired = false, channels =
{ TuxWeaponsCore.MOD_ID }, packetHandler = PacketHandler.class)
public class TuxWeaponsCore implements ModDelta
{
     // TODO
     // - beheading enchant
     // - giant magma core drops
     // - death messages
     // - reimplement ukulele
     // - retexture magma core
     
     public static final String   MOD_ID         = "TuxWeapons";
     
     @Instance (TuxWeaponsCore.MOD_ID)
     public static TuxWeaponsCore instance;
     
     @SidedProxy (clientSide = "mcdelta.tuxweapons.proxy.TWClientProxy", serverSide = "mcdelta.tuxweapons.proxy.TWCommonProxy")
     public static TWCommonProxy  proxy;
     
     private String               cfg_tuxweapons = "tuxweapons";
     
     
     
     
     @EventHandler
     public void preInit (FMLPreInitializationEvent event)
     {
          PacketHandler.packets[2] = PacketSpawnParticle.class;
          PacketHandler.packets[3] = PacketThrowablePickup.class;
     }
     
     
     
     
     @EventHandler
     public void load (FMLInitializationEvent event)
     {
          ItemTW.hammers.isEmpty();
          BlockTW.redstoneTmpBlock.getBlockBoundsMinZ();
          EnchantmentTW.drawback.getClass();
          
          DeltaCore.config.addConfig(cfg_tuxweapons);
          TWSettings.initTWConfig(DeltaCore.config.getConfig(cfg_tuxweapons));
          
          MinecraftForge.EVENT_BUS.register(new EventEnchants());
          MinecraftForge.EVENT_BUS.register(new DamageModifier());
          MinecraftForge.EVENT_BUS.register(new EventItemInfo());
          MinecraftForge.EVENT_BUS.register(new EventFOVModifier());
          
          TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
          GameRegistry.registerPlayerTracker(new PlayerTracker());
          
          Recipes.addCraftingRecipes();
          
          Potions.init();
          
          proxy.registerRenderers();
     }
     
     
     
     
     @EventHandler
     public void postInit (FMLPostInitializationEvent event)
     {
          registerEntities();
          
          for (Item item : Item.itemsList)
          {
               if (item instanceof ItemSword && !BASHER.effc_item.contains(item) && !SLASHER.effc_item.contains(item))
               {
                    SLASHER.effc_item.add(item);
               }
               
               else if (item instanceof ItemAxe && !BASHER.effc_item.contains(item) && !SLASHER.effc_item.contains(item))
               {
                    BASHER.effc_item.add(item);
               }
               
               if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().toLowerCase().contains("gold"))
               {
                    GOLDEN.effc_item.add(item);
               }
               
               if (item instanceof ItemAxe && ((ItemAxe) item).getToolMaterialName().toLowerCase().contains("gold"))
               {
                    GOLDEN.effc_item.add(item);
               }
          }
     }
     
     
     
     
     private void registerEntities ()
     {
          EntityRegistry.registerModEntity(EntitySpear.class, MOD_ID.toLowerCase() + ":" + "spear", 1, this, 64, 3, true);
          EntityRegistry.registerModEntity(EntityKnife.class, MOD_ID.toLowerCase() + ":" + "knife", 2, this, 64, 3, true);
          EntityRegistry.registerModEntity(EntityGrappHook.class, MOD_ID.toLowerCase() + ":" + "grappHook", 3, this, 64, 3, true);
          EntityRegistry.registerModEntity(EntityTWFireball.class, MOD_ID.toLowerCase() + ":" + "fireball", 4, this, 64, 3, true);
          EntityRegistry.registerModEntity(EntityBolt.class, MOD_ID.toLowerCase() + ":" + "bolt", 5, this, 64, 3, true);
          EntityRegistry.registerModEntity(EntityDynamite.class, MOD_ID.toLowerCase() + ":" + "dynamite", 6, this, 64, 3, true);
          EntityRegistry.registerModEntity(EntityEMPGrenade.class, MOD_ID.toLowerCase() + ":" + "empGrenade", 7, this, 64, 3, true);
     }
     
     
     
     
     @Override
     public void doThings (Stage stage)
     {
     }
     
     
     
     
     public static void spawnParticle (int particle, World world, double x, double y, double z, Object... obj)
     {
          if (Assets.isClient())
          {
               EnumParticles.values()[particle].spawnParticle(world, x, y, z, obj);
          }
          
          else
          {
               PacketDispatcher.sendPacketToAllAround(x, y, z, 10, world.provider.dimensionId, Assets.populatePacket(new PacketSpawnParticle(particle, x, y, z)));
          }
     }
}