package net.nml.bubble.renderer;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;

public class BubbleRenderState extends LivingEntityRenderState {
    public float size = 1.0f;
    public float opacity = 1.0f;
	public int color = 0;
    
    public BubbleRenderState() {
        super();
    }
} 