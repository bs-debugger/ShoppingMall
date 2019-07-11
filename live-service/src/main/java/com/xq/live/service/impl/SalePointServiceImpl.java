package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.*;
import com.xq.live.model.SalePoint;
import com.xq.live.model.SalePointUser;
import com.xq.live.model.User;
import com.xq.live.service.SalePointService;
import com.xq.live.vo.in.SalePointInVo;
import com.xq.live.vo.in.SalePointUserInVo;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.SalePointOut;
import com.xq.live.vo.out.SalePointUserOut;
import com.xq.live.vo.out.SalepointTopPicOut;
import org.javatuples.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/9/26.
 */
@Service
public class SalePointServiceImpl implements SalePointService{

    @Autowired
    SalePointMapper salePointMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    GoodsSkuMapper goodsSkuMapper;

    @Autowired
    SalePointUserMapper salePointUserMapper;

    @Autowired
    SalepointTopPicMapper salepointTopPicMapper;

    @Autowired
    private SalePointGoodsMapper salePointGoodsMapper;

    @Override
    public Pager<SalePointOut> list(SalePointInVo inVo) {
        Pager<SalePointOut> result = new Pager<SalePointOut>();
        int listTotal=salePointMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if(listTotal>0){
            List<SalePointOut> list=salePointMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Pager<GoodsSkuOut> getGoodsListBySalePointId(SalePointInVo inVo) {
        Pager<GoodsSkuOut> result = new Pager<GoodsSkuOut>();
        int listTotal=goodsSkuMapper.listTotalBySalepointId(inVo);
        result.setTotal(listTotal);
        if(listTotal>0){
            List<GoodsSkuOut> list=goodsSkuMapper.listBySalepointId(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<SalePointUser> getSalePointUserByUserId(Long userId) {
        List<SalePointUser> list=salePointUserMapper.selectByUserId(userId);
        return  list;
    }

    @Override
    public SalePoint getSalePointByID(Long id) {
        SalePoint result = salePointMapper.selectByPrimaryKey(id);
        return result;
    }

    @Override
    public List<SalePointOut> getSalePointByGoodsSkuId(SalePointInVo salePointInVo) {
        List<SalePointOut> salePointOuts = salePointMapper.listSalepointByGoodsSkuId(salePointInVo);
        for (SalePointOut salePointOut : salePointOuts) {
            if (salePointOut != null) {
                List<SalepointTopPicOut> picOutList = salepointTopPicMapper.selectBySalepointId(salePointOut.getId());
                List<Pair<String, String>> picList = new ArrayList<>();
                if (picOutList != null && picOutList.size() > 0) {
                    for (SalepointTopPicOut picOut : picOutList) {
                        picList.add(new Pair<String, String>(picOut.getAttachment().getSmallPicUrl(), picOut.getAttachment().getPicUrl()));    //小图和大图url
                    }
                }
                salePointOut.setSalepointTopPics(picList);
            }
        }
        return salePointOuts;
    }

    /**
     * 通过手机号添加核销员
     * @param salePointUserInVo
     * @return
     */
    @Override
    public Integer addSalePointUser(SalePointUserInVo salePointUserInVo) {
        User user=userMapper.findByMobile(salePointUserInVo.getPhone());
        if (user==null||user.getId()==null){
            return null;
        }
        salePointUserInVo.setUserId(user.getId());
        SalePointUser point=salePointUserMapper.selectByUserIdAndSalepint(salePointUserInVo);
        if (point!=null){
            return null;
        }
        SalePointUser salePointUser = new SalePointUser();
        BeanUtils.copyProperties(salePointUserInVo, salePointUser);
        int i = salePointUserMapper.insert(salePointUser);
        return i;
    }

    /**
     * 查询销售点的核销员
     * @param salePointUser
     * @return
     */
    @Override
    public List<SalePointUserOut> selectByUserIdAndSalepintList(SalePointUserInVo salePointUser) {
        List<SalePointUser> list = salePointUserMapper.selectByUserIdAndSalepintList(salePointUser);
        List<SalePointUserOut> listOut = new ArrayList<>();
        for (SalePointUser salePoint:list){
            SalePointUserOut salePointUserOut = new SalePointUserOut();
            User user = userMapper.selectByPrimaryKey(salePoint.getUserId());
            salePointUserOut.setUser(user);
            salePointUserOut.setSalePointUser(salePoint);
            listOut.add(salePointUserOut);
        }
        return listOut;
    }

    /**
     * 删除销售点的核销员
     * @param salePointUser
     * @return
     */
    @Override
    public Integer deleteSalepintUser(SalePointUserInVo salePointUser) {
        Integer i = salePointUserMapper.deleteByPrimaryKey(salePointUser.getId());
        return i;
    }

    @Override
    public int deleteGoodsSkuBySalePointId(SalePointInVo salePointInVo) {
        int re = salePointGoodsMapper.deleteGoodsSkuBySalePointId(salePointInVo);
        return re;
    }

}
