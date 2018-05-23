package com.mark.server.domain;

import com.mark.service.util.KryoTool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by lulei on 2018/5/22.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> targetClazz;

    public RpcDecoder(Class<?> targetClazz) {
        this.targetClazz = targetClazz;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        /*if (dataLength <= 0) {
            ctx.close();
        }*/
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = KryoTool.decodeByte(data, targetClazz);
        out.add(obj);
    }

}
