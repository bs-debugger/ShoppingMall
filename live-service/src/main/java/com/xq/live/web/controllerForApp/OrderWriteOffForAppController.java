package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.OrderWriteOff;
import com.xq.live.service.OrderWriteOffService;
import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.out.OrderWriteOffOut;
import com.xq.live.web.utils.CutOutTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 商城系统票券核销相关接口
 * Created by lipeng on 2018/12/3.
 */
@RestController
@RequestMapping(value = "/app/orderWriteOff")
public class OrderWriteOffForAppController {
    @Autowired
    private OrderWriteOffService orderWriteOffService;

    /**
     * 分页查询票券核销列表
     * page,rows,shopId,beginTime,endTime
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<OrderWriteOffOut>> list(OrderWriteOffInVo inVo){
        Pager<OrderWriteOffOut> list = orderWriteOffService.list(inVo);
        return new BaseResp<Pager<OrderWriteOffOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 查询票券核销详情
     * id
     * @param id
     * @return
     */
    @RequestMapping(value = "/getDetail",method = RequestMethod.GET)
    public BaseResp<OrderWriteOffOut> getDetail(Long id){
        OrderWriteOffOut re = orderWriteOffService.getDetail(id);
        return new BaseResp<OrderWriteOffOut>(ResultStatus.SUCCESS,re);
    }

    /**
     * 通过票券编号查询票券核销详情
     * orderCouponCode
     * @param orderCouponCode
     * @return
     */
    @RequestMapping(value = "/getDetailByOrderCouponCode",method = RequestMethod.GET)
    public BaseResp<OrderWriteOffOut> getDetailByOrderCouponCode(String  orderCouponCode){
        OrderWriteOffOut re = orderWriteOffService.getDetailByOrderCouponCode(orderCouponCode);
        return new BaseResp<OrderWriteOffOut>(ResultStatus.SUCCESS,re);
    }

    /**
     * 查询某个时间段的服务费信息
     * 注:入参shopId=35&begainTime=2018-07-01&endTime=2018-07-28
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/amount",method = RequestMethod.GET)
    public BaseResp<Map<Integer,OrderWriteOffOut>> amount(OrderWriteOffInVo inVo){
        Map<Integer,OrderWriteOffOut> map = new HashMap<Integer,OrderWriteOffOut>();
        List<OrderWriteOffOut> orderWriteOffOutList= orderWriteOffService.listAmount(inVo);
        if(orderWriteOffOutList!=null){
            OrderWriteOffOut orderWriteOffOut=orderWriteOffOutList.get(0);
            inVo.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_NO_BILL);
            OrderWriteOffOut orderWriteOffOutNoBill=orderWriteOffService.listAmount(inVo).get(0);
            if(orderWriteOffOutNoBill!=null&&orderWriteOffOutNoBill.getTotalService().compareTo(BigDecimal.ZERO)!=0){
                orderWriteOffOut.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_NO_BILL);
                orderWriteOffOut.setTotalNoService(orderWriteOffOutNoBill.getTotalService());
                map.put(1, orderWriteOffOut);
            }else{
                if(orderWriteOffOut!=null){
                    orderWriteOffOut.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_IS_BILL);
                    orderWriteOffOut.setTotalNoService(BigDecimal.ZERO);
                    map.put(1, orderWriteOffOut);
                }
            }
        }
        if (map==null){
            return new BaseResp<Map<Integer,OrderWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        return new BaseResp<Map<Integer,OrderWriteOffOut>>(ResultStatus.SUCCESS,map);
    }

    /**
     * 分页查询票券核销列表
     * page,rows,shopId,beginTime,endTime
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForShop",method = RequestMethod.GET)
    public BaseResp<Pager<OrderWriteOffOut>> listForShop(OrderWriteOffInVo inVo){
        if(inVo==null||inVo.getShopId()==null||inVo.getBeginTime()==null||inVo.getEndTime()==null){
            return new BaseResp<Pager<OrderWriteOffOut>>(ResultStatus.error_param_empty);
        }
        Pager<OrderWriteOffOut> list = orderWriteOffService.listForShop(inVo);
        return new BaseResp<Pager<OrderWriteOffOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 返回时间段内各个月份的金额(shopid和时间段)带查询时间的接口
     * 注:入参shopId=35&begainTime=2018-07-01&endTime=2018-07-28
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listAmountForM",method = RequestMethod.GET)
    public BaseResp<List<OrderWriteOffOut>> listAmountForM(OrderWriteOffInVo inVo){
        //获取分开好的月份
        List<OrderWriteOffInVo> listInVo= CutOutTimeUtils.getValueForDateNew(inVo);
        if (listInVo.size()==0||listInVo==null){
            return new BaseResp<List<OrderWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        List<OrderWriteOffOut> outList=new ArrayList<OrderWriteOffOut>();

        for (int ivo=0;ivo<listInVo.size();ivo++){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(listInVo.get(ivo).getEndTime());
            calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            listInVo.get(ivo).setEndTime(calendar.getTime());//这个时间就是日期往后推一天的结果
        }

        for (int i=0;i<listInVo.size();i++){
            //可以将没有记录的月份不返回
            OrderWriteOffOut offOut=orderWriteOffService.listAmount(listInVo.get(i)).get(0);

            listInVo.get(i).setIsBill( OrderWriteOff.ORDER_WRITE_OFF_NO_BILL);
            OrderWriteOffOut offOutByBill=orderWriteOffService.listAmount(listInVo.get(i)).get(0);
            if (offOutByBill!=null&&offOutByBill.getTotalService().compareTo(BigDecimal.ZERO)!=0){
                if (offOut==null){
                    offOut=new OrderWriteOffOut();
                }
                offOut.setIsBill( OrderWriteOff.ORDER_WRITE_OFF_NO_BILL);
                offOut.setTotalNoService(offOutByBill.getTotalService());
                offOut.setStartDoTime(listInVo.get(i).getBeginTime());
                offOut.setEndDoTime(listInVo.get(i).getEndTime());
                outList.add(i, offOut);
            }else {
                if (offOut==null){
                    offOut=new OrderWriteOffOut();
                }
                offOut.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_IS_BILL);
                offOut.setTotalNoService(BigDecimal.ZERO);
                offOut.setStartDoTime(listInVo.get(i).getBeginTime());
                offOut.setEndDoTime(listInVo.get(i).getEndTime());
                outList.add(i,offOut);
            }
        }
        if (outList.size()<1||outList==null){
            return new BaseResp<List<OrderWriteOffOut>>(ResultStatus.error_sowriteoff_amount);
        }
        return new BaseResp<List<OrderWriteOffOut>>(ResultStatus.SUCCESS,outList);
    }
}
