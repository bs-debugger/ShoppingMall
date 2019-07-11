package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.SoOut;
import com.xq.live.vo.out.SoWriteOffOut;
import com.xq.live.web.utils.CutOutTimeUtils;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * ${DESCRIPTION}
 *
 * @author lipeng
 * @date 2018-02-21 18:18
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/app/hx")
public class SoWriteOffForAppController {

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private SoService soService;

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SoWriteOff> get(@PathVariable("id") Long id) {
        SoWriteOff soWriteOff = soWriteOffService.get(id);
        return new BaseResp<SoWriteOff>(ResultStatus.SUCCESS, soWriteOff);
    }

    /**
     * 注:cashierId和cashierName是当前用户的userId和userName，要从网关中取
     * @param soWriteOff
     * @param result
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid SoWriteOff soWriteOff, BindingResult result) {
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        soWriteOff.setCashierId(user.getId());
        soWriteOff.setCashierName(user.getUserName());

        //1、参数校验--验证券是否被核销过
        Coupon coupon = couponService.get(soWriteOff.getCouponId());
        if(coupon == null || coupon.getIsUsed() == Coupon.COUPON_IS_USED_YES){
            return new BaseResp<Long>(ResultStatus.error_coupon_is_used);
        }

        //验证扫码人id
        User cashier = userService.getUserById(soWriteOff.getCashierId());
        if(cashier == null){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_id);
        }

        //验证账号类型：商家账号
        if(cashier.getUserType() != User.USER_TYPE_SJ){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_user_type);
        }

        //验证商家信息
        if(cashier.getShopId() == null){
            return new BaseResp<Long>(ResultStatus.error_para_user_shop_id);
        }
        Shop shop = shopService.getShopByUserId(cashier.getId());
        if(shop ==  null){
            return new BaseResp<Long>(ResultStatus.error_shop_info_empty);
        }

        SoOut soOut = soService.get(soWriteOff.getSoId());
        //判断核销的券所属于的订单是否为食典券订单，砍价券订单，抢购券订单。
        //如果未上述的订单，则要判断订单所属的商家是否为该核销商家，如果不是则不让核销
        //但是现在有个新的判断方法，判断该票券所在shopId是否是核销的商家，如果不是则不让核销
        if(soOut!=null) {
            if (soOut.getSoType() == So.SO_TYPE_PT && soOut.getShopId() != null) {
                if (!soWriteOff.getShopId().equals(soOut.getShopId())) {
                    return new BaseResp<Long>(ResultStatus.error_act_shop_not_right);
                }
            }
        }else{
            return new BaseResp<Long>(ResultStatus.error_para_coupon_code_empty);
        }


        soWriteOff.setShopId(shop.getId());
        soWriteOff.setShopName(shop.getShopName());
        soWriteOff.setPaidAmount(soWriteOff.getShopAmount().subtract(soWriteOff.getCouponAmount()));
        //2、核销抵用券
        Long id = soWriteOffService.add(soWriteOff);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 查询每个商家核销的票卷的信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<SoWriteOffOut>> list(SoWriteOffInVo inVo){
        //为了防止线上报错，先不传inVo.getIsBill()
        if(inVo==null||inVo.getShopId()==null||inVo.getBegainTime()==null||inVo.getEndTime()==null){
            return new BaseResp<Pager<SoWriteOffOut>>(ResultStatus.error_param_empty);
        }
        Pager<SoWriteOffOut> list = soWriteOffService.list(inVo);
        return new BaseResp<Pager<SoWriteOffOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 返回时间段内各个月份的金额(shopid和时间段)
     * 注:入参shopId=35&begainTime=2018-07-01&endTime=2018-07-28
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listAmount",method = RequestMethod.GET)
    public BaseResp<Map<Integer,SoWriteOffOut>> listAmount(SoWriteOffInVo inVo){
        //获取分开好的月份
        List<SoWriteOffInVo> listInVo= CutOutTimeUtils.getValueForDate(inVo);
        if (listInVo.size()==0||listInVo==null){
            return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        Map<Integer,SoWriteOffOut> map = new HashMap<Integer,SoWriteOffOut>();

        for (int ivo=0;ivo<listInVo.size();ivo++){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(listInVo.get(ivo).getEndTime());
            calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            listInVo.get(ivo).setEndTime(calendar.getTime());//这个时间就是日期往后推一天的结果
        }

        for (int i=0;i<listInVo.size();i++){
            //可以将没有记录的月份不返回
            if (soWriteOffService.listAmount(listInVo.get(i)).get(0)!=null){
                map.put(i+1, soWriteOffService.listAmount(listInVo.get(i)).get(0));
            }
            SoWriteOffOut offOut=soWriteOffService.listAmount(listInVo.get(i)).get(0);
            //offOut.setIsBill(SoWriteOff.SO_WRITE_OFF_IS_BILL);
            listInVo.get(i).setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
            SoWriteOffOut offOutByBill=soWriteOffService.listAmount(listInVo.get(i)).get(0);
            if (offOutByBill!=null&&offOutByBill.getTotalService().compareTo(BigDecimal.ZERO)!=0){
                offOut.setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
                offOut.setTotalNoService(offOutByBill.getTotalService());
                map.put(i + 1, offOut);
            }else {
                if (offOut!=null){
                    offOut.setIsBill(SoWriteOff.SO_WRITE_OFF_IS_BILL);
                    offOut.setTotalNoService(BigDecimal.ZERO);
                }
                map.put(i + 1, offOut);
            }
        }
        if (map==null){
            return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.SUCCESS,map);
    }

    /**
     * 返回时间段内各个月份的金额(shopid和时间段)带查询时间的接口
     * 注:入参shopId=35&begainTime=2018-07-01&endTime=2018-07-28
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listAmountForM",method = RequestMethod.GET)
    public BaseResp<List<SoWriteOffOut>> listAmountForM(SoWriteOffInVo inVo){
        //获取分开好的月份
        List<SoWriteOffInVo> listInVo= CutOutTimeUtils.getValueForDate(inVo);
        if (listInVo.size()==0||listInVo==null){
            return new BaseResp<List<SoWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        List<SoWriteOffOut> outList=new ArrayList<SoWriteOffOut>();

        for (int ivo=0;ivo<listInVo.size();ivo++){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(listInVo.get(ivo).getEndTime());
            calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            listInVo.get(ivo).setEndTime(calendar.getTime());//这个时间就是日期往后推一天的结果
        }

        for (int i=0;i<listInVo.size();i++){
            //可以将没有记录的月份不返回
            if (soWriteOffService.listAmount(listInVo.get(i)).get(0)!=null){
                SoWriteOffOut newSo=soWriteOffService.listAmount(listInVo.get(i)).get(0);
/*                newSo.setStartDoTime(listInVo.get(i).getBegainTime());
                newSo.setEndDoTime(listInVo.get(i).getEndTime());*/
                outList.add(i,newSo);
            }
            SoWriteOffOut offOut=soWriteOffService.listAmount(listInVo.get(i)).get(0);
            listInVo.get(i).setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
            SoWriteOffOut offOutByBill=soWriteOffService.listAmount(listInVo.get(i)).get(0);
            if (offOutByBill!=null&&offOutByBill.getTotalService().compareTo(BigDecimal.ZERO)!=0){
                offOut.setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
                offOut.setTotalNoService(offOutByBill.getTotalService());
                offOut.setStartDoTime(listInVo.get(i).getBegainTime());
                offOut.setEndDoTime(listInVo.get(i).getEndTime());
                outList.set(i, offOut);
            }else {
                if (offOut!=null){
                    offOut.setIsBill(SoWriteOff.SO_WRITE_OFF_IS_BILL);
                    offOut.setTotalNoService(BigDecimal.ZERO);
                    offOut.setStartDoTime(listInVo.get(i).getBegainTime());
                    offOut.setEndDoTime(listInVo.get(i).getEndTime());
                }
                outList.set(i,offOut);
            }
        }
        if (outList.size()<1||outList==null){
            return new BaseResp<List<SoWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        return new BaseResp<List<SoWriteOffOut>>(ResultStatus.SUCCESS,outList);
    }


    /**
     * 查询某个时间段的服务费信息
     * 注:入参shopId=35&begainTime=2018-07-01&endTime=2018-07-28
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/amount",method = RequestMethod.GET)
    public BaseResp<Map<Integer,SoWriteOffOut>> amount(SoWriteOffInVo inVo){
        Map<Integer,SoWriteOffOut> map = new HashMap<Integer,SoWriteOffOut>();
            SoWriteOffOut offOut=soWriteOffService.listAmount(inVo).get(0);
        inVo.setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
        SoWriteOffOut offOutByBill=soWriteOffService.listAmount(inVo).get(0);
            if (offOutByBill!=null&&offOutByBill.getTotalService().compareTo(BigDecimal.ZERO)!=0){
                offOut.setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
                offOut.setTotalNoService(offOutByBill.getTotalService());
                map.put(1, offOut);
            }else {
                if (offOut!=null){
                    offOut.setIsBill(SoWriteOff.SO_WRITE_OFF_IS_BILL);
                    offOut.setTotalNoService(BigDecimal.ZERO);
                }
                map.put(1, offOut);
            }
        if (map==null){
            return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        return new BaseResp<Map<Integer,SoWriteOffOut>>(ResultStatus.SUCCESS,map);
    }


    /**
     * 查询一段时间内核销的券类别
     * @param inVo
     * @return
     * app/hx/sotype?shopId=35&begainTime=2018-06-01&endTime=2018-08-01
     * 商家id
     */
    @RequestMapping(value = "/sotype",method = RequestMethod.GET)
    public BaseResp<List<SoWriteOffOut>> sotype(SoWriteOffInVo inVo){
        if(inVo==null||inVo.getShopId()==null||inVo.getBegainTime()==null||inVo.getEndTime()==null){
            return new BaseResp<List<SoWriteOffOut>>(ResultStatus.error_param_empty);
        }
        List<SoWriteOffOut> list = soWriteOffService.sotype(inVo);
        return new BaseResp<List<SoWriteOffOut>>(ResultStatus.SUCCESS,list);
    }


}
