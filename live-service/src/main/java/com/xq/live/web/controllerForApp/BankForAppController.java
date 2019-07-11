package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.model.UserBankInfo;
import com.xq.live.service.BankService;
import com.xq.live.vo.in.BankInVo;
import com.xq.live.web.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/1613:27
 */
@RestController
@RequestMapping("/app/bank")
public class BankForAppController {

    @Autowired
    private BankService bankService;

    @RequestMapping(value = "/queryListByOwnerIdAndType",method = RequestMethod.POST)
    public BaseResp<List<UserBankInfo>> queryListByOwnerIdAndType(Integer type){
        if(type==0){
            return new BaseResp<List<UserBankInfo>>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<UserBankInfo>>(ResultStatus.error_param_empty);
        }
        List<UserBankInfo> list = bankService.queryListByOwnerIdAndType(user.getId(),type);
        return new BaseResp<>(ResultStatus.SUCCESS,list);
    }


    @RequestMapping(value = "/bindBankCard",method = RequestMethod.POST)
    public BaseResp<Object> bindBankCard(BankInVo bankInVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null||chenkbindBankCardParam(bankInVo)){
            return new BaseResp<Object>(ResultStatus.error_param_empty);
        }
        bankInVo.setUserId(user.getId());
        Map<String,Object> map = bankService.bindBankCard(bankInVo);
        if(new Integer(map.get("code").toString())==1){
            return new BaseResp<>(ResultStatus.SUCCESS);
        }else if(new Integer(map.get("code").toString())==-2){
            return new BaseResp<>(ResultStatus.error_user_security_code);
        }else if(new Integer(map.get("code").toString())==-3){
            return new BaseResp<>(ResultStatus.error_user_security_EXPIRE);
        }else{
            return new BaseResp<>(ResultStatus.FAIL,map.get("data"));
        }
    }

    /**
     * 校验参数
     * @param bankInVo
     * @return
     */
    private boolean chenkbindBankCardParam(BankInVo bankInVo){
        Integer step = bankInVo.getStep();
        if(step==1){
            String accountNo = bankInVo.getAccountNo();
            String name = bankInVo.getName();
            String mobile = bankInVo.getMobile();
            String idCard = bankInVo.getIdCard();
            Integer bindType = bankInVo.getBindType();
            if(StringUtils.isBlank(accountNo)||StringUtils.isBlank(name)||StringUtils.isBlank(mobile)
                ||StringUtils.isBlank(idCard)){
                return true;
            }
            if(bindType==0){
                return true;
            }
        }else if(step==2){
            String mobile = bankInVo.getMobile();
            String code = bankInVo.getCode();
            if(StringUtils.isBlank(mobile)||StringUtils.isBlank(code)){
                return true;
            }
        }else if(step==3){
            String accountNo = bankInVo.getAccountNo();
            String name = bankInVo.getName();
            String mobile = bankInVo.getMobile();
            String idCard = bankInVo.getIdCard();
            String securityCode = bankInVo.getSecurityCode();
            if(StringUtils.isBlank(accountNo)||StringUtils.isBlank(name)||StringUtils.isBlank(mobile)
                    ||StringUtils.isBlank(idCard)||StringUtils.isBlank(securityCode)){
                return true;
            }
        }else{
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/unbindBankCard",method = RequestMethod.POST)
    public BaseResp<Object> unbindBankCard(BankInVo bankInVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Object>(ResultStatus.error_param_empty);
        }
        bankInVo.setUserId(user.getId());
        Integer ret = bankService.unbindBankCard(bankInVo);
        if(ret==1){
            return new BaseResp<>(ResultStatus.SUCCESS);
        }else{
            return new BaseResp<>(ResultStatus.FAIL);
        }
    }

}
