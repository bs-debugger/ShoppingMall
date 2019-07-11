package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.DataDicMapper;
import com.xq.live.model.DataDic;
import com.xq.live.service.DataDicService;
import com.xq.live.vo.in.DataDicInvo;
import com.xq.live.vo.out.DataDicOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by lipeng on 2018/4/4.
 */
@Service
public class DataDicServiceImpl implements DataDicService{
    @Autowired
    private DataDicMapper dataDicMapper;

    @Override
    public Pager<DataDicOut> listForType(DataDicInvo invo) {
        Pager<DataDicOut> result = new Pager<DataDicOut>();
        List<DataDicOut> list = dataDicMapper.listForType(invo);
        result.setTotal(list.size());
        result.setPage(invo.getPage());
        result.setList(list);
        return result;
    }

    @Override
    public Long add(DataDicInvo invo) {
        List<DataDicOut> list = dataDicMapper.listForType(invo);
        if(list!=null&&list.size()!=0) {
            DataDicOut dataDicOut = list.get(list.size() - 1);
            int i = Integer.valueOf(dataDicOut.getCode()).intValue();
            DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
            String s = mFormat.format(i + 1);
            invo.setCode(s);
        }else{
            invo.setCode("001");
        }
        int insert = dataDicMapper.insert(invo);
        if(insert<1){
            return null;
        }
        return invo.getId();
    }

    @Override
    public Long delete(DataDicInvo invo) {
        DataDic dataDic = dataDicMapper.selectByPrimaryKey(invo.getId());
        BeanUtils.copyProperties(dataDic,invo);
        invo.setIsDeleted(1);
        int i = dataDicMapper.updateByPrimaryKeySelective(invo);
        if(i<1){
            return null;
        }
        return invo.getId();
    }

    @Override
    public DataDicOut selectByTypeAndCode(DataDicInvo invo) {
        DataDicOut dataDicOut = dataDicMapper.selectByTypeAndCode(invo);
        return dataDicOut;
    }
}
