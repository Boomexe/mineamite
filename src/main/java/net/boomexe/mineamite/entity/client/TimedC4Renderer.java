package net.boomexe.mineamite.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.boomexe.mineamite.MineamiteMod;
import net.boomexe.mineamite.entity.custom.TimedC4Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.ForgeMod;
import org.joml.Matrix4f;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TimedC4Renderer extends GeoEntityRenderer<TimedC4Entity> {
    public TimedC4Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TimedC4Model());
    }

    @Override
    public ResourceLocation getTextureLocation(TimedC4Entity animatable) {
        return new ResourceLocation(MineamiteMod.MOD_ID, "textures/entity/c4.png");
    }

    @Override
    public void render(TimedC4Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.scale(0.6f, 0.6f, 0.6f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void renderNameTag(TimedC4Entity pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        double squareDistance = this.entityRenderDispatcher.distanceToSqr(pEntity);
        if (isNameplateInRenderDistance(squareDistance)) {
            float nameTagOffsetY = pEntity.getNameTagOffsetY() - 0.1f;
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, nameTagOffsetY, 0.0F);
            pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            pPoseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pPoseStack.last().pose();
            float backgroundOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            Font font = this.getFont();
            float xPos = (float)(-font.width(pDisplayName) / 2);
            font.drawInBatch(pDisplayName, xPos, 0, -1, false, matrix4f, pBuffer, Font.DisplayMode.NORMAL, 0, pPackedLight);

            pPoseStack.popPose();
        }
    }

    public boolean isNameplateInRenderDistance(double squareDistance) {
        return squareDistance <= 5.0f;
        // CAN MAKE THIS CONFIG
    }
}
