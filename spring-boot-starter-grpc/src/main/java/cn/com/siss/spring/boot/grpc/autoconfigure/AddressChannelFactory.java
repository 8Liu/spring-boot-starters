package cn.com.siss.spring.boot.grpc.autoconfigure;

import cn.com.siss.spring.boot.grpc.properties.GRpcChannelProperties;
import cn.com.siss.spring.boot.grpc.properties.GRpcChannelsProperties;
import com.google.common.collect.Lists;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.internal.GrpcUtil;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * AddressChannelFactory
 *
 * @author John Deng
 */
@Slf4j
public class AddressChannelFactory implements GRpcChannelFactory {
    private final GRpcChannelsProperties properties;
    private final GlobalClientInterceptorRegistry globalClientInterceptorRegistry;

    public AddressChannelFactory(GRpcChannelsProperties properties, GlobalClientInterceptorRegistry globalClientInterceptorRegistry) {
        this.properties = properties;
        this.globalClientInterceptorRegistry = globalClientInterceptorRegistry;
    }

    @Override
    public ManagedChannel createChannel(String name) {
        return this.createChannel(name, null);
    }

    @Override
    public ManagedChannel createChannel(String name, List<ClientInterceptor> interceptors) {
        GRpcChannelProperties channelProperties = properties.getChannel(name);
        String host = channelProperties.getServerHost();
        host = "".equals(host) ? name : host;
        Integer port = channelProperties.getServerPort();
        Long keyAliveDelay = channelProperties.getKeepAliveDelay();

        NegotiationType negotiationType;
        if (channelProperties.isPlaintext()) {
            negotiationType = NegotiationType.PLAINTEXT;
        } else {
            negotiationType = NegotiationType.PLAINTEXT_UPGRADE;
        }

        List<ClientInterceptor> globalInterceptorList = globalClientInterceptorRegistry.getClientInterceptors();

        NettyChannelBuilder channelBuilder;
        if (channelProperties.isEnableKeepAlive()) {
            channelBuilder = NettyChannelBuilder.forAddress(host, port)
                    .negotiationType(negotiationType)
                    .keepAliveTime(keyAliveDelay, TimeUnit.SECONDS)
                    .keepAliveTimeout(channelProperties.getKeepAliveTimeout(), TimeUnit.SECONDS);
        } else {
            channelBuilder = NettyChannelBuilder.forAddress(host, port)
                    .negotiationType(negotiationType)
                    .keepAliveTime(GrpcUtil.KEEPALIVE_TIME_NANOS_DISABLED, TimeUnit.NANOSECONDS);
        }

        ManagedChannel channel = channelBuilder.intercept(globalInterceptorList).build();

        if ((null != channel) && !channel.isTerminated() && !channel.isShutdown()) {
            log.info("gRPC channel - connect to server host: {}, port: {}", host, port);
            log.info("gRPC channel - keep alive : {}, timeout: {} seconds",
                    channelProperties.isEnableKeepAlive() ? "yes" : "no", keyAliveDelay);
        }

        Set<ClientInterceptor> interceptorSet = new HashSet<>();
        return (ManagedChannel)ClientInterceptors.intercept(channel, Lists.newArrayList(interceptorSet));
    }
}

