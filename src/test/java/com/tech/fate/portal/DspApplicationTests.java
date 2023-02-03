package com.tech.fate.portal;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tech.fate.portal.common.FmlManagerResp;
import com.tech.fate.portal.dto.ComponentsParamsDto;
import com.tech.fate.portal.dto.ProjectParticipantsDto;
import com.tech.fate.portal.mapper.ComponentsMapper;
import com.tech.fate.portal.service.ProjectService;
import com.tech.fate.portal.util.HttpUtils;
import com.tech.fate.portal.vo.ComponentsParams;
import com.tech.fate.portal.vo.ProjectAssociateDataVo;
import com.tech.fate.portal.vo.ProjectParticipantAndDataVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Slf4j
class DspApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
//        String sign = JwtUtil.sign("liuyuliang", "12345");
//        log.info("token = {}", sign);
//        String username = JwtUtil.getUsername(sign);
//        log.info("username = {}", username);
        String json = ResourceUtil.readUtf8Str("data/menuList.json");
        log.info("menu = {}", json);
    }

//    @Autowired
//    private ComponentsMapper componentsMapper;
//    @Test
//    public void tokenTest() {
////        String json = ResourceUtil.readUtf8Str("data/menuList.json");
////        log.info("menu = {}", json);
//        ComponentsParamsDto componentsDto = new ComponentsParamsDto();
//        componentsDto.setNodeId("dataio");
//        componentsDto.setStatus(1);
//        List<ComponentsParams> componentsParamsDtoList = componentsMapper.queryAlgorithmComponentsParams(componentsDto);
//        log.info("原始数据 = {}", JSONUtil.toJsonStr(componentsParamsDtoList));
//        List<ComponentsParams> result = treeC(componentsParamsDtoList);
//        log.info("递归后的数据 = {}",JSONUtil.toJsonStr(result));
//    }

//    public static <T> List<T> treeC(List<T> list) {
//
//        if (null == list) {
//            list = new ArrayList<>();
//        }
//
//        HashMap<String, T> map = new HashMap<>();
//
//        ArrayList<T> treeList = new ArrayList<>();
//
//        for (T treeType : list) {
//            ComponentsParams type = (ComponentsParams) treeType;
//            map.put(type.getUuid(), treeType);
//        }
//
//        for (T treeType : list) {
//            ComponentsParams type = (ComponentsParams) treeType;
//
//            ComponentsParams parent = (ComponentsParams) map.get(type.getParentId());
//
//            if (null != parent) {
//                List<ComponentsParams> children = parent.getChildParamValue();
//                if (null != children) {
//                    children.add(type);
//                } else {
//                    children = new ArrayList<>();
//                    children.add(type);
//                    parent.setChildParamValue(children);
//                }
////                parent.setHasChildren(true);
//            } else {
//                treeList.add(treeType);
//            }
//        }
//
//        return treeList;
//    }

    @Autowired
    private ProjectService projectService;

    @Test
    public void getProjectParticipantList() throws Exception {
//        String url = "http://192.168.0.3:8180/api/v1/project/6c8c315d4a2042408037fbc36fb7c64a/participant";
//        String result = HttpUtils.get(url);
//        FmlManagerResp fmlManagerResp = JSONUtil.toBean(result, FmlManagerResp.class);
//        JSONObject data = (JSONObject) fmlManagerResp.getData();
//        data.forEach(d -> log.info("d===={}", d.getValue()));
//        data.forEach(d -> log.info("j===={}", com.alibaba.fastjson.JSONObject.parseObject(d.getValue().toString(), ProjectParticipantsDto.class)));
//        List<ProjectParticipantsDto> projectParticipantsDtoList = projectService.queryProjectParticipantList("6c8c315d4a2042408037fbc36fb7c64a");
//        log.info("projectParticipantsDtoList====={}",projectParticipantsDtoList);
        List<ProjectAssociateDataVo> projectDataList = projectService.projectAssociateDataList("6c8c315d4a2042408037fbc36fb7c64a");
        log.info("projectDataList====={}", projectDataList);
    }

    @Test
    public void getProjectParticipantAndDataList() throws Exception {
        List<ProjectParticipantAndDataVo> projectParticipantAndDataVos = projectService.getProjectParticipantAndDataList("6c8c315d4a2042408037fbc36fb7c64a");
        log.info("project = {}", projectParticipantAndDataVos);
    }

}
