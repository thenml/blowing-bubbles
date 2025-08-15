package net.nml.bubble;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
	// 0 - fast, 1 - simple, 2 - small, 3 - medium when big
	@Entry(category = "client", isSlider=true) public static ModelQuality bubbleQuality = ModelQuality.QUALITY;
	public enum ModelQuality {
        FAST, PLAIN, SIMPLE, QUALITY
    }
}
