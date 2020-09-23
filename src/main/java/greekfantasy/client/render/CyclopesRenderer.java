package greekfantasy.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import greekfantasy.GreekFantasy;
import greekfantasy.client.model.CyclopesModel;
import greekfantasy.entity.CyclopesEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class CyclopesRenderer<T extends CyclopesEntity> extends BipedRenderer<T, CyclopesModel<T>> {
  
  private static final ResourceLocation TEXTURE = new ResourceLocation(GreekFantasy.MODID, "textures/entity/cyclopes.png");
  private static final float SCALE = 1.15F;

  public CyclopesRenderer(final EntityRendererManager renderManagerIn) {
    super(renderManagerIn, new CyclopesModel<T>(0.0F), 0.0F);
  }

  @Override
  public ResourceLocation getEntityTexture(final T entity) {
    return TEXTURE;
  }
  
  @Override
  public void render(final T entityIn, final float rotationYawIn, final float partialTick, 
      final MatrixStack matrixStackIn, final IRenderTypeBuffer bufferIn, final int packedLightIn) {
    matrixStackIn.push();
    matrixStackIn.scale(SCALE, SCALE, SCALE);
    super.render(entityIn, rotationYawIn, partialTick, matrixStackIn, bufferIn, packedLightIn);
    matrixStackIn.pop();
  }
}
