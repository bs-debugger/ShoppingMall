package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ExcelUtils;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.AccountLog;
import com.xq.live.model.ShopAccount;
import com.xq.live.model.ShopAccountDetail;
import com.xq.live.service.ShopAccountService;
import com.xq.live.vo.in.IdInVo;
import com.xq.live.vo.in.ShopAccountInVo;
import com.xq.live.vo.out.ShopAccountOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Api(tags = "商户对账-ShopAccountController")
@RestController
@RequestMapping("/manage/shopAccount")
public class ShopAccountController {

    @Resource
    ShopAccountService shopAccountService;

    @ApiOperation(value = "商户对账统计")
    @PostMapping(value = "/findShopAccountTotal")
    public BaseResp<ShopAccount> findShopAccountTotal(@RequestBody ShopAccountInVo inVo){
        ShopAccount shopAccount = shopAccountService.findShopAccountTotal(inVo);
        return new BaseResp<ShopAccount>(ResultStatus.SUCCESS, shopAccount);
    }

    @ApiOperation(value = "提现记录")
    @PostMapping(value = "/searchShopWithdrawList")
    public BaseResp<Pager<ShopAccountOut>> searchShopWithdrawList(@RequestBody ShopAccountInVo inVo) {
        Pager<ShopAccountOut> shopAccount = shopAccountService.searchShopWithdrawList(inVo);
        return new BaseResp<Pager<ShopAccountOut>>(ResultStatus.SUCCESS, shopAccount);
    }

    @ApiOperation(value = "审批通过")
    @PostMapping(value = "/approvePass")
    public BaseResp approvePass(@RequestBody IdInVo idInVo) {
        return shopAccountService.approvePass(idInVo);
    }


    @ApiOperation(value = "审批驳回")
    @PostMapping(value = "/approveReject")
    public BaseResp approveReject(@RequestBody ShopAccountInVo inVo) {
        return shopAccountService.approveReject(inVo);
    }

    @ApiOperation(value = "店铺核销明细")
    @PostMapping(value = "/findShopAccountDetailById")
    public BaseResp<Pager<ShopAccountDetail>> findShopAccountDetailById(@RequestBody ShopAccountInVo inVo){
        return new BaseResp<Pager<ShopAccountDetail>>(ResultStatus.SUCCESS, shopAccountService.findShopAccountDetailById(inVo));
    }

    @ApiOperation(value = "店铺金额变更操作日志")
    @PostMapping(value = "/findAccountLog")
    public BaseResp<Pager<AccountLog>> findAccountLog(@RequestBody ShopAccountInVo inVo) {
        return new BaseResp<Pager<AccountLog>>(ResultStatus.SUCCESS, shopAccountService.findAccountLog(inVo));
    }

    @ApiOperation(value = "刷新商户对账统计")
    @PostMapping(value = "/refreshShopAccountTotal")
    public BaseResp<ShopAccount> refreshShopAccountTotal(@RequestBody ShopAccountInVo inVo){
        ShopAccount shopAccount = shopAccountService.refreshShopAccountTotal(inVo);
        return new BaseResp<ShopAccount>(ResultStatus.SUCCESS, shopAccount);
    }

    @ApiOperation(value = "导出报表")
    @GetMapping(value = "/export")
    public void export(ShopAccountInVo inVo, HttpServletResponse response, HttpServletRequest request) throws InterruptedException {
        //size默认值
        inVo.setPage(1);
        inVo.setRows(500000);
        //获取数据
        Pager<ShopAccountOut> outPager = shopAccountService.searchShopWithdrawList(inVo);
        final List<ShopAccountOut>  outs= outPager.getList();
        //excel标题
        final String[] title = {"提现编号", "商家名称", "提现银行卡", "所属银行", "电话号码","发起提现时间",
                "持卡人","提现金额","审批状态"};

        //sheet名
        String sheetName = "商户对账记录";

        //context上下文
        final String [][]content=new String[outs.size()][];
        ExecutorService executorService = Executors.newScheduledThreadPool(10);
        for (int i = 0; i < outs.size(); i++) {
            final int finalI = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ShopAccountOut out = outs.get(finalI);
                    content[finalI] = new String[title.length];
                    content[finalI][0] = ExcelUtils.dataFromat(out.getCashId().toString());
                    content[finalI][1] = ExcelUtils.dataFromat(out.getShopName());
                    content[finalI][2] = ExcelUtils.dataFromat(out.getAccountName());
                    content[finalI][3] = ExcelUtils.dataFromat(out.getBankCardName());
                    content[finalI][4] = ExcelUtils.dataFromat(out.getMobile());
                    content[finalI][5] = ExcelUtils.dataFromat(out.getBeginTime());
                    content[finalI][6] = ExcelUtils.dataFromat(out.getAccountCardholderName());
                    content[finalI][7] = ExcelUtils.dataFromat(out.getCashAmount().toString());
                    content[finalI][8] = ExcelUtils.dataFromat(getApplyStatus(out.getApplyStatus()));
                }
            });

        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, title, content, null);
        //响应到客户端
        try {
            //文件名称
            String filename = "商户对账记录"+getNowPlusTimeMill()+".xls";
            //获取User-agent 当前是哪个浏览器
            String userAgent = request.getHeader("User-Agent");
            // 这里使用到FileUtils工具类进行编码
            String encodeFilename = ExcelUtils.encodeDownloadFilename(filename, userAgent);
            response.setHeader("content-disposition", "filename="+encodeFilename);
            //文件后缀
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //输出流对象
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析审批状态
     * @param status
     * @return
     */
    private static String getApplyStatus(Integer status){
        if(status==1){
            return "待审批";
        }else if(status==2){
            return "审批通过";
        }else if(status==3){
            return "审批不通过";
        }else{
            return "";
        }
    }

    /**
     * 当前时间
     * @return
     * @throws Exception
     */
    public static String getNowPlusTimeMill() throws Exception {
        java.text.SimpleDateFormat sdfLongTimePlusMill = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String nowDate = "";
        try {
            java.sql.Date date = null;
            date = new java.sql.Date(new java.util.Date().getTime());
            nowDate = sdfLongTimePlusMill.format(date);
            return nowDate;
        }
        catch (Exception e) {
            throw e;
        }
    }
}
