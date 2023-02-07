package com.tech.fate.portal;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tech.fate.portal.dto.ComponentsDto;
import com.tech.fate.portal.dto.ComponentsParamsDto;
import com.tech.fate.portal.dto.ComponentsParamsSettingsDto;
import com.tech.fate.portal.mapper.ComponentsMapper;
import com.tech.fate.portal.service.node.ComponentsService;
import com.tech.fate.portal.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class ComponentsTest {

    @Test
    public void run() {
        List<ComponentsDto> componentsDtoList = Lists.newArrayList();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                componentsDtoList.add(build(i, j));
            }
        }
        log.info("db 组件 list = {}", componentsDtoList);
        List<ComponentsVo> componentsGroup = BeanUtil.copyToList(componentsDtoList.stream().distinct().collect(Collectors.toList()), ComponentsVo.class);
        log.info("db 组件 分组 list = {}", componentsGroup);
        Map<String, List<ComponentsDto>> componentsGroupDetails = componentsDtoList.stream().collect(Collectors.groupingBy(componentsDto -> componentsDto.getGroupId()));
        log.info("db 组件 组内详情 list = {}", componentsGroupDetails);
        componentsGroup.forEach(componentsVo -> buildComponentsVo(componentsVo, componentsGroupDetails));
        log.info("db 组件 分组整合 list = {}", componentsGroup);
    }

    private ComponentsDto build(int i, int j) {
        ComponentsDto componentsDto = new ComponentsDto();
        componentsDto.setId(String.valueOf(i));
        componentsDto.setGroupId(String.valueOf(i));
        componentsDto.setGroupName("分组" + i);
        componentsDto.setStatus("1");
        componentsDto.setNodeId(i + "-" + j);
        componentsDto.setNodeName("组件" + j);
        componentsDto.setNodeLogo("logo");
        return componentsDto;
    }

    private void buildComponentsVo(ComponentsVo componentsVo, Map<String, List<ComponentsDto>> componentsGroupDetails) {
        componentsVo.setChildren(BeanUtil.copyToList(componentsGroupDetails.get(componentsVo.getGroupId()), ComponentsBaseInfo.class));
        componentsVo.setNodeNum(componentsGroupDetails.get(componentsVo.getGroupId()).size());
    }


    @Autowired
    private ComponentsService componentsService;

    @Test
    public void componentsList() {
        ComponentsDto componentsDto = new ComponentsDto();
        componentsDto.setStatus("1");
        List<ComponentsVo> result = componentsService.algorithmComponents();
        log.info("查询组件列表结果= {}", JSONObject.toJSONString(result));
    }

    @Test
    public void saveComponentsSettings() {
        ComponentsParamsSettings componentsParamsSettings = new ComponentsParamsSettings();
        componentsParamsSettings.setProjectUuid("1");
        componentsParamsSettings.setTaskUuid("1");
        componentsParamsSettings.setNodeId("1");
        JSONObject settings = new JSONObject();
        settings.put("tranTimes", "3");
        settings.put("data", "/xxx/xxx.csv");
        settings.put("maxLimit", "1.0");
        componentsParamsSettings.setParamSettings(JSONObject.toJSONString(settings));
        log.info("settings = {}", JSONObject.toJSONString(componentsParamsSettings));
        componentsService.saveAlgorithmComponentsParamsSettings(componentsParamsSettings);
    }

    @Test
    public void queryComponentsSettings() {
        ComponentsParamsSettingsDto componentsParamsSettings = new ComponentsParamsSettingsDto();
        componentsParamsSettings.setProjectUuid("1");
        componentsParamsSettings.setTaskUuid("1");
        componentsParamsSettings.setNodeId("1");
//        JSONObject settings = new JSONObject();
//        settings.put("tranTimes", "3");
//        settings.put("data", "/xxx/xxx.csv");
//        settings.put("maxLimit", "1.0");
//        componentsParamsSettings.setParamSettings(JSONUtil.toJsonStr(settings));
        log.info("settings = {}", JSONObject.toJSONString(componentsParamsSettings));
        List<ComponentsParamsSettings> componentsParamsSettings1 = componentsService.queryAlgorithmComponentsParamsSettings("1", "1", "1");
        log.info("result = {}", JSONObject.toJSONString(componentsParamsSettings1));
    }

    @Test
    public void jobParam() {
        String menus = ResourceUtil.readUtf8Str("data/dslReader.json");
        JSONObject jobParam = JSONObject.parseObject(menus);
//        jobParam.get("output").put("module", "new");
        updateJson(jobParam, "data", Lists.newArrayList("qqqqq"));
//        jobParam.get(JobConstants.JOB_CONF, JSONObject.class).get(JobConstants.CONF_ROLE, JSONObject.class).put("host", Lists.newArrayList("10000"));
        log.info("== {} ", jobParam.keySet());
    }

    public static Object updateJson(Object objJson, String nodeKey, Object nodeValue) {
        //如果obj为json数组
        if (objJson instanceof JSONArray) {
            JSONArray objArray = (JSONArray) objJson;
            for (int i = 0; i < objArray.size(); i++) {
                updateJson(objArray.get(i), nodeKey, nodeValue);
            }
        } else if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            Iterator it = jsonObject.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                if (object instanceof JSONArray) {
                    JSONArray objArray = (JSONArray) object;
                    updateJson(objArray, nodeKey, nodeValue);
                } else if (object instanceof JSONObject) {
                    updateJson(object, nodeKey, nodeValue);
                } else {
                    if (key.equals(nodeKey)) {
                        //替换数据
                        jsonObject.put(key, nodeValue);
                    }
                }
            }
        }
        return objJson;
    }

    @Value("#{'${fate.arbiter}'.split(',')}")
    private List Arbiter;
    @Value("${fate.job.parameters.common.auto.retries}")
    private Integer autoRetries;
    @Value("${fate.job.parameters.common.computing.partitions}")
    private Integer computingPartitions;
    @Value("${fate.job.parameters.common.task.cores}")
    private Integer taskCores;
    @Value("${fate.job.parameters.common.task.parallelism}")
    private Integer taskParallelism;

    @Test
    public void testValue() {
//        log.info("Arbiter = {}", Arbiter);
//        cn.hutool.json.JSONObject jobParameters = new cn.hutool.json.JSONObject();
//        String jobParameter = ResourceUtil.readUtf8Str("data/jobParameters.json");
//        jobParameter = jobParameter.replace("@" + JobConstants.CONF_JOB_PARAMS_AUTO_RETRIES, String.valueOf(autoRetries))
//                .replace("@" + JobConstants.CONF_JOB_PARAMS_COMPUTING_PARTITIONS, String.valueOf(computingPartitions))
//                .replace("@" + JobConstants.CONF_JOB_PARAMS_TASK_CORES, String.valueOf(taskCores))
//                .replace("@" + JobConstants.CONF_JOB_PARAMS_TASK_PARALLELISM, String.valueOf(taskParallelism));
//        log.info("jobParameter = {}", jobParameter);
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        for (int i = 0; i < 3; i++) {
            run(jsonObject, String.valueOf(i));
        }
        log.info("JsonObject = {}", jsonObject);
    }

    private void run(cn.hutool.json.JSONObject jsonObject, String ss) {
        jsonObject.set(ss, ss);
    }

//    @Autowired
//    private DemoService demoService;
//
//    @Test
//    public void testList() throws InterruptedException {
//        CountDownLatch countDownLatch = new CountDownLatch(5);
//        List<ComponentsStatus> componentsStatusList = Lists.newLinkedList();
//        for (int i = 0; i < 5; i++) {
//            log.info("index = {}", i);
//            demoService.status(componentsStatusList, i, countDownLatch);
//        }
//        countDownLatch.await();
//        log.info("list = {}", JSONUtil.toJsonStr(componentsStatusList));
//
//    }

    @Autowired
    private ComponentsMapper componentsMapper;

    @Test
    public void testRecursion() {
        ComponentsParamsDto componentsDto = new ComponentsParamsDto();
        componentsDto.setNodeId("dataio");
        componentsDto.setStatus(1);
//        List<ComponentsParams> componentsParamsDtoList = componentsMapper.queryAlgorithmComponentsParams(componentsDto);
//        log.info("原始数据 = {}", JSONUtil.toJsonStr(componentsParamsDtoList));
//        List<ComponentsParams> result = buildDeptTreeByStream(componentsParamsDtoList);
//        List list = componentsParamsDtoList.stream().map(componentsParams -> JSONUtil.parseObj(componentsParams.getParamValue())).collect(Collectors.toList());
//        log.info("递归后的数据 = {}", JSONUtil.toJsonStr(list));
    }

//    public static List<ComponentsParams> buildDeptTreeByStream(List<ComponentsParams> trees) {
//        //获取parentId = 0的根节点
//        List<ComponentsParams> list = trees.stream().filter(item -> item.getParentId().equals("0")).collect(Collectors.toList());
//        //根据parentId进行分组
//        Map<String, List<ComponentsParams>> map = trees.stream().collect(Collectors.groupingBy(ComponentsParams::getParentId));
//        recursionFnTree(list, map);
//        return list;
//    }
//
//    public static void recursionFnTree(List<ComponentsParams> list, Map<String, List<ComponentsParams>> map) {
//        for (ComponentsParams componentsParams : list) {
//            List<ComponentsParams> childList = map.get(componentsParams.getUuid());
//            componentsParams.setChildParamValue(childList);
//            if (null != childList && 0 < childList.size()) {
//                recursionFnTree(childList, map);
//            }
//        }
//    }

    @Test
    public void runList() {
        String str = " [ { \"field1\": \"aaa\", \"field2\": \"sss\" }, { \"field1\": \"aaa\", \"field2\": \"sss\" } ]";
        List list = JSONUtil.toList(str, JSONObject.class);
        log.info("=== {}", list);

    }

    private static Object transType(Object value) {
        String str = JSONUtil.toJsonStr(value);
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (str.contains(".")) {
            return Double.parseDouble(str);
        } else if (pattern.matcher(str).matches()) {
            return Integer.valueOf(str);
        }else if("null".equals(str)){
            return "null";
        }
        return value;
    }

    public static void main(String[] args) {
//       t();
       replace();
    }

    private static void replace(){
        String reader = ResourceUtil.readUtf8Str("data/confReader.json");
        reader = reader.replace("@name", "name").replace("@namespace", "namespace");
        log.info("after = {}",reader);
    }

    private static void forEachJson(Map.Entry o) {
        if (JSONUtil.isTypeJSON(JSONUtil.toJsonStr(o.getValue()))) {
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(o.getValue());
            jsonObject.forEach(json -> {
                json.setValue(transType(json.getValue()));
            });
            o.setValue(jsonObject);
        } else {
            o.setValue(transType(o.getValue()));
        }
    }

    private static void test(cn.hutool.json.JSONObject jsonObject){
        jsonObject.forEach(json -> {

        });
    }

    private static void t(){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        String s = "{\n" +
                "  \"penalty\": \"L2\",\n" +
                "  \"tol\": \"0.00001\",\n" +
                "  \"alpha\": \"0.01\",\n" +
                "  \"optimizer\": \"rmsprop\",\n" +
                "  \"batch_size\": \"-1\",\n" +
                "  \"learning_rate\": \"0.15\",\n" +
                "  \"max_iter\": \"10\",\n" +
                "  \"early_stop\": \"diff\",\n" +
                "  \"init_param\": {\n" +
                "    \"init_method\": \"random_uniform\"\n" +
                "  },\n" +
                "  \"cv_param\": {\n" +
                "    \"n_splits\": \"5\",\n" +
                "    \"random_seed\": \"103\",\n" +
                "    \"shuffle\": false,\n" +
                "    \"need_cv\": false\n" +
                "  }\n" +
                "}";
        jsonObject = JSONUtil.parseObj(s);
        log.info("before = {}", jsonObject);
        log.info("key = {}",jsonObject.keySet());
        jsonObject.forEach(json -> forEachJson(json));
        log.info("result = {}", jsonObject);
    }
}
