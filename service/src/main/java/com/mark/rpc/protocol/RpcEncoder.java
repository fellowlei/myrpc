package com.mark.rpc.protocol;

import com.mark.service.util.KryoTool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RPC Encoder
 * @author huangyong
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> targetClazz;

    public RpcEncoder(Class<?> targetClazz) {
        this.targetClazz = targetClazz;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (targetClazz.isInstance(in)) {
            byte[] data =  KryoTool.encodeByte(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
