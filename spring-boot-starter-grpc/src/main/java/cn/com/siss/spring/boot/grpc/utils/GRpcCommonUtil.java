package cn.com.siss.spring.boot.grpc.utils;

import cn.com.siss.framework.common.constant.web.ReturnCodeConstant;
import cn.com.siss.spring.boot.grpc.proto.CommonProto.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Common proto 数据处理工具类
 *
 * @Description:
 * @Author: HJ
 * @CreateDate: 2020/4/14
 * @UpdateUser: HJ
 * @UpdateDate: 2020/4/14
 * @UpdateRemark:
 * @Version: 1.0
 */
public class GRpcCommonUtil {

    /**
     * 创建Long类型请求参数对象
     *
     * @param data
     * @return
     */
    public static BaseLongRequest createLongRequest(Long data) {
        if (null == data) {
            return null;
        }
        return BaseLongRequest.newBuilder().setData(data).build();
    }

    /**
     * 创建Integer类型请求参数对象
     *
     * @param data
     * @return
     */
    public static BaseIntRequest createIntRequest(Integer data) {
        if (null == data) {
            return null;
        }
        return BaseIntRequest.newBuilder().setData(data).build();
    }

    /**
     * 创建String类型请求参数对象
     *
     * @param data
     * @return
     */
    public static BaseStrRequest createSrtRequest(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return BaseStrRequest.newBuilder().setStrData(data).build();
    }

    /**
     * 创建String类型请求参数对象
     *
     * @param dataInfo
     * @return
     */
    public static <T> BaseStrRequest createSrtRequest(T dataInfo) {
        if (null == dataInfo) {
            return null;
        }
        return BaseStrRequest.newBuilder().setStrData(JSONObject.toJSONString(dataInfo)).build();
    }

    /**
     * 创建String类型请求参数对象
     *
     * @param dataList
     * @return
     */
    public static <T> BaseStrRequest createSrtRequest(List<T> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        return BaseStrRequest.newBuilder().setStrData(JSONObject.toJSONString(dataList)).build();
    }

    /**
     * 创建Long类型请求参数对象
     *
     * @param merchantId
     * @param data
     * @return
     */
    public static MerchantLongRequest createLongRequest(Long merchantId, Long data) {
        if (null == merchantId || null == data) {
            return null;
        }
        return MerchantLongRequest.newBuilder().setMerchantId(merchantId).setData(data).build();
    }

    /**
     * 创建Integer类型请求参数对象
     *
     * @param data
     * @return
     */
    public static MerchantIntRequest createIntRequest(Long merchantId, Integer data) {
        if (null == merchantId || null == data) {
            return null;
        }
        return MerchantIntRequest.newBuilder().setMerchantId(merchantId).setData(data).build();
    }

    /**
     * 创建String类型请求参数对象
     *
     * @param data
     * @return
     */
    public static MerchantStrRequest createSrtRequest(Long merchantId, String data) {
        if (null == merchantId || StringUtils.isEmpty(data)) {
            return null;
        }
        return MerchantStrRequest.newBuilder().setMerchantId(merchantId).setData(data).build();
    }

    /**
     * 创建String类型请求参数对象
     *
     * @param dataInfo
     * @return
     */
    public static <T> MerchantStrRequest createSrtRequest(Long merchantId, T dataInfo) {
        if (null == merchantId || null == dataInfo) {
            return null;
        }
        return MerchantStrRequest.newBuilder().setMerchantId(merchantId)
                .setData(JSONObject.toJSONString(dataInfo)).build();
    }

    /**
     * 创建String类型请求参数对象
     *
     * @param dataList
     * @return
     */
    public static <T> MerchantStrRequest createSrtRequest(Long merchantId, List<T> dataList) {
        if (null == merchantId || CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        return MerchantStrRequest.newBuilder().setMerchantId(merchantId)
                .setData(JSONObject.toJSONString(dataList)).build();
    }

    /**
     * 创建商户业务数据处理请求参数Proto对象
     *
     * @param merchantId 商户编号
     * @param dataInfo   业务数据对象
     * @param operator   操作人
     * @param <T>
     * @return
     */
    public static <T> ProcessDataRequest createDataRequest(Long merchantId,
                                                           T dataInfo,
                                                           String operator) {
        if (null == merchantId || null == dataInfo) {
            return null;
        }
        return setDataRequestValue(merchantId, dataInfo, operator);
    }

    /**
     * 创建商户业务数据列表处理请求参数Proto对象
     *
     * @param merchantId 商户编号
     * @param dataList   业务数据列表
     * @param operator   操作人
     * @param <T>
     * @return
     */
    public static <T> ProcessDataRequest createDataRequest(Long merchantId,
                                                           List<T> dataList,
                                                           String operator) {
        if (null == merchantId || CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        return setDataRequestValue(merchantId, dataList, operator);
    }

    /**
     * 创建ProtoResponse基础返回对象
     *
     * @param returnCode
     * @param message
     * @return
     */
    public static ProtoResponse createProtoResponse(Integer returnCode, String message) {
        if (null == returnCode) {
            return null;
        }
        ProtoResponse.Builder builder = ProtoResponse.newBuilder();
        builder.setReturnCode(returnCode);
        if (!StringUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        return builder.build();
    }

    /**
     * 创建BaseStrResponse基础返回对象
     *
     * @param dataStr
     * @return
     */
    public static BaseStrResponse createStrResponse(String dataStr) {
        if (StringUtils.isEmpty(dataStr)) {
            return null;
        }
        return BaseStrResponse.newBuilder().setStrData(dataStr).build();
    }

    /**
     * 创建BaseStrResponse基础返回对象
     *
     * @param dataInfo
     * @return
     */
    public static <T> BaseStrResponse createStrResponse(T dataInfo) {
        if (null == dataInfo) {
            return null;
        }
        return BaseStrResponse.newBuilder().setStrData(JSONObject.toJSONString(dataInfo)).build();
    }

    /**
     * 创建BaseStrResponse基础返回对象
     *
     * @param dataList
     * @return
     */
    public static <T> BaseStrResponse createStrResponse(List<T> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        return BaseStrResponse.newBuilder().setStrData(JSONObject.toJSONString(dataList)).build();
    }

    /**
     * 创建BaseStrResponse基础返回对象
     *
     * @param dataMap
     * @return
     */
    public static BaseStrResponse createStrResponse(Map dataMap) {
        if (CollectionUtils.isEmpty(dataMap)) {
            return null;
        }
        return BaseStrResponse.newBuilder().setStrData(JSONObject.toJSONString(dataMap)).build();
    }

    /**
     * 创建JSONDataResponse基础返回对象
     *
     * @param returnCode 返回编码
     * @param dataStr    业务数据
     * @param message    返回消息
     * @return
     */
    public static JSONDataResponse createDataResponse(Integer returnCode,
                                                      String dataStr,
                                                      String message) {
        if (null == returnCode || (ReturnCodeConstant.CODE_1000.equals(returnCode)
                && StringUtils.isEmpty(dataStr))) {
            return null;
        }
        return setDataResponseValue(returnCode, dataStr, message);
    }

    /**
     * 创建JSONDataResponse基础返回对象
     *
     * @param returnCode 返回编码
     * @param dataInfo   业务数据
     * @param message    返回消息
     * @return
     */
    public static <T> JSONDataResponse createDataResponse(Integer returnCode, T dataInfo, String message) {
        if (null == returnCode || (ReturnCodeConstant.CODE_1000.equals(returnCode)
                && null == dataInfo)) {
            return null;
        }
        String jsonData = null;
        if (null != dataInfo) {
            jsonData = JSONObject.toJSONString(dataInfo);
        }
        return setDataResponseValue(returnCode, jsonData, message);
    }

    /**
     * 创建JSONDataResponse基础返回对象
     *
     * @param returnCode 返回编码
     * @param dataList   业务数据列表
     * @param message    返回消息
     * @return
     */
    public static <T> JSONDataResponse createDataResponse(Integer returnCode, List<T> dataList, String message) {
        if (null == returnCode || (ReturnCodeConstant.CODE_1000.equals(returnCode)
                && CollectionUtils.isEmpty(dataList))) {
            return null;
        }
        String jsonData = null;
        if (!CollectionUtils.isEmpty(dataList)) {
            jsonData = JSONObject.toJSONString(dataList);
        }
        return setDataResponseValue(returnCode, jsonData, message);
    }

    /**
     * 创建JSONDataResponse基础返回对象
     *
     * @param returnCode 返回编码
     * @param dataMap    业务数据MAP
     * @param message    返回消息
     * @return
     */
    public static JSONDataResponse createDataResponse(Integer returnCode, Map dataMap, String message) {
        if (null == returnCode || (ReturnCodeConstant.CODE_1000.equals(returnCode)
                && CollectionUtils.isEmpty(dataMap))) {
            return null;
        }
        String jsonData = null;
        if (!CollectionUtils.isEmpty(dataMap)) {
            jsonData = JSONObject.toJSONString(dataMap);
        }
        return setDataResponseValue(returnCode, jsonData, message);
    }

    /**
     * 从BaseStrRequest对象中获取数据对象信息
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getDataFromRequest(BaseStrRequest request, Class<T> clazz) {
        if (null == request || StringUtils.isEmpty(request.getStrData())) {
            return null;
        } else {
            return JSONObject.parseObject(request.getStrData(), clazz);
        }
    }

    /**
     * 从BaseStrRequest对象中获取数据对象信息表表
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataListFromRequest(BaseStrRequest request, Class<T> clazz) {
        if (null == request || StringUtils.isEmpty(request.getStrData())) {
            return null;
        } else {
            return JSONObject.parseArray(request.getStrData(), clazz);
        }
    }

    /**
     * 从BaseStrRequest对象中获取数据对象信息
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getDataFromRequest(MerchantStrRequest request, Class<T> clazz) {
        if (null == request || StringUtils.isEmpty(request.getData())) {
            return null;
        } else {
            return JSONObject.parseObject(request.getData(), clazz);
        }
    }

    /**
     * 从BaseStrRequest对象中获取数据对象信息表表
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataListFromRequest(MerchantStrRequest request, Class<T> clazz) {
        if (null == request || StringUtils.isEmpty(request.getData())) {
            return null;
        } else {
            return JSONObject.parseArray(request.getData(), clazz);
        }
    }

    /**
     * 从ProcessDataRequest对象中获取数据对象信息
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getDataFromRequest(ProcessDataRequest request, Class<T> clazz) {
        if (null == request || StringUtils.isEmpty(request.getJsonData())) {
            return null;
        } else {
            return JSONObject.parseObject(request.getJsonData(), clazz);
        }
    }

    /**
     * 从ProcessDataRequest对象中获取数据对象信息表表
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataListFromRequest(ProcessDataRequest request, Class<T> clazz) {
        if (null == request || StringUtils.isEmpty(request.getJsonData())) {
            return null;
        } else {
            return JSONObject.parseArray(request.getJsonData(), clazz);
        }
    }

    /**
     * 从BaseStrResponse对象中获取数据对象信息
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getDataFromResponse(BaseStrResponse response, Class<T> clazz) {
        if (null == response || StringUtils.isEmpty(response.getStrData())) {
            return null;
        } else {
            return JSONObject.parseObject(response.getStrData(), clazz);
        }
    }

    /**
     * 从BaseStrResponse对象中获取数据对象信息表表
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataListFromResponse(BaseStrResponse response, Class<T> clazz) {
        if (null == response || StringUtils.isEmpty(response.getStrData())) {
            return null;
        } else {
            return JSONObject.parseArray(response.getStrData(), clazz);
        }
    }

    /**
     * 从JSONDataResponse对象中获取数据对象信息
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getDataFromResponse(JSONDataResponse response, Class<T> clazz) {
        if (null == response || !ReturnCodeConstant.CODE_1000.equals(response.getReturnCode())
                || StringUtils.isEmpty(response.getJsonData())) {
            return null;
        } else {
            return JSONObject.parseObject(response.getJsonData(), clazz);
        }
    }

    /**
     * 从JSONDataResponse对象中获取数据对象信息表表
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getDataListFromResponse(JSONDataResponse response, Class<T> clazz) {
        if (null == response || !ReturnCodeConstant.CODE_1000.equals(response.getReturnCode())
                || StringUtils.isEmpty(response.getJsonData())) {
            return null;
        } else {
            return JSONObject.parseArray(response.getJsonData(), clazz);
        }
    }

    /**
     * 设置商户业务数据处理请求参数Proto对象值
     *
     * @param merchantId 商户编号
     * @param data       业务数据
     * @param operator   操作人
     * @return
     */
    private static ProcessDataRequest setDataRequestValue(Long merchantId,
                                                          Object data,
                                                          String operator) {
        ProcessDataRequest.Builder builder = ProcessDataRequest.newBuilder();
        builder.setMerchantId(merchantId);
        builder.setJsonData(JSONObject.toJSONString(data));
        if (!StringUtils.isEmpty(operator)) {
            builder.setOperator(operator);
        }
        return builder.build();
    }

    /**
     * 设置JSON格式的业务数据返回对象值
     *
     * @param returnCode 返回编码
     * @param jsonData   业务数据
     * @param message    返回消息
     * @return
     */
    private static JSONDataResponse setDataResponseValue(Integer returnCode,
                                                         String jsonData,
                                                         String message) {
        JSONDataResponse.Builder builder = JSONDataResponse.newBuilder();
        builder.setReturnCode(returnCode);
        if (!StringUtils.isEmpty(jsonData)) {
            builder.setJsonData(jsonData);
        }
        if (!StringUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        return builder.build();
    }

}
