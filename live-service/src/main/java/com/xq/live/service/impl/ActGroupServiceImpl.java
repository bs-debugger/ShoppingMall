package com.xq.live.service.impl;

import com.xq.live.dao.ActGroupMapper;
import com.xq.live.dao.ActShopMapper;
import com.xq.live.model.ActGroup;
import com.xq.live.service.ActGroupService;
import com.xq.live.vo.in.ActGroupInVo;
import com.xq.live.vo.out.ActGroupOut;
import com.xq.live.vo.out.ActShopOut;
import com.xq.live.vo.out.ActUserOut;
import com.xq.live.web.utils.GroupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/5/2.
 */
@Service
public class ActGroupServiceImpl implements ActGroupService{

    @Autowired
    private ActGroupMapper actGroupMapper;

    //根据活动ID查询全部参加活动小组信息
    @Override
    public List<ActGroupOut> top(ActGroupInVo inVo) {
        List<ActGroupOut> list = actGroupMapper.selectOutAll(inVo);
        return list;
    }

    //根据主键ID或者活动ID查询参加活动小组信息
    @Override
    public List<ActGroupOut> listForID(ActGroupInVo inVo) {
        List<ActGroupOut> list = actGroupMapper.selectByOutID(inVo);
        return list;
    }

    //添加参加活动小组信息
    @Override
    public Long add(ActGroup actGroup) {
        if(actGroup==null||actGroup.getActId()==null){
            return null;
        }
        int i =actGroupMapper.insert(actGroup);
        if (i>0){
            return actGroup.getId();
        }
        return null;
    }

    //批量添加分组信息
    @Override
    public int addListGroup(List<ActGroup> list) {
        for (int l=0;l<list.size();l++){
            //int index = actSkuMapper.countByActId(record.getActId());
           /* DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
            String s = mFormat.format(l+1);*/
            String s = (l+1)+"";
            list.get(l).setGroupCode(s);
        }

        int i =actGroupMapper.insertList(list);
        //插入成功是返回i，失败返回0
        if (i>0){
            return 1;
        }
        return 0;
    }

    //获取不需要被淘汰的小组
    @Override
    public int updateByGroup(List<ActGroupOut> list) {
        int i=actGroupMapper.updateByGroup(list);
        //1更新成功0失败
        if (i>0){
            return 1;
        }else {
            return 0;
        }
    }

    //更新小组的票数
    @Override
    public int updateGroupNum(ActGroupInVo inVo) {
        int i=actGroupMapper.updateGroupNum(inVo);
        //1更新成功0失败
        if (i>0){
            return 1;
        }else {
            return 0;
        }
    }

    //根据票数多小查询活动小组列表信息
    @Override
    public List<ActGroupOut> selectGroupOut() {
        List<ActGroupOut> list=actGroupMapper.listGroupOut();
        if (list==null||list.size()==0){
            return null;
        }
        return list;
    }

    //根据主键ID和活动ID删除参加活动小组信息
    @Override
    public Long updateID(ActGroup actGroup) {
        int i = actGroupMapper.updateByID(actGroup);
        if (i>0){
            return actGroup.getId();
        }
        return null;
    }

    @Override
    public ActGroupOut listActByShop(ActGroupInVo inVo) {
        ActGroupOut out=actGroupMapper.actByshopId(inVo);
        if (out!=null){
            return  out;
        }
        return null;
    }

    //接收商家ID列表和用户列表
    @Override
    public Map<Long, ActUserOut> mapList(List<Long> shopOutID, List<ActUserOut> userOuts) {
        Map<Long,ActUserOut> mapList= GroupUtil.groupTwo(shopOutID, userOuts);
        return mapList;
    }

    //接收商家列表和用户列表
    @Override
    public Map<ActShopOut, ActUserOut> map(List<ActShopOut> shopOuts, List<ActUserOut> userOuts) {
        Map<ActShopOut,ActUserOut> map =GroupUtil.group(shopOuts,userOuts);
        return map;
    }
}
