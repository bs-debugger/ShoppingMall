package com.xq.live.web.controller;

import com.xq.live.model.Shop;
import com.xq.live.model.Sku;
import com.xq.live.model.SysUser;
import com.xq.live.service.ShopService;
import com.xq.live.service.SkuService;
import com.xq.live.utils.HtmlUtil;
import com.xq.live.utils.SessionUtils;
import com.xq.live.vo.ShopInVo;
import com.xq.live.vo.SkuInVo;
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
 * @date 2018-02-13 17:03
 * @copyright:hbxq
 **/
@Controller
@RequestMapping("/shop")
public class ShopController extends BaseController {

    @Autowired
    private ShopService shopService;

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
    public Map<String, Object> dataList(ShopInVo inVo, HttpServletResponse response) throws Exception {
        List<Shop> dataList = shopService.queryWithPg(inVo);
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
    public ModelAndView list(ShopInVo inVo, HttpServletRequest request) throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        return forword("shop/shop", context);
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
        Shop shop = shopService.queryById(id);
        if (shop == null) {
            sendFailureMessage(response, "没有找到对应的记录!");
            return;
        }
        context.put(SUCCESS, true);
        context.put("data", shop);
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
    public void save(Shop shop,HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(shop.getId() == null){
            shopService.add(shop);
        }else{
            shopService.update(shop);
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
        shopService.delete(ids);
        sendSuccessMessage(response, "删除成功");
    }
}
