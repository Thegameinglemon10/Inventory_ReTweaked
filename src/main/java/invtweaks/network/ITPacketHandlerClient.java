package invtweaks.network;

import invtweaks.network.packets.ITPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;

public class ITPacketHandlerClient extends SimpleChannelInboundHandler<ITPacket> {
    @Override
    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull ITPacket msg) throws Exception {
        @NotNull final NetHandlerPlayClient handler = (NetHandlerPlayClient) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        Minecraft.getMinecraft().addScheduledTask(() -> msg.handle(handler));
    }
}
