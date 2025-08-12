package net.nml.bubble.datagen;

import com.supermartijn642.fusion.api.model.DefaultModelTypes;
import com.supermartijn642.fusion.api.model.ModelInstance;
import com.supermartijn642.fusion.api.model.data.ConnectingModelDataBuilder;
import com.supermartijn642.fusion.api.predicate.DefaultConnectionPredicates;
import com.supermartijn642.fusion.api.provider.FusionModelProvider;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.nml.bubble.BlowingBubbles;
import net.nml.bubble.ModRegistry;
import net.nml.bubble.block.BubbleBlock;

@Environment(EnvType.CLIENT)
public class ConnectedBubbleBlockProvider extends FusionModelProvider {
	public ConnectedBubbleBlockProvider(FabricDataOutput output) {
		super(BlowingBubbles.MOD_ID, output);
	}

	@Override
	protected void generate() {
		for (Pair<BubbleBlock, String> pair : ModRegistry.BUBBLE_BLOCKS) {
			// Block block = pair.getLeft();
			String name = pair.getRight();
			var modelData = ConnectingModelDataBuilder.builder()
				.parent(Identifier.of(BlowingBubbles.MOD_ID, "block/bubble_block"))
				.texture("all", Identifier.of(BlowingBubbles.MOD_ID, "block/bubble_block"))
				.connection(DefaultConnectionPredicates.isSameBlock())
				.build();
			var modelInstance = ModelInstance.of(DefaultModelTypes.CONNECTING, modelData);
			this.addModel(Identifier.of(BlowingBubbles.MOD_ID, "block/" + name), modelInstance);
		}
	}
}