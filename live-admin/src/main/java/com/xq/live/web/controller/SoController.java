package com.xq.live.web.controller;

import com.xq.live.service.SoService;
import com.xq.live.utils.HtmlUtil;
import com.xq.live.vo.SoInVo;
import com.xq.live.vo.out.SoOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户订单管理
 *
 * @author zhangpeng32
 * @date 2018-02-22 17:42
 * @copyright:hbxq
 **/
@Controller
@RequestMapping("/so")
public class SoController extends BaseController {

    @Autowired
    private SoService soService;

    /**
     * 列表页面
     *
     * @param inVo
     * @param response
     * @throws Exception User: Zhang Peng
     *                   Date: 2015年11月16日
     */
    @RequestMapping("/dataList")
    @ResponseBody
    public Map<String, Object> dataList(SoInVo inVo, HttpServletResponse response) throws Exception {
        List<SoOut> dataList = soService.queryWithPg(inVo);
        // 设置页面数据
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", inVo.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return jsonMap;
    }

    /**
     * 查询列表页
     *
     * @param inVo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list(SoInVo inVo, HttpServletRequest request) throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        return forword("so/so", context);
    }

    /**
     * 根据id查询记录
     * User: Zhang Peng
     * Date: 2018年2月16日
     */
    @RequestMapping("/getId")
    public void getId(Long id, HttpServletResponse response) throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        SoOut soOut = soService.queryById(id);
        if (soOut == null) {
            sendFailureMessage(response, "没有找到对应的记录!");
            return;
        }
        context.put(SUCCESS, true);
        context.put("data", soOut);
        HtmlUtil.writerJson(response, context);
    }
}
