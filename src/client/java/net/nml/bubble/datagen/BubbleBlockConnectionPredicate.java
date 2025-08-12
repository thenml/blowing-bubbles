package net.nml.bubble.datagen;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.supermartijn642.fusion.api.predicate.ConnectionDirection;
import com.supermartijn642.fusion.api.predicate.ConnectionPredicate;
import com.supermartijn642.fusion.api.util.Serializer;
import com.supermartijn642.fusion.util.IdentifierUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class BubbleBlockConnectionPredicate implements ConnectionPredicate {
    public static final Serializer<BubbleBlockConnectionPredicate> SERIALIZER = new Serializer<>() {
        @Override
        public BubbleBlockConnectionPredicate deserialize(JsonObject json) throws JsonParseException{
            if(!json.has("block") || !json.get("block").isJsonPrimitive() || !json.getAsJsonPrimitive("block").isString())
                throw new JsonParseException("Match block predicate must have string property 'block'!");
            if(!IdentifierUtil.isValidIdentifier(json.get("block").getAsString()))
                throw new JsonParseException("Property 'block' must be a valid identifier!");
            Identifier identifier = Identifier.of(json.get("block").getAsString());
            if(!Registries.BLOCK.containsId(identifier))
                throw new JsonParseException("Unknown block '" + identifier + "'!");
            Block block = Registries.BLOCK.get(identifier);
            return new BubbleBlockConnectionPredicate(block);
        }

        @Override
        public JsonObject serialize(BubbleBlockConnectionPredicate value){
            JsonObject json = new JsonObject();
            json.addProperty("block", Registries.BLOCK.getKey(value.block).get().getValue().toString());
            return json;
        }
    };

    private final Block block;

    public BubbleBlockConnectionPredicate(Block block){
        this.block = block;
    }

    @Override
    public Serializer<? extends ConnectionPredicate> getSerializer(){
        return SERIALIZER;
    }

    @Override
    public final boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof BubbleBlockConnectionPredicate that)) return false;

        return this.block.equals(that.block);
    }

    @Override
    public int hashCode(){
        return this.block.hashCode();
    }

    @Override
    public boolean shouldConnect(Direction side, @Nullable BlockState ownState, BlockState otherState, BlockState blockInFront, ConnectionDirection direction) {
		if (ownState == null)
			return false;
		if (otherState.getBlock() != this.block)
			return false;

		if (getConnectionCount(otherState) <= 4)
			return true;

        // Get properties from this block's state and the other block's state
        boolean ownValue = getBooleanPropertyForDirection(side, ownState);
        boolean otherValue = getBooleanPropertyForDirection(side.getOpposite(), otherState);

		// TODO: im tired
		// TODO: also it crashes when breaking blocks
        return ownValue && otherValue;
    }

	private int getConnectionCount(BlockState state) {
		int connectionCount = 0;
		if (state.get(Properties.NORTH)) connectionCount++;
		if (state.get(Properties.SOUTH)) connectionCount++;
		if (state.get(Properties.EAST)) connectionCount++;
		if (state.get(Properties.WEST)) connectionCount++;
		if (state.get(Properties.UP)) connectionCount++;
		if (state.get(Properties.DOWN)) connectionCount++;
		return connectionCount;
	}

    private boolean getBooleanPropertyForDirection(Direction direction, BlockState state) {
        switch (direction) {
            case NORTH: return state.get(Properties.NORTH);
            case SOUTH: return state.get(Properties.SOUTH);
            case EAST:  return state.get(Properties.EAST);
            case WEST:  return state.get(Properties.WEST);
			case UP:    return state.get(Properties.UP);
			case DOWN:  return state.get(Properties.DOWN);
            default:    return false;
        }
    }
}
