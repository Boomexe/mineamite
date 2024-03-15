package net.boomexe.mineamite.entity.client;

import net.boomexe.mineamite.MineamiteMod;
import net.boomexe.mineamite.entity.custom.TimedC4Entity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TimedC4Model extends GeoModel<TimedC4Entity> {

	@Override
	public ResourceLocation getModelResource(TimedC4Entity animatable) {
		return new ResourceLocation(MineamiteMod.MOD_ID, "geo/c4.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TimedC4Entity animatable) {
		return new ResourceLocation(MineamiteMod.MOD_ID, "textures/entity/c4.png");
	}

	@Override
	public ResourceLocation getAnimationResource(TimedC4Entity animatable) {
		return new ResourceLocation(MineamiteMod.MOD_ID, "animations/c4.animation.json");
	}
}