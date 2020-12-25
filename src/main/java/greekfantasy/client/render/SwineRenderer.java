package greekfantasy.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class SwineRenderer<T extends LivingEntity> extends LivingRenderer<T, PigModel<T>> {
  
  private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");
  
  public SwineRenderer(EntityRendererManager renderManagerIn) {
     super(renderManagerIn, new PigModel<>(), 0.7F);
  }
  
  // This method is a copy of the super methods but without firing additional render events (to avoid recursion)
  @Override
  public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    matrixStackIn.push();
    this.entityModel.swingProgress = this.getSwingProgress(entityIn, partialTicks);

    boolean shouldSit = entityIn.isPassenger() && (entityIn.getRidingEntity() != null && entityIn.getRidingEntity().shouldRiderSit());
    this.entityModel.isSitting = shouldSit;
    this.entityModel.isChild = entityIn.isChild();
    float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset);
    float f1 = MathHelper.interpolateAngle(partialTicks, entityIn.prevRotationYawHead, entityIn.rotationYawHead);
    float f2 = f1 - f;
    if (shouldSit && entityIn.getRidingEntity() instanceof LivingEntity) {
       LivingEntity livingentity = (LivingEntity)entityIn.getRidingEntity();
       f = MathHelper.interpolateAngle(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
       f2 = f1 - f;
       float f3 = MathHelper.wrapDegrees(f2);
       if (f3 < -85.0F) {
          f3 = -85.0F;
       }

       if (f3 >= 85.0F) {
          f3 = 85.0F;
       }

       f = f1 - f3;
       if (f3 * f3 > 2500.0F) {
          f += f3 * 0.2F;
       }

       f2 = f1 - f;
    }

    float f6 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
    if (entityIn.getPose() == Pose.SLEEPING) {
       Direction direction = entityIn.getBedDirection();
       if (direction != null) {
          float f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1F;
          matrixStackIn.translate((double)((float)(-direction.getXOffset()) * f4), 0.0D, (double)((float)(-direction.getZOffset()) * f4));
       }
    }

    float f7 = this.handleRotationFloat(entityIn, partialTicks);
    this.applyRotations(entityIn, matrixStackIn, f7, f, partialTicks);
    matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
    this.preRenderCallback(entityIn, matrixStackIn, partialTicks);
    matrixStackIn.translate(0.0D, (double)-1.501F, 0.0D);
    float f8 = 0.0F;
    float f5 = 0.0F;
    if (!shouldSit && entityIn.isAlive()) {
       f8 = MathHelper.lerp(partialTicks, entityIn.prevLimbSwingAmount, entityIn.limbSwingAmount);
       f5 = entityIn.limbSwing - entityIn.limbSwingAmount * (1.0F - partialTicks);
       if (entityIn.isChild()) {
          f5 *= 3.0F;
       }

       if (f8 > 1.0F) {
          f8 = 1.0F;
       }
    }

    this.entityModel.setLivingAnimations(entityIn, f5, f8, partialTicks);
    this.entityModel.setRotationAngles(entityIn, f5, f8, f7, f2, f6);
    Minecraft minecraft = Minecraft.getInstance();
    boolean flag = this.isVisible(entityIn);
    boolean flag1 = !flag && !entityIn.isInvisibleToPlayer(minecraft.player);
    boolean flag2 = minecraft.isEntityGlowing(entityIn);
    RenderType rendertype = this.func_230496_a_(entityIn, flag, flag1, flag2);
    if (rendertype != null) {
       IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
       int i = getPackedOverlay(entityIn, this.getOverlayProgress(entityIn, partialTicks));
       this.entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
    }

//    if (!entityIn.isSpectator()) {
//       for(LayerRenderer<T, M> layerrenderer : this.layerRenderers) {
//          layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
//       }
//    }

    matrixStackIn.pop();
    
    // render nameplate
//    net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(entityIn, entityIn.getDisplayName(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
//    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
//    if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.canRenderName(entityIn))) {
//       this.renderName(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
//    }
 }

  @Override
  public ResourceLocation getEntityTexture(T entity) {
     return PIG_TEXTURES;
  }
}
