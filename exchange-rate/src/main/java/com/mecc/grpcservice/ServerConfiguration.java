package com.mecc.grpcservice;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

// Fine tunning gRPC see:
// https://medium.com/@mahdiyusefi72/first-step-to-tune-grpc-in-java-8b57f7f0f591
// https://groups.google.com/g/grpc-io/c/LrnAbWFozb0?pli=1

@Configuration
public class ServerConfiguration {

    @Bean
    public GrpcServerConfigurer fineTuneServerConfigurer() {
        return serverBuilder -> ((NettyServerBuilder) serverBuilder)
            .bossEventLoopGroup(new NioEventLoopGroup(2))
            .workerEventLoopGroup(new NioEventLoopGroup(5))
            .channelType(NioServerSocketChannel.class)
            .executor(Executors.newFixedThreadPool(100,
                    new ThreadFactoryBuilder().setNameFormat("grpc-%d").build()));
    }
}
