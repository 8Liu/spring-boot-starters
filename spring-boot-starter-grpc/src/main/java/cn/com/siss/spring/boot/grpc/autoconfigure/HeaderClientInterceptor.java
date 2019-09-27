package cn.com.siss.spring.boot.grpc.autoconfigure;

import cn.com.siss.framework.common.constant.LogConstant;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;

@Slf4j
public class HeaderClientInterceptor implements ClientInterceptor {

    private static final String TAG = "HeaderClientInterceptor";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions,
                                                               Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {

            @Override
            public void start(ClientCall.Listener<RespT> responseListener, Metadata headers) {
                /* put custom header */
                Metadata.Key<String> customHeadKey = Metadata.Key.of(LogConstant.LOG_TRACE_ID, Metadata.ASCII_STRING_MARSHALLER);
                headers.put(customHeadKey, MDC.get(LogConstant.LOG_TRACE_ID));
                log.info(TAG, "header send to server:" + headers);
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        /**
                         * if you don't need receive header from server,
                         * you can use {@link io.grpc.stub.MetadataUtils attachHeaders}
                         * directly to send header
                         */
                        log.info(TAG, "header received from server:" + headers);
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
