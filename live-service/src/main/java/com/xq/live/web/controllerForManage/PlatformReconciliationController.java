package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.PlatformReconciliationMapper;
import com.xq.live.service.PlatformReconciliationService;
import com.xq.live.vo.in.PlatformReconciliationInVo;
import com.xq.live.vo.out.PlatformReconciliationOut;
import com.xq.live.web.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api(tags = "平台对账-PlatformReconciliationController")
@RestController
@RequestMapping("manage/Platform")
public class PlatformReconciliationController {

    @Autowired
    private PlatformReconciliationService platformReconciliationService;

    @Autowired
    private PlatformReconciliationMapper platformReconciliationMapper;

    /**
     * 平台对账列表
     * */
    @ApiOperation(value = "获取平台对账列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResp<Pager<PlatformReconciliationOut>> getList(@RequestBody PlatformReconciliationInVo platformReconciliationInVo){
        return new BaseResp<>(ResultStatus.SUCCESS, platformReconciliationService.getList(platformReconciliationInVo));
    }

    /**
     * 平台对账导出
     * */
    @ApiOperation(value = "导出报表")
    @GetMapping(value = "/export")
    public void export(PlatformReconciliationInVo platformReconciliationInVo, HttpServletResponse response){
        //文件名格式化用
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format = df.format(new Date());
        platformReconciliationInVo.setRows(Integer.MAX_VALUE);
        //导出操作
        FileUtil.exportExcel(platformReconciliationMapper.getList(platformReconciliationInVo),"平台对账列表","平台对账列表", PlatformReconciliationOut.class,"平台对账列表"+format+".xls",response);
    }
}
