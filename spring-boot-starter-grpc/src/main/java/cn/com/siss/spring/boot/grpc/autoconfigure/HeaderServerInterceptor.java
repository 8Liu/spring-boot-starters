package cn.com.siss.spring.boot.grpc.autoconfigure;

import cn.com.siss.framework.common.constant.LogConstant;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

@Slf4j
public class HeaderServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        // 获取客户端参数
        Metadata.Key<String> logTraceIdKey = Metadata.Key.of(LogConstant.LOG_TRACE_ID, Metadata.ASCII_STRING_MARSHALLER);
        String logTraceId = metadata.get(logTraceIdKey);
        if (StringUtils.isEmpty(logTraceId)) {
            logTraceId = UUID.randomUUID().toString();
        }
        MDC.put(LogConstant.LOG_TRACE_ID, logTraceId);

        return serverCallHandler.startCall(serverCall, metadata);
    }
}
