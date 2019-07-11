package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.DataDicService;
import com.xq.live.vo.in.DataDicInvo;
import com.xq.live.vo.out.DataDicOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lipeng on 2018/4/4.
 */
@RestController
@RequestMapping(value = "/app/dataDic")
public class DataDicForAppController {

    @Autowired
    private DataDicService dataDicService;

    /**
     * 更具type分页查询数据字典
     * @param invo
     * @return
     */
    @RequestMapping(value = "/listForType",method = RequestMethod.GET)
    public BaseResp<Pager<DataDicOut>> listForType(DataDicInvo invo){
        if(invo==null||invo.getType()==null){
            return new BaseResp<Pager<DataDicOut>>(ResultStatus.error_param_empty);
        }

        Pager<DataDicOut> dataDicOutPager = dataDicService.listForType(invo);
        return new BaseResp<Pager<DataDicOut>>(ResultStatus.SUCCESS,dataDicOutPager);
    }

    /**
     * 插入一条数据字典
     * @param invo
     * @param result
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid DataDicInvo invo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        Long add = dataDicService.add(invo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,add);
    }

    /**
     * 逻辑删除一条数据
     * @param invo
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResp<Long> delete(DataDicInvo invo){
        if(invo==null||invo.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        Long delete = dataDicService.delete(invo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,delete);
    }

    /**
     * 根据type和code查询数据
     * @param invo
     * @return
     */
    @RequestMapping(value = "/selectByTypeAndCode",method = RequestMethod.GET)
    public BaseResp<DataDicOut> selectByTypeAndCode(DataDicInvo invo){
        if(invo==null||invo.getType()==null||invo.getCode()==null){
            return new BaseResp<DataDicOut>(ResultStatus.error_param_empty);
        }

        DataDicOut dataDicOut = dataDicService.selectByTypeAndCode(invo);
        return new BaseResp<DataDicOut>(ResultStatus.SUCCESS,dataDicOut);
    }
}
