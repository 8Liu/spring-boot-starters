package cn.com.siss.spring.boot.grpc.utils;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;

//import com.google.protobuf.util.JsonFormat;

@Slf4j
public class GRpcMessageConverter {

    private static Map<String, Callable<?>> primitivesMap;

    static {
        primitivesMap = new HashMap<>();
        primitivesMap.put("java.lang.Boolean", () -> new Boolean(false));
        primitivesMap.put("java.lang.Character", () -> new Character('0'));
        primitivesMap.put("java.lang.Byte", () -> new Byte("0"));
        primitivesMap.put("java.lang.Short", () -> new Short((short) 0));
        primitivesMap.put("java.lang.Integer", () -> new Integer(0));
        primitivesMap.put("java.lang.Long", () -> new Long(0L));
        primitivesMap.put("java.lang.Float", () -> new Float(0.0));
        primitivesMap.put("java.lang.Double", () -> new Double(0.0));
        primitivesMap.put("java.lang.String", () -> new String(""));
    }

    protected static Object getField(PropertyAccessor propertyAccessor, String name) {
        Object value = null;
        try {
            value = propertyAccessor.getPropertyValue(name);
            log.trace("Get Object field: {}, value: {}", name, value);
        } catch (Exception e) {
            log.trace("Get object field exception: {}", e);
        }
        return value;
    }

    protected static void setField(PropertyAccessor propertyAccessor, String name, Object value) {
        try {
            log.trace("Set Object field: {}, value: {}", name, value);
            if (null != value) {
                if (Date.class.equals(propertyAccessor.getPropertyType(name))) {
                    if (value instanceof Long) {
                        Long timeValue = (Long) value;
                        // 如果值为Long类型的最小值时, 日期设为NULL
                        if (timeValue > Long.MIN_VALUE) {
                            value = new Date(timeValue);
                        } else {
                            value = null;
                        }
                    } else if (!(value instanceof Date)) {
                        value = null;
                    }
                } else if (BigDecimal.class.equals(propertyAccessor.getPropertyType(name))) {
                    if (value instanceof Number) {
                        value = new BigDecimal(value.toString());
                    } else {
                        value = null;
                    }
                }
            }
            propertyAccessor.setPropertyValue(name, value);
        } catch (Exception e) {
            log.trace("Set object field exception: {}", e);
        }
    }

    /**
     * Set gRpc field
     *
     * @param builder
     * @param field
     * @param value
     */
    protected static void setField(Builder builder, Descriptors.FieldDescriptor field, Object value) {
        try {
            log.trace("Set gRpc field: {}, value: {}", field.getName(), value);
            if ("char[]".equals(value.getClass().getTypeName())) {
                value = new String((char[]) value);
            } else if (value instanceof Date) {
                value = ((Date) value).getTime();
            } else if (value instanceof BigDecimal) {
                value = ((BigDecimal) value).doubleValue();
            }
            builder.setField(field, value);
        } catch (Exception e) {
            log.trace("Set gRpc field exception: {}", e);
        }
    }

    protected static void setRepeatedField(Builder builder, Descriptors.FieldDescriptor field, Object value) {
        try {
            log.trace("Set gRpc repeated field: {}, value: {}", field.getName(), value);
            builder.addRepeatedField(field, value);
        } catch (Exception e) {
            log.trace("Set gRpc field exception: {}", e);
        }
    }

    protected static MessageOrBuilder getFieldBuilder(Builder builder, Descriptors.FieldDescriptor field) {
        try {
            log.trace("Get gRpc field builder: {}", field.getName());
            return builder.newBuilderForField(field);
        } catch (Exception e) {
            log.trace("Set gRpc field exception: {}", e);
        }
        return null;
    }

    private static Object newInstance(Class<?> clazz) {
        Object object = null;

        Callable<?> getPrimitiveInstance = null;
        if (null != clazz) {
            getPrimitiveInstance = primitivesMap.get(clazz.getTypeName());
        }
        if (null != getPrimitiveInstance) {
            try {
                return getPrimitiveInstance.call();
            } catch (Exception e) {
                log.trace("{}", e);
            }
        }

        try {
            object = BeanUtils.instantiate(clazz);
        } catch (Exception e) {
            log.trace("{}", e);
        }

        return object;
    }


    public static List<Object> fromGRpcMessages(Collection<MessageOrBuilder> messages, Class<?> clazz) {
        List<Object> objects = new ArrayList<>();

        int i = 0;
        for (MessageOrBuilder message : messages) {
            Object value = fromGRpcMessage(message, clazz);
            objects.add(i++, value);
        }
        return objects;
    }

    public static <T> T fromGRpcMessage(MessageOrBuilder message, Class<T> clazz) {
        return fromGRpcMessage(message, clazz, null);
    }

    /*public static <T> T fromGRPcMessage(MessageOrBuilder message, TypeReference<T> typeReference) {
        try {
            String jsonString = JsonFormat.printer().includingDefaultValueFields().print(message);
            return JSONObject.parseObject(jsonString, typeReference);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static <T> T fromGRpcMessage(MessageOrBuilder message, Class<T> clazz, Class<?> genericClazz) {

        T object = null;
        try {
            object = clazz.newInstance();
            PropertyAccessor propertyAccessor = PropertyAccessorFactory.forDirectFieldAccess(object);
            Descriptors.Descriptor fieldDescriptor = message.getDescriptorForType();
            if (null == propertyAccessor || null == fieldDescriptor) {
                return null;
            }

            for (Descriptors.FieldDescriptor field : fieldDescriptor.getFields()) {
                Object value = message.getField(field);
                String fieldName = field.getName();
                if (Descriptors.FieldDescriptor.Type.MESSAGE == field.getType()) {
                    String valueTypeName = value.getClass().getName();
                    if (("java.util.Collections$EmptyList").equals(valueTypeName)) {
                        continue;
                    } else if (("java.util.Collections$UnmodifiableRandomAccessList").equals(valueTypeName)) {
                        if (null == genericClazz) {
                            Field declaredField = clazz.getDeclaredField(fieldName);
                            // 获取Collection集合属性定义的泛型类别
                            genericClazz = getCollectionFiledGenericType(declaredField);
                        }
                        value = fromGRpcMessages((Collection<MessageOrBuilder>) value, genericClazz);
                    } else {
                        if ("".equals(value.toString())) {
                            continue;
                        }
                        Class<?> fieldType = propertyAccessor.getPropertyType(fieldName);
                        if (null != genericClazz && null != fieldType && ("java.lang.Object").equals(fieldType.getName())) {
                            fieldType = genericClazz;
                        }
                        value = fromGRpcMessage((MessageOrBuilder) value, fieldType, genericClazz);
                    }
                }

                setField(propertyAccessor, fieldName, value);
            }
        } catch (Exception e) {
            log.debug("Exception: {}", e);
        }

        return object;
    }

    public static void toGRpcMessages(Collection<?> objects, Builder builder, Descriptors.FieldDescriptor field) {

        for (Object object : objects) {
            Message value = null;
            try {
                value = toGRpcMessage(object, getFieldBuilder(builder, field));
            } catch (Exception e) {
                log.debug("{}", e);
            }
            setRepeatedField(builder, field, value);
        }
    }

    public static Message toGRpcMessage(Object object, MessageOrBuilder message) {
        Builder builder = (Builder) message;
        try {

            if (null == object) {
                return builder.build().getDefaultInstanceForType();
            }
            PropertyAccessor propertyAccessor = PropertyAccessorFactory.forDirectFieldAccess(object);
            Descriptors.Descriptor fieldDescriptor = message.getDescriptorForType();
            for (Descriptors.FieldDescriptor field : fieldDescriptor.getFields()) {
                Object value = getField(propertyAccessor, field.getName());
                if (null == value) {
                    // 如果是日期类型, 则赋为Long类的最小值
                    if (Date.class.equals(propertyAccessor.getPropertyType(field.getName()))) {
                        value = Long.MIN_VALUE;
                    } else {
                        continue;
                    }
                }

                if (Descriptors.FieldDescriptor.Type.MESSAGE == field.getType()) {
                    Builder fieldBuilder = (Builder) getFieldBuilder(builder, field);
                    if (value instanceof Collection) {
                        toGRpcMessages((Collection<?>) value, builder, field);
                    } else if (null != fieldBuilder) {
                        Object nestedValue = toGRpcMessage(value, fieldBuilder);
                        setField(builder, field, nestedValue);
                    }
                } else {
                    setField(builder, field, value);
                }
            }
        } catch (Exception e) {
            log.debug("{}", e);
        }

        return builder.build();
    }

    /**
     * 获取Collection集合属性定义的泛型类别
     *
     * @param filed
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?> getCollectionFiledGenericType(Field filed) throws ClassNotFoundException {
        if (!Arrays.asList(filed.getType().getInterfaces()).contains(Collection.class)) {
            throw new RuntimeException("getCollectionFiledGenericType方法需要传Collection类型的Filed属性");
        }
        Type genericType = filed.getGenericType();
        Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
        if (types != null && types.length == 1) {
            return Class.forName(types[0].getTypeName());
        }
        throw new RuntimeException("获取集合属性的泛型失败");
    }
}
