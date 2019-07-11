package com.xq.live.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ListObjConverter;
import com.xq.live.common.Pager;
import com.xq.live.dao.AttachmentMapper;
import com.xq.live.dao.BusinessListingsMapper;
import com.xq.live.model.Attachment;
import com.xq.live.service.BusinessListingsService;
import com.xq.live.vo.in.BusinessListingsInVo;
import com.xq.live.vo.out.BusinessListingsOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessListingsServiceImpl implements BusinessListingsService {

    @Autowired
    private BusinessListingsMapper businessListingsMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;
    /**
     * 商家列表信息
     * */
    @Override
    public PageInfo<BusinessListingsOut> getList(BusinessListingsInVo businessListingsInVo) {
        //设置分页对象
        PageHelper.startPage(businessListingsInVo.getPage(), businessListingsInVo.getRows());
        //查询列表
        List<BusinessListingsOut> list = businessListingsMapper.getList(businessListingsInVo);
        for (BusinessListingsOut businessListingsOut : list) {
            //封装核销员信息
            businessListingsOut.setShopCashier(businessListingsMapper.getClerkList(businessListingsOut.getId()));
            //封装店铺主图
            businessListingsOut.setShopOwnerMap(businessListingsMapper.getShopOwnerList(businessListingsOut.getId()));
        }
        return new PageInfo<>(list);
    }

    /**
     * 店铺所有商品
     * */
    @Override
    public Pager<BusinessListingsOut> getShopList(BusinessListingsInVo businessListingsInVo) {
        //创建分页对象
        Pager<BusinessListingsOut> result = new Pager<>();
        //查询列表
        List<BusinessListingsOut> list = businessListingsMapper.getShopList(businessListingsInVo);
        for (BusinessListingsOut businessListingsOut : list) {
            businessListingsOut.setGoodsSkuPicsList(queryImgByIds(businessListingsOut.getGoodsSkuPics()));
        }
        //转为输出实例
        List<BusinessListingsOut> accountOuts = ListObjConverter.convert(list, BusinessListingsOut.class);
        result.setPage(businessListingsInVo.getPage());
        result.setList(accountOuts);
        return result;
    }

    /**
     * 编辑商品信息
     * */
    @Override
    @Transactional
    public BaseResp updateGoods(BusinessListingsInVo businessListingsInVo) {
        //数据封装对象
        BaseResp bs = new BaseResp();
        try {
            //更新商品信息
            businessListingsMapper.updateGoods(businessListingsInVo);
            bs.setMessage("编辑商品成功");
            bs.setCode(0);
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
            bs.setMessage("编辑商品失败");
            bs.setCode(1);
            return bs;
        }
    }

    /**
     * 删除商家信息
     * */
    @Override
    @Transactional
    public BaseResp deleteBusinesses(BusinessListingsInVo businessListingsInVo) {
        //数据封装对象
        BaseResp bs = new BaseResp();
        try {
            //删除商家信息-逻辑删
            businessListingsMapper.deleteBusinesses(businessListingsInVo);
            bs.setMessage("删除商家信息成功");
            bs.setCode(0);
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
            bs.setMessage("删除商家信息失败");
            bs.setCode(1);
            return bs;
        }
    }

    /**
     * 编辑商家信息
     * */
    @Override
    @Transactional
    public BaseResp updateBusinesses(BusinessListingsInVo businessListingsInVo) {
        //数据封装对象
        BaseResp bs = new BaseResp();
        try {
            businessListingsMapper.updateBusinesses(businessListingsInVo);
            bs.setMessage("编辑商家信息成功");
            bs.setCode(0);
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
            bs.setMessage("编辑商家信息失败");
            bs.setCode(1);
            return bs;
        }
    }

    /**
     * 核销员增删
     * */
    @Override
    @Transactional
    public BaseResp updateClerk(BusinessListingsInVo businessListingsInVo) {
        //数据封装
        BaseResp bs = new BaseResp();
        try {
            //删除
            if(businessListingsInVo.getType() == 1){
                businessListingsMapper.updateClerk(businessListingsInVo);
                bs.setCode(0);
                bs.setMessage("删除成功");
                return bs;
            }else{
                //查询新增核销员cashier_id
                businessListingsInVo.setCashierId(businessListingsMapper.getCashier());
                //新增
                businessListingsMapper.insertClerk(businessListingsInVo);
                bs.setCode(0);
                bs.setMessage("新增成功");
                return bs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }

    @Override
    public BaseResp updateShopOwnerMap(BusinessListingsInVo businessListingsInVo) {
        //数据封装
        BaseResp bs = new BaseResp();
        try {
            //删除
            if(businessListingsInVo.getType() == 1){
                businessListingsMapper.updateShopOwnerMap(businessListingsInVo);
                bs.setCode(0);
                bs.setMessage("删除成功");
                return bs;
            }else{
                //新增
                Integer attachementId = businessListingsMapper.insertShopOwnerMap(businessListingsInVo);
                //新增中间表
                businessListingsInVo.setAttachementId(attachementId);
                businessListingsMapper.insertShopTopPic(businessListingsInVo);
                bs.setCode(0);
                bs.setMessage("新增成功");
                return bs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }


    /**
     * 通过图片ID查询图片路径
     * @param ids
     * @return
     */
    private List<String> queryImgByIds(String ids){
        if(StringUtils.isBlank(ids))return new ArrayList<>();
        List<Attachment> list = attachmentMapper.selectByImgIds(ids);
        List<String> imgs = new ArrayList<>();
        if(list!=null){
            for(Attachment attachment:list){
                imgs.add(attachment.getSmallPicUrl());
            }
        }
        return imgs;
    }
}
