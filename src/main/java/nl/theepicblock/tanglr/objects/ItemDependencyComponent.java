package nl.theepicblock.tanglr.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ItemDependencyComponent(long dependency, long generation) {
    public static final Codec<ItemDependencyComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("dependency").forGetter(ItemDependencyComponent::dependency),
                    Codec.LONG.fieldOf("generation").forGetter(ItemDependencyComponent::generation)
            ).apply(instance, ItemDependencyComponent::new)
    );
    public static final StreamCodec<ByteBuf, ItemDependencyComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, ItemDependencyComponent::dependency,
            ByteBufCodecs.VAR_LONG, ItemDependencyComponent::generation,
            ItemDependencyComponent::new
    );
}
