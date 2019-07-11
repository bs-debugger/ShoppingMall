package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.RefundApplicationMapper;
import com.xq.live.service.RefundApplicationService;
import com.xq.live.vo.in.PayRefundApplicationInVO;
import com.xq.live.vo.out.PayRefundApplicationOut;
import com.xq.live.web.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;


@Api(tags = "退款申请-RefundApplicationController")
@RestController
@RequestMapping("manage/payRefund")
public class RefundApplicationController {

     @Autowired
     private RefundApplicationService refundApplicationService;

     @Autowired
     private RefundApplicationMapper refundApplicationMapper;
    /**
     * 获取退款申请列表
     * @param payRefundApplicationVO
     * @return
     */
    @ApiOperation("获取退款申请列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResp<Pager<PayRefundApplicationOut>> getList(@RequestBody PayRefundApplicationInVO payRefundApplicationVO){
        return new BaseResp<>(ResultStatus.SUCCESS, refundApplicationService.getList(payRefundApplicationVO));
    }

    /**
     * 退款申请头部图表
     * @param
     * @return
     */
    @ApiOperation("退款申请头部图表")
    @RequestMapping(value = "/getTableList", method = RequestMethod.POST)
    public BaseResp getTableList(){
        return refundApplicationService.getTableList();
    }

    /**
     * 批量同意申请
     * @param
     * @return
     */
    @ApiOperation("批量同意申请")
    @RequestMapping(value = "/agreeRefund", method = RequestMethod.POST)
    public BaseResp agreeRefund(@RequestBody PayRefundApplicationInVO payRefundApplicationVO) {
        return refundApplicationService.agreeRefund(payRefundApplicationVO);
    }

    /**
     *批量驳回退款申请
     * */
    @ApiOperation("批量驳回退款申请")
    @RequestMapping(value = "/refuseRefund", method = RequestMethod.POST)
    public BaseResp refuseRefund(@RequestBody PayRefundApplicationInVO payRefundApplicationVO) {
        return refundApplicationService.refuseRefund(payRefundApplicationVO);
    }

    /**
     * 导出操作
     * */
    @ApiOperation(value = "导出报表")
    @GetMapping(value = "/export")
    public void export(PayRefundApplicationInVO payRefundApplicationVO, HttpServletResponse response){
        //文件名格式化用
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format = df.format(new Date());
        payRefundApplicationVO.setRows(Integer.MAX_VALUE);
        //导出操作
        FileUtil.exportExcel(refundApplicationMapper.getList(payRefundApplicationVO),"退款申请列表","退款申请列表",PayRefundApplicationOut.class,"退款申请列表"+format+".xls",response);
    }
}
