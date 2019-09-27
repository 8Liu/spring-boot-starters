package cn.com.siss.spring.boot.grpc.autoconfigure;

import cn.com.siss.spring.boot.grpc.annotations.GRpcGlobalInterceptor;
import io.grpc.ClientInterceptor;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GlobalClientInterceptorRegistry
 *
 * @author John Deng
 */
@Getter
public class GlobalClientInterceptorRegistry implements ApplicationContextAware {

    private final List<ClientInterceptor> clientInterceptors = new ArrayList<>();

    private ApplicationContext applicationContext;

    @Autowired
    private AbstractApplicationContext abstractApplicationContext;

    @PostConstruct
    public void init() {
        Map<String, GlobalClientInterceptorConfigurerAdapter> map = applicationContext.getBeansOfType(GlobalClientInterceptorConfigurerAdapter.class);
        for (GlobalClientInterceptorConfigurerAdapter globalClientInterceptorConfigurerAdapter : map.values()) {
            globalClientInterceptorConfigurerAdapter.addClientInterceptors(this);
        }

        Collection<ClientInterceptor> globalClientInterceptors = getBeanNamesByTypeWithAnnotation(GRpcGlobalInterceptor.class, ClientInterceptor.class)
                .map(name -> abstractApplicationContext.getBeanFactory().getBean(name, ClientInterceptor.class))
                .collect(Collectors.toList());
        clientInterceptors.addAll(globalClientInterceptors);

    }

    public GlobalClientInterceptorRegistry addClientInterceptors(ClientInterceptor interceptor) {
        clientInterceptors.add(interceptor);
        return this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private <T> Stream<String> getBeanNamesByTypeWithAnnotation(Class<? extends Annotation> annotationType, Class<T> beanType){
        return Stream.of(applicationContext.getBeanNamesForType(beanType))
                .filter(name -> { BeanDefinition beanDefinition = abstractApplicationContext.getBeanFactory().getBeanDefinition(name);
                    if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                        StandardMethodMetadata metadata = (StandardMethodMetadata) beanDefinition.getSource();
                        return metadata.isAnnotated(annotationType.getName());
                    }
                    return null != abstractApplicationContext.getBeanFactory().findAnnotationOnBean(name, annotationType);
                });
    }
}