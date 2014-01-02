package mcdelta.tuxweapons.client.entity;

import mcdelta.core.assets.client.RenderAssets;
import mcdelta.core.client.item.IExtraPasses;
import mcdelta.tuxweapons.entity.EntityGrappHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly (Side.CLIENT)
public class RenderGrappHook extends Render
{
     private static final ResourceLocation glint = new ResourceLocation("textures/misc/enchanted_item_glint.png");
     
     
     
     
     public void renderGrappHook (EntityGrappHook grappHook, double x, double y, double z, float par8, float par9)
     {
          ItemStack stack = grappHook.stack;
          
          if (stack != null)
          {
               GL11.glPushMatrix();
               
               int passes = ((IExtraPasses) stack.getItem()).getPasses(stack);
               Icon[] icons = new Icon[passes];
               int[] colors = new int[passes];
               boolean[] shiny = new boolean[passes];
               
               for (int i = 0; i < passes; i++)
               {
                    icons[i] = ((IExtraPasses) stack.getItem()).getIconFromPass(stack, i + 1);
               }
               
               for (int i = 0; i < passes; i++)
               {
                    colors[i] = ((IExtraPasses) stack.getItem()).getColorFromPass(stack, i + 1);
               }
               
               for (int i = 0; i < passes; i++)
               {
                    shiny[i] = ((IExtraPasses) stack.getItem()).getShinyFromPass(stack, i + 1);
               }
               
               TextureManager engine = Minecraft.getMinecraft().getTextureManager();
               
               GL11.glTranslatef((float) x, (float) y + 0.04F, (float) z);
               
               GL11.glRotatef(grappHook.prevRotationYaw - 90.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(grappHook.prevRotationPitch - 135, 0.0F, 0.0F, 1.0F);
               
               float f11 = (float) grappHook.arrowShake - par9;
               
               if (f11 > 0.0F)
               {
                    float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
                    GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
               }
               
               float scale = 1.5F;
               GL11.glScalef(scale, scale, scale);
               
               RenderAssets.renderItemInWorld(stack, engine, passes, icons, colors, shiny);
               
               GL11.glPopMatrix();
               
               GL11.glPushMatrix();
               
               Entity hook = grappHook.owner;
               Entity owner = grappHook;
               
               if (hook != null)
               {
                    y -= (3.5 - (double) owner.height) * 0.5D;
                    Tessellator tessellator = Tessellator.instance;
                    double d3 = this.leashFunction((double) hook.prevRotationYaw, (double) hook.rotationYaw, (double) (par9 * 0.5F)) * 0.01745329238474369D;
                    double d4 = this.leashFunction((double) hook.prevRotationPitch, (double) hook.rotationPitch, (double) (par9 * 0.5F)) * 0.01745329238474369D;
                    double d5 = Math.cos(d3);
                    double d6 = Math.sin(d3);
                    double d7 = Math.sin(d4);
                    
                    double d8 = Math.cos(d4);
                    double d9 = this.leashFunction(hook.prevPosX, hook.posX, (double) par9) - d5 * 0.7D - d6 * 0.5D * d8;
                    double d10 = this.leashFunction(hook.prevPosY + (double) (hook.getEyeHeight() + 1.7D) * 0.7D, hook.posY + (double) (hook.getEyeHeight() + 1.7D) * 0.7D, (double) par9) - d7 * 0.5D - 0.25D;
                    double d11 = this.leashFunction(hook.prevPosZ, hook.posZ, (double) par9) - d6 * 0.7D + d5 * 0.5D * d8;
                    double d12 = this.leashFunction((double) owner.prevRotationYaw, (double) owner.rotationYaw, (double) par9) * 0.01745329238474369D + (Math.PI / 2D);
                    d5 = Math.cos(d12) * (double) owner.width * 0.4D;
                    d6 = Math.sin(d12) * (double) owner.width * 0.4D;
                    double d13 = this.leashFunction(owner.prevPosX, owner.posX, (double) par9) + d5;
                    double d14 = this.leashFunction(owner.prevPosY, owner.posY, (double) par9);
                    double d15 = this.leashFunction(owner.prevPosZ, owner.posZ, (double) par9) + d6;
                    x += d5;
                    z += d6;
                    double d16 = (double) ((float) (d9 - d13));
                    double d17 = (double) ((float) (d10 - d14));
                    double d18 = (double) ((float) (d11 - d15));
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    boolean flag = true;
                    double d19 = 0.025D;
                    tessellator.startDrawing(5);
                    int i;
                    float f2;
                    
                    for (i = 0; i <= 24; ++i)
                    {
                         if (i % 2 == 0)
                         {
                              tessellator.setColorRGBA_F(0.52F, 0.52F, 0.56F, 1.0F);
                         }
                         else
                         {
                              tessellator.setColorRGBA_F(0.36F, 0.36F, 0.37F, 1.0F);
                         }
                         
                         f2 = (float) i / 24.0F;
                         tessellator.addVertex(x + d16 * (double) f2 + 0.0D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F), z + d18 * (double) f2);
                         tessellator.addVertex(x + d16 * (double) f2 + 0.025D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F) + 0.025D, z + d18 * (double) f2);
                    }
                    
                    tessellator.draw();
                    tessellator.startDrawing(5);
                    
                    for (i = 0; i <= 24; ++i)
                    {
                         if (i % 2 == 0)
                         {
                              tessellator.setColorRGBA_F(0.52F, 0.52F, 0.56F, 1.0F);
                         }
                         else
                         {
                              tessellator.setColorRGBA_F(0.36F, 0.36F, 0.37F, 1.0F);
                         }
                         
                         f2 = (float) i / 24.0F;
                         tessellator.addVertex(x + d16 * (double) f2 + 0.0D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F) + 0.025D, z + d18 * (double) f2);
                         tessellator.addVertex(x + d16 * (double) f2 + 0.025D, y + d17 * (double) (f2 * f2 + f2) * 0.5D + (double) ((24.0F - (float) i) / 18.0F + 0.125F), z + d18 * (double) f2 + 0.025D);
                    }
                    
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_CULL_FACE);
               }
               
               GL11.glPopMatrix();
          }
     }
     
     
     
     
     private double leashFunction (double x, double y, double z)
     {
          return x + (y - x) * z;
     }
     
     
     
     
     @Override
     public void doRender (Entity entity, double par2, double par4, double par6, float par8, float par9)
     {
          this.renderGrappHook((EntityGrappHook) entity, par2, par4, par6, par8, par9);
     }
     
     
     
     
     @Override
     protected ResourceLocation getEntityTexture (Entity entity)
     {
          EntityGrappHook grappHook = (EntityGrappHook) entity;
          return this.renderManager.renderEngine.getResourceLocation(grappHook.stack.getItemSpriteNumber());
     }
}
