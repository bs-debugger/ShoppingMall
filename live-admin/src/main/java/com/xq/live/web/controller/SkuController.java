package com.xq.live.web.controller;

import com.xq.live.model.Sku;
import com.xq.live.model.SysUser;
import com.xq.live.model.User;
import com.xq.live.service.SkuService;
import com.xq.live.service.UserService;
import com.xq.live.utils.HtmlUtil;
import com.xq.live.utils.SessionUtils;
import com.xq.live.vo.SkuInVo;
import com.xq.live.vo.UserInVo;
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
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-12 13:39
 * @copyright:hbxq
 **/
@Controller
@RequestMapping("/sku")
public class SkuController extends BaseController{

    @Autowired
    private SkuService skuService;

    /**
     * <p>
     * 列表页面
     * <p>
     * @param inVo
     * <p>
     * @param response
     * <p>
     * @throws Exception
     * <p>
     * User: Zhang Peng
     * <p>
     * Date: 2015年11月16日
     */
    @RequestMapping("/dataList")
    @ResponseBody
    public Map<String, Object> dataList(SkuInVo inVo, HttpServletResponse response) throws Exception {
        List<Sku> dataList = skuService.queryWithPg(inVo);
        // 设置页面数据
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", inVo.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        return  jsonMap;
    }

    /**
     * 查询列表页
     * @param inVo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list(SkuInVo inVo, HttpServletRequest request) throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        return forword("sku/sku", context);
    }

    /**
     * <p>
     * 根据id查询用户信息
     * <p>
     * User: Zhang Peng
     * <p>
     * Date: 2015年11月16日
     */
    @RequestMapping("/getId")
    public void getId(Long id, HttpServletResponse response) throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        Sku sku = skuService.queryById(id);
        if (sku == null) {
            sendFailureMessage(response, "没有找到对应的记录!");
            return;
        }
        context.put(SUCCESS, true);
        context.put("data", sku);
        HtmlUtil.writerJson(response, context);
    }
    /**
     * <p> 保存用户信息
     * <p> @param inVo
     * <p> @param response
     * <p> @throws Exception
     * <p> User: Zhang Peng
     * <p> Date: 2015年11月16日
     */
    @RequestMapping("/save")
    public void save(Sku sku,HttpServletRequest request, HttpServletResponse response) throws Exception{
        SysUser sysUser = SessionUtils.getUser(request);
        if(sysUser == null){
            sendFailureMessage(response, "请重新登录");
        }
        sku.setOpreatorId(Long.valueOf(sysUser.getId()));
        sku.setOpreatorName(sysUser.getNickName());
        if(sku.getId() == null){
            skuService.add(sku);
        }else{
            skuService.update(sku);
        }
        sendSuccessMessage(response, "保存成功~");
    }
    /**
     * <p> 删除用户
     * <p> @param ids
     * <p> @param response
     * <p> @throws Exception
     * <p> User: Zhang Peng
     * <p> Date: 2015年11月16日
     */
    @RequestMapping("/delete")
    public void delete(Long[] ids,HttpServletResponse response) throws Exception{
        skuService.delete(ids);
        sendSuccessMessage(response, "删除成功");
    }
}
