package com.xq.live.web.controllerForClientApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Shop;
import com.xq.live.model.User;
import com.xq.live.service.CountService;
import com.xq.live.service.ShopService;
import com.xq.live.service.UserService;
import com.xq.live.service.impl.ShopServiceImpl;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.ShopForSubOut;
import com.xq.live.vo.out.ShopOut;
import com.xq.live.vo.out.SubbranchOut;
import com.xq.live.web.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家controller
 *
 * @author lipeng
 * @create 2018-01-17 18:01
 **/
@RestController
@RequestMapping(value = "/clientApp/shop")
public class ShopForClientAppController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserService userService;

    @Autowired
    private CountService countService;

    /**
     * 根据商家id查询商家信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<ShopOut> getShopById(@PathVariable(value = "id") Long id) {
        Integer pops = countService.shopPops(id);
        ShopOut result = shopService.findShopOutById(id);
        result.setPopNum(pops);
        return new BaseResp<ShopOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 进入商家详细页
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public BaseResp<Shop> detail(@Valid ShopInVo inVo, BindingResult result, HttpServletRequest request){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Shop>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        inVo.setUserIp(IpUtils.getIpAddr(request));
        Shop shop = shopService.detail(inVo);
        return new BaseResp<Shop>(ResultStatus.SUCCESS, shop);
    }


    /**
     * 删除一条商家记录
     *
     * @param id
     * @return
     */
/*    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public BaseResp<Integer> delete(@PathVariable(value = "id") Long id) {
        int result = shopService.deleteShopById(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }*/

    /**
     * 更新一条商家记录
     *
     * @param shop
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(Shop shop) {
        if(shop==null||shop.getId()==null||(shop.getMobile()==null&&shop.getPhone()==null)|| shop.getAddress()==null||shop.getLocationX()==null||shop.getLocationY()==null){
             return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        int result = shopService.updateShop(shop);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据条件查询商家列表信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<ShopOut>> list(ShopInVo inVo, HttpServletRequest request){
        Pager<ShopOut> result = shopService.list(inVo);
        return new BaseResp<Pager<ShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据条件查询首页各类热门餐厅
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForHomePage", method = RequestMethod.GET)
    public BaseResp<Map<String, List<ShopOut>>> listForHomePage(ShopInVo inVo, HttpServletRequest request){
        Map<String, List<ShopOut>> result = shopService.listForHomePage(inVo);
        return new BaseResp<Map<String, List<ShopOut>>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据条件查询商家列表信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForChuangXiang", method = RequestMethod.GET)
    public BaseResp<Pager<ShopOut>> listForChuangXiang(ShopInVo inVo, HttpServletRequest request){
        Pager<ShopOut> result = shopService.listForChuangXiang(inVo);
        return new BaseResp<Pager<ShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 排序查热门商家——后续优化
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<ShopOut>> top(ShopInVo inVo){
        List<ShopOut> result = shopService.top(inVo);
        return new BaseResp<List<ShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据用户id（userId）查询商家信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getByUserId/{userId}", method = RequestMethod.GET)
    public BaseResp<Shop> getShopByUserId(@PathVariable(value = "userId") Long userId) {
        Shop result = shopService.getShopByUserId(userId);
        return new BaseResp<Shop>(ResultStatus.SUCCESS, result);
    }


    /**
     * 生成商家二维码图(包括商家id和商家code)这是跳转商家订单页,商家详情页(一共4张图)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/CreateCode")
    public BaseResp<Map<String,String>> freeOrderBySo(ShopInVo inVo){
        if (inVo==null||inVo.getId()==null||inVo.getShopCode()==null) {
            return new BaseResp<Map<String,String>>(ResultStatus.error_param_empty);
        }
        Long id=inVo.getId();
        ShopOut out=shopService.findShopOutById(id);
        if (out==null){
            return new BaseResp<Map<String,String>>(ResultStatus.error_shop_info_empty);
        }
        out.setShopCode(inVo.getShopCode());
        //ShopServiceImpl codeUrl=new ShopServiceImpl();
        String shopSoImge=shopService.uploadQRCodeToCosBySo(out);
        String shopSo= shopService.uploadQRCodeToBySo(out);
        String shopInfoImge=shopService.uploadQRCodeToCosByInfo(out);
        String shopInfo=shopService.uploadQRCodeToByInfo(out);
        if (shopSoImge==null||shopSo==null||shopInfoImge==null||shopInfo==null){
            return new BaseResp<Map<String,String>>(ResultStatus.error_shop_code);
        }
        Map<String,String> shopUrl=new HashMap<>();
        shopUrl.put("商家买单有背景图",shopSoImge);
        shopUrl.put("商家买单无背景图",shopSo);
        shopUrl.put("商家详情有背景图",shopInfoImge);
        shopUrl.put("商家详情无背景图",shopInfo);
        return new BaseResp<Map<String,String>>(ResultStatus.SUCCESS,shopUrl);
    }

    /**
     * 通过Code查询一条商家记录
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/getByCode", method = RequestMethod.GET)
    public BaseResp<ShopOut> getShopById(String code) {
        ShopOut result = shopService.getShopByCode(code);
        if (result==null){
            return new BaseResp<ShopOut>(ResultStatus.getError_allocation_selectList);
        }
        return new BaseResp<ShopOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 通过shopid和时间查询所有分店数据
     * @param inVo
     * @return
     * app/shop/getByShopId?id=35&beginTime=2018-06-01&endTime=2018-08-01
     */
    @RequestMapping(value = "/getByShopId", method = RequestMethod.GET)
    public BaseResp<List<ShopForSubOut>> getShopById(ShopInVo inVo) {
        List<ShopForSubOut> subOut = shopService.listSubByShopId(inVo);
        if (subOut==null||subOut.size()<1){
            return new BaseResp<List<ShopForSubOut>>(ResultStatus.getError_allocation_selectList);
        }
        return new BaseResp<List<ShopForSubOut>>(ResultStatus.SUCCESS, subOut);
    }

    /**
     * 通过shopid和时间查询所有分店数据(新)
     * @param inVo
     * @return
     * app/shop/subbranchList?id=35&beginTime=2018-06-01&endTime=2018-08-01
     */
    @RequestMapping(value = "/subbranchList", method = RequestMethod.GET)
    public BaseResp<List<SubbranchOut>> subbranchList(ShopInVo inVo) {
        List<SubbranchOut> subOut = shopService.subbranchList(inVo);
        if (subOut==null||subOut.size()<1){
            return new BaseResp<List<SubbranchOut>>(ResultStatus.getError_allocation_selectList);
        }
        return new BaseResp<List<SubbranchOut>>(ResultStatus.SUCCESS, subOut);
    }



}
