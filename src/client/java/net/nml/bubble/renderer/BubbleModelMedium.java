package net.nml.bubble.renderer;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.nml.bubble.BlowingBubbles;

public class BubbleModelMedium extends BubbleModelAbstract {
	public static final EntityModelLayer LAYER = new EntityModelLayer(
			Identifier.of(BlowingBubbles.MOD_ID, "bubble_md"), "main");

	public BubbleModelMedium(ModelPart root) {
		super(root);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("bb_main",
				ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F))
						.uv(10, 31).cuboid(-3.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(32, 0).cuboid(2.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(20, 0).cuboid(3.0F, -2.0F, -2.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(32, 5).cuboid(5.0F, -3.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(20, 5).cuboid(-5.0F, -2.0F, -2.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(32, 21).cuboid(-6.0F, -3.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(28, 15).cuboid(-7.0F, -5.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
						.uv(20, 35).cuboid(-8.0F, -6.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(32, 26).cuboid(-2.0F, -2.0F, -5.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(42, 6).cuboid(-2.0F, -3.0F, -6.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(10, 41).cuboid(-2.0F, -5.0F, -7.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(0, 24).cuboid(-2.0F, -11.0F, -8.0F, 4.0F, 6.0F, 1.0F, new Dilation(0.0F))
						.uv(0, 45).cuboid(-5.0F, -10.0F, -7.0F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(30, 51).cuboid(-6.0F, -10.0F, -6.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(42, 20).cuboid(-7.0F, -10.0F, -5.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
						.uv(0, 14).cuboid(-8.0F, -10.0F, -3.0F, 1.0F, 4.0F, 6.0F, new Dilation(0.0F))
						.uv(30, 35).cuboid(-8.0F, -11.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(34, 51).cuboid(-3.0F, -10.0F, -8.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(20, 10).cuboid(-4.0F, -2.0F, -4.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(38, 51).cuboid(-4.0F, -6.0F, -7.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(18, 49).cuboid(-7.0F, -6.0F, -4.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(28, 21).cuboid(-6.0F, -6.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(42, 58).cuboid(-5.0F, -6.0F, -6.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(52, 0).cuboid(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(52, 3).cuboid(-6.0F, -4.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(48, 24).cuboid(-4.0F, -3.0F, -5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 30).cuboid(-4.0F, -4.0F, -6.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(52, 38).cuboid(-5.0F, -4.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(58, 36).cuboid(-5.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(32, 45).cuboid(-4.0F, -2.0F, 2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(52, 6).cuboid(-5.0F, -3.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(52, 32).cuboid(-6.0F, -4.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(24, 49).cuboid(-7.0F, -6.0F, 2.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(46, 58).cuboid(-6.0F, -6.0F, 4.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(58, 54).cuboid(-5.0F, -6.0F, 5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(52, 35).cuboid(-4.0F, -6.0F, 6.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 44).cuboid(-4.0F, -4.0F, 5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 52).cuboid(-4.0F, -3.0F, 4.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(10, 36).cuboid(7.0F, -11.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(14, 14).cuboid(7.0F, -10.0F, -3.0F, 1.0F, 4.0F, 6.0F, new Dilation(0.0F))
						.uv(0, 37).cuboid(7.0F, -6.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(52, 49).cuboid(2.0F, -10.0F, -8.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(18, 53).cuboid(-3.0F, -10.0F, 7.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(10, 24).cuboid(-2.0F, -11.0F, 7.0F, 4.0F, 6.0F, 1.0F, new Dilation(0.0F))
						.uv(22, 53).cuboid(2.0F, -10.0F, 7.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(42, 40).cuboid(-7.0F, -10.0F, 3.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
						.uv(26, 53).cuboid(-6.0F, -10.0F, 5.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(6, 46).cuboid(-5.0F, -10.0F, 6.0F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(12, 46).cuboid(3.0F, -10.0F, 6.0F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(44, 53).cuboid(5.0F, -10.0F, 5.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(20, 43).cuboid(6.0F, -10.0F, 3.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
						.uv(40, 46).cuboid(3.0F, -10.0F, -7.0F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(48, 53).cuboid(5.0F, -10.0F, -6.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
						.uv(26, 43).cuboid(6.0F, -10.0F, -5.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
						.uv(20, 29).cuboid(6.0F, -5.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
						.uv(38, 15).cuboid(-3.0F, -16.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(40, 10).cuboid(2.0F, -16.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(0, 7).cuboid(-2.0F, -16.0F, -3.0F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F))
						.uv(20, 40).cuboid(-2.0F, -2.0F, 3.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(42, 8).cuboid(-2.0F, -3.0F, 5.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(0, 42).cuboid(-2.0F, -5.0F, 6.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(0, 54).cuboid(2.0F, -6.0F, 6.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(58, 57).cuboid(4.0F, -6.0F, 5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(30, 59).cuboid(5.0F, -6.0F, 4.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(46, 49).cuboid(6.0F, -6.0F, 2.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(36, 57).cuboid(2.0F, -4.0F, 5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(24, 60).cuboid(4.0F, -4.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(54, 24).cuboid(5.0F, -4.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(38, 54).cuboid(4.0F, -3.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(52, 57).cuboid(2.0F, -3.0F, 4.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(46, 46).cuboid(2.0F, -2.0F, 2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(48, 15).cuboid(2.0F, -2.0F, -4.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(54, 46).cuboid(4.0F, -3.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(52, 54).cuboid(5.0F, -4.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(0, 50).cuboid(6.0F, -6.0F, -4.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(34, 59).cuboid(5.0F, -6.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(38, 59).cuboid(4.0F, -6.0F, -6.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(6, 55).cuboid(2.0F, -6.0F, -7.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(58, 0).cuboid(2.0F, -3.0F, -5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(58, 2).cuboid(2.0F, -4.0F, -6.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(60, 24).cuboid(4.0F, -4.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(12, 55).cuboid(2.0F, -12.0F, -7.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(50, 59).cuboid(4.0F, -12.0F, -6.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(54, 59).cuboid(5.0F, -12.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(50, 10).cuboid(6.0F, -12.0F, -4.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(58, 4).cuboid(2.0F, -13.0F, -6.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(60, 46).cuboid(4.0F, -13.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 9).cuboid(5.0F, -13.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(56, 12).cuboid(4.0F, -14.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(6, 58).cuboid(2.0F, -14.0F, -5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(48, 18).cuboid(2.0F, -15.0F, -4.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(30, 29).cuboid(-7.0F, -13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
						.uv(42, 0).cuboid(-2.0F, -13.0F, -7.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(0, 31).cuboid(6.0F, -13.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
						.uv(42, 3).cuboid(-2.0F, -13.0F, 6.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(10, 44).cuboid(-2.0F, -14.0F, 5.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(40, 29).cuboid(5.0F, -14.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(44, 26).cuboid(-2.0F, -14.0F, -6.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(32, 40).cuboid(-6.0F, -14.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(20, 24).cuboid(-5.0F, -15.0F, -2.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(40, 34).cuboid(-2.0F, -15.0F, -5.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(28, 10).cuboid(3.0F, -15.0F, -2.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F))
						.uv(40, 37).cuboid(-2.0F, -15.0F, 3.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(56, 15).cuboid(2.0F, -12.0F, 6.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(0, 60).cuboid(4.0F, -12.0F, 5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(4, 60).cuboid(5.0F, -12.0F, 4.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(50, 28).cuboid(6.0F, -12.0F, 2.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(56, 18).cuboid(-4.0F, -12.0F, -7.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(8, 60).cuboid(-5.0F, -12.0F, -6.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(12, 60).cuboid(-6.0F, -12.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(6, 51).cuboid(-7.0F, -12.0F, -4.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(16, 60).cuboid(-6.0F, -12.0F, 4.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(20, 60).cuboid(-5.0F, -12.0F, 5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 21).cuboid(-4.0F, -12.0F, 6.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
						.uv(12, 51).cuboid(-7.0F, -12.0F, 2.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
						.uv(58, 6).cuboid(-4.0F, -13.0F, -6.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(58, 60).cuboid(-5.0F, -13.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 27).cuboid(-6.0F, -13.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(30, 56).cuboid(-6.0F, -13.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(42, 61).cuboid(-5.0F, -13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(12, 58).cuboid(-4.0F, -13.0F, 5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(18, 58).cuboid(2.0F, -13.0F, 5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 38).cuboid(5.0F, -13.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(46, 61).cuboid(4.0F, -13.0F, 4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 41).cuboid(4.0F, -14.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(24, 58).cuboid(2.0F, -14.0F, 4.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(58, 32).cuboid(-4.0F, -14.0F, 4.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(56, 49).cuboid(-5.0F, -14.0F, 2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(58, 34).cuboid(-4.0F, -14.0F, -5.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
						.uv(0, 57).cuboid(-5.0F, -14.0F, -4.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(48, 21).cuboid(2.0F, -15.0F, -4.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(32, 48).cuboid(-4.0F, -15.0F, -4.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(48, 40).cuboid(-4.0F, -15.0F, 2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
						.uv(48, 43).cuboid(2.0F, -15.0F, 2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)),
				ModelTransform.origin(0.0F, 16.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(BubbleRenderState state) {
		super.setAngles(state);
	}
}
