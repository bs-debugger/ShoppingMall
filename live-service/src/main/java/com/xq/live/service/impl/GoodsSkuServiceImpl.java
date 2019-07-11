package com.xq.live.service.impl;

import com.xq.live.common.*;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ActGoodsSkuService;
import com.xq.live.service.GoodsSkuService;
import com.xq.live.service.SmsSendService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.HttpRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.javatuples.Pair;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;

/**
 * 商城系统Sku的ServiceImpl
 * Created by lipeng on 2018/8/29.
 */
@Service
public class GoodsSkuServiceImpl implements GoodsSkuService{

    private org.slf4j.Logger log = LoggerFactory.getLogger(GoodsSkuServiceImpl.class);

    @Autowired
    SalepointTopPicMapper salepointTopPicMapper;
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;
    @Autowired
    private ActInfoMapper actInfoMapper;
    @Autowired
    private SalePointMapper salePointMapper;
    @Autowired
    private SalePointGoodsMapper salePointGoodsMapper;
    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;
    @Autowired
    private  ShopMapper shopMapper;
    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private ActRankingMapper actRankingMapper;
    @Autowired
    private GoodsSpuMapper goodsSpuMapper;
    @Autowired
    private GoodsSpuSpecMapper goodsSpuSpecMapper;
    @Autowired
    private GoodsSpuDescMapper goodsSpuDescMapper;
    @Autowired
    private GoodsSkuSpecValueMapper goodsSkuSpecValueMapper;
    @Autowired
    private GoodsPromotionRulesMapper goodsPromotionRulesMapper;
    @Autowired
    private DeliveryTemplateMapper deliveryTemplateMapper;
    @Autowired
    private DeliveryMethodMapper deliveryMethodMapper;
    @Autowired
    private DeliveryTemplateShopMapper deliveryTemplateShopMapper;

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Autowired
    private GoodsSkuRejectLogMapper goodsSkuRejectLogMapper;

    @Autowired
    private GoodsSkuAuditLogMapper goodsSkuAuditLogMapper;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ActTimeRulesMapper actTimeRulesMapper;

    @Autowired
    private GoodsSkuDetailMapper goodsSkuDetailMapper;

    private Logger logger = Logger.getLogger(GoodsSkuServiceImpl.class);

    @Override
    public GoodsSku selectOne(Long id) {
        return goodsSkuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(GoodsSkuInVo inVo) {
        inVo.setSkuCode(RandomStringUtil.getRandomCode(8, 0));
        inVo.setIsDeleted(GoodsSku.GOODS_SKU_NO_DELETED);//未删除
        inVo.setStatus(GoodsSku.STATUS_SJ);//插入上架
        int res = goodsSkuMapper.insert(inVo);

        if(res < 1){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Integer update(GoodsSkuInVo inVo) {
        GoodsSku goodsSku=goodsSkuMapper.selectByPrimaryKey(inVo.getId());
        //商品由下架状态改为上架状态时，将该商品排序放在第一位
        if(goodsSku!=null&&goodsSku.getAuditStatus()!=2){//当前商品审核状态为未通过
            throw new RuntimeException("提示商品已提交审核");
        }
        if(goodsSku!=null&&goodsSku.getStatus()==GoodsSku.STATUS_XJ&&inVo.getStatus()==GoodsSku.STATUS_SJ){
            this.setGoodsSkuTop(inVo.getId());
        }

        // 查询商品的店铺
        Shop shop = shopMapper.selectByPrimaryKey(goodsSku.getShopId());
        inVo.setAuditStatus(null);
        inVo.setUpdateTime(new Date());
        Integer result = goodsSkuMapper.updateByPrimaryKeySelective(inVo);
        if (result > 0 && shop != null && !StringUtils.isBlank(shop.getCity())) {
            log.info("============================================================================");
            log.info("缓存删除");
            log.info("============================================================================");
            // 删除商品对应城市的活动推荐缓存
            redisCache.del("at-activity-recommend-" + shop.getCity());
            redisCache.del("at-banner-activity-" + shop.getCity());
            redisCache.del("at-benefits-activity-" + shop.getCity());
            redisCache.del("at-yy-seckill-activity-" + shop.getCity());
            redisCache.del("at-choiceness-shop-activity-" + shop.getCity());
        }
        return result;
    }

    @Override
    public Pager<GoodsSkuOut> list(GoodsSkuInVo inVo) {
        Pager<GoodsSkuOut> result = new Pager<GoodsSkuOut>();
        if(null!=inVo.getStatus()&&inVo.getStatus()==100){//当值为100 时  查询待审核商品
            inVo.setAuditStatus(100);
            inVo.setStatus(null);
        }else{
            inVo.setAuditStatus(null);
        }
        int listTotal = goodsSkuMapper.listTotal(inVo);
        if(listTotal > 0){
            List<GoodsSkuOut> list = goodsSkuMapper.list(inVo);
            //为了避免查询过多,只有在查询某个店铺的商品的时候,查询对应的活动列表
            if(inVo.getShopId()!=null) {
                for (GoodsSkuOut goodsSkuOut : list) {
                    this.setActInfoDetailAndActInfoList(inVo, goodsSkuOut);
                }
            }
            this.setSalePointOutsByGoodsSkuOutList(list,inVo);//添加商品支持的销售点
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    /*要删的，万达用*/
    @Override
    public Pager<GoodsSkuOut> listNewForPlaza(GoodsSkuInVo inVo) {
        Pager<GoodsSkuOut> result = new Pager<GoodsSkuOut>();
        int listTotal = goodsSkuMapper.listPlazaTotal(inVo);
        if(listTotal > 0){
            List<GoodsSkuOut> list = goodsSkuMapper.plazaList(inVo);
            //为了避免查询过多,只有在查询某个店铺的商品的时候,查询对应的活动列表
            if(inVo.getShopId()!=null) {
                for (GoodsSkuOut goodsSkuOut : list) {
                    this.setActInfoDetailAndActInfoList(inVo, goodsSkuOut);
                }
            }
            this.setSalePointOutsByGoodsSkuOutList(list,inVo);//添加商品支持的销售点
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    public List<GoodsSkuOut> top(GoodsSkuInVo inVo) {
        return goodsSkuMapper.list(inVo);
    }

    @Override
    public GoodsSkuOut selectDetailBySkuId(Long id) {
        return goodsSkuMapper.selectDetailBySkuId(id);
    }

    /**
     * 查询单个商品详情带上销售点
     * @param inVo
     * @return
     */
    @Override
    public GoodsSkuOut selectPlaceBySkuId(GoodsSkuInVo inVo) {
        GoodsSkuOut goodsSkuOut = goodsSkuMapper.selectDetailBySkuId(inVo.getId());
        if (goodsSkuOut==null){
            return  null;
        }
        //查询商品参与的活动列表,和对应某个活动的活动详情和商品是否能够下架
        this.setActInfoDetailAndActInfoList(inVo,goodsSkuOut);

        //查询商品对应的spu的详细信息和spu对应的文描信息
        GoodsSpuOut goodsSpuOut = goodsSpuMapper.selectDetailById(goodsSkuOut.getSpuId());
        goodsSkuOut.setGoodsSpuOut(goodsSpuOut);

        //查询商品的类目信息
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goodsSkuOut.getCategoryId());
        goodsSkuOut.setGoodsCategory(goodsCategory);

        //查询对应的商家信息
        if (goodsSkuOut.getShopId()>0){
            Shop shop=shopMapper.selectByPrimaryKey(goodsSkuOut.getShopId());
            goodsSkuOut.setShop(shop);

            ShopInVo shopInVo=new ShopInVo();
            shopInVo.setLocationY(inVo.getLocationY());
            shopInVo.setId(goodsSkuOut.getShopId());
            shopInVo.setLocationX(inVo.getLocationX());
            ShopOut shopOut=shopMapper.selectDetailById(shopInVo);
            goodsSkuOut.setShopOut(shopOut);
        }

        //添加商品获奖结果
        this.addActRankingInfo(goodsSkuOut);

        //查询销售点
        SalePointInVo salePointInVo=new SalePointInVo();
        salePointInVo.setGoodsSkuId(inVo.getId());
        salePointInVo.setLocationX(inVo.getLocationX());
        salePointInVo.setLocationY(inVo.getLocationY());
        List<SalePointOut> salePointOuts = salePointMapper.listSalepointByGoodsSkuId(salePointInVo);
        this.setSalepointTopPicsBySalePointOutList(salePointOuts);//添加销售点对应的图片组
        goodsSkuOut.setSalePointOuts(salePointOuts);
        
        this.setGoodsSkuPics(goodsSkuOut);//设置商品图片组
        this.setGoodsPayType(goodsSkuOut);//设置购买方式

        //当上商品状态为驳回时  需要返回驳回原因
        if(goodsSkuOut.getAuditStatus()!=null&&goodsSkuOut.getAuditStatus()==3){
            GoodsSkuRejectLog goodsSkuRejectLog = goodsSkuRejectLogMapper.selectLasterByGoodsSku(inVo.getId());
            goodsSkuOut.setGoodsSkuRejectLog(goodsSkuRejectLog);
        }

        if(null!=goodsSkuOut.getUseSkuDetail()){//设置商品详细信息
            List<GoodsSkuDetail> goodsSkuDetails=goodsSkuDetailMapper.listBySkuId(goodsSkuOut.getId());
            goodsSkuOut.setGoodsSkuDetails(goodsSkuDetails);
        }

        return goodsSkuOut;
    }

    /*设置购买方式*/
    private void setGoodsPayType(GoodsSkuOut goodsSkuOut){
        if (goodsSkuOut.getGoodsCategory().getId().toString().equals("87")){
            goodsSkuOut.setGoodsPayType(GoodsSku.GOODS_SKU_PAY_TYPE_YJ);
        }else if (goodsSkuOut.getShopId()==0){
            goodsSkuOut.setGoodsPayType(GoodsSku.GOODS_SKU_PAY_TYPE_ALL);
        }else {
            goodsSkuOut.setGoodsPayType(GoodsSku.GOODS_SKU_PAY_TYPE_KJ);
        }
    }

    /**
     * 设置商品图片组
     * @param goodsSkuOut
     */
    private void setGoodsSkuPics(GoodsSkuOut goodsSkuOut) {
        if(StringUtils.isNotBlank(goodsSkuOut.getGoodsSkuPics())){
            String[] regoinArr = goodsSkuOut.getGoodsSkuPics().split(",");
            List<String> stringList = Arrays.asList(regoinArr);
            if(stringList!=null&&stringList.size()>0){
                List<Long> ids = new ArrayList<Long>();
                for(String picId : stringList){
                    ids.add(Long.valueOf(picId));
                }
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("ids", ids);
                List<Attachment> picUrls = attachmentMapper.selectByIds(paramsMap);
                goodsSkuOut.setAttachments(picUrls);
            }
        }
    }

    /**
     * 添加商品参与的活动列表,和对应某个活动的活动详情
     * @param inVo
     * @param goodsSkuOut
     */
    public void setActInfoDetailAndActInfoList(GoodsSkuInVo inVo,GoodsSkuOut goodsSkuOut){
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setSkuId(goodsSkuOut.getId());
        //查询商品参与的活动列表
        List<ActGoodsSkuOut> actGoodsSkuOuts = actGoodsSkuMapper.selectActInfoListByGoodsSkuId(actGoodsSkuInVo);
        int canUpdate = 0;
        /*for (ActGoodsSkuOut actGoodsSkuOut : actGoodsSkuOuts) {
            if(actGoodsSkuOut.getActId().equals(45L)){//如果参与活动id为45,则这个商品不能下架
                canUpdate = 1;
            }
        }*/
        ActRankingInVo actRankingInVo = new ActRankingInVo();
        actRankingInVo.setActId(new Long(45));
        actRankingInVo.setType(ActRanking.ACT_TYPE_SKU);
        List<ActRankingOut> rankingOuts=actRankingMapper.selectActInfoList(actRankingInVo);
        for (ActRankingOut actRankingOut:rankingOuts){
            if (goodsSkuOut.getId().toString().equals(actRankingOut.getSkuId().toString())){
                canUpdate = 1;//如果参与活动id为45,且获奖的商品,则这个商品不能下架
            }
        }

        goodsSkuOut.setCanUpdate(canUpdate);
        goodsSkuOut.setActGoodsSkuOuts(actGoodsSkuOuts);

        if (inVo.getActId()!=null){
            //添加活动类目
            actGoodsSkuInVo.setActId(inVo.getActId());
            ActGoodsSkuOut actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            goodsSkuOut.setActGoodsSkuOut(actGoodsSkuOut);
            ActOrder order = new ActOrder();
            order.setUserId(inVo.getUserId());
            order.setActGoodsSkuId(actGoodsSkuOut.getId());
            List<ActOrder> actOrder = actOrderMapper.selectByActGoodsSkuId(order);
            goodsSkuOut.setActOrder(actOrder);
        }

        if(null!=goodsSkuOut.getUseSkuDetail()){//设置商品详细信息
            List<GoodsSkuDetail> goodsSkuDetails=goodsSkuDetailMapper.listBySkuId(goodsSkuOut.getId());
            goodsSkuOut.setGoodsSkuDetails(goodsSkuDetails);
        }
    }

    @Override
    public List<GoodsSkuOut> selectListBySkuId(List<OrderCartOut> items) {
        return goodsSkuMapper.selectListBySkuId(items);
    }
    /**
     * 分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    @Override
    public Pager<GoodsSkuOut> actList(GoodsSkuInVo inVo) {
        Date time = new Date();
        Pager<GoodsSkuOut> result = new Pager<GoodsSkuOut>();
        int listTotal = goodsSkuMapper.selectActTotal(inVo);
        if(listTotal > 0){
            List<GoodsSkuOut> list = goodsSkuMapper.selectActList(inVo);
            this.setSalePointOutsByGoodsSkuOutList(list, inVo);//添加商品支持的销售点
                for (GoodsSkuOut skuOuts:list){
                    if (skuOuts.getShopId()> 0){
                        Shop shop = shopMapper.selectByPrimaryKey(skuOuts.getShopId());
                        skuOuts.setShop(shop);
                    }
                    //添加商品获奖结果
                    this.addActRankingInfo(skuOuts);
                    ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
                    actGoodsSkuInVo.setSkuId(skuOuts.getId());
                    actGoodsSkuInVo.setActId(inVo.getActId());
                    ActGoodsSkuOut actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                    ActInfo actInfo = actInfoMapper.selectByPrimaryKey(inVo.getActId());
                    ActGoodsSku goodsSku=new ActGoodsSku();
                    if (actGoodsSkuOut!=null&&actGoodsSkuOut.getActId()!=null){
                        BeanUtils.copyProperties(actGoodsSkuOut, goodsSku);
                        if (actGoodsSkuOut.getState() == ActGoodsSku.STATE_WAIT&&actInfo.getType()==OrderInfo.FLAG_TYPE_TG){
                            //判断活动是否结束
                            if (time.getTime()>=actGoodsSkuOut.getDueTime().getTime()){
                                if (actGoodsSkuOut.getCurrentNum()<actGoodsSkuOut.getPeopleNum()){
                                    goodsSku.setState(ActGoodsSku.STATE_FAIL);
                                }
                                //修改超时团状态
                                actGoodsSkuMapper.updateOverTimeActStatus(goodsSku);
                                //修改用户状态
                                ActOrder actOrder = new ActOrder();
                                actOrder.setActGoodsSkuId(goodsSku.getId());
                                actOrderMapper.updateOverTimeByStatus(actOrder);
                                if(goodsSku.getState()==ActGoodsSku.STATE_FAIL){
                                    actGoodsSkuService.failDistribution(goodsSku);//失败修改该团产生的相关奖励金
                                }
                            }
                        }
                        //将修改过后的数据返回
                        ActGoodsSku actGoodsSkus=actGoodsSkuMapper.selectByPrimaryKey(actGoodsSkuOut.getId());
                        BeanUtils.copyProperties(actGoodsSkus, actGoodsSkuOut);
                        skuOuts.setActGoodsSkuOut(actGoodsSkuOut);
                    }
                }
                result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    private void addActRankingInfo(GoodsSkuOut skuOuts ){
        ActRankingInVo actRankingInVo = new ActRankingInVo();
        actRankingInVo.setActId(new Long(45));
        actRankingInVo.setSkuId(skuOuts.getId());
        actRankingInVo.setType(ActRanking.ACT_TYPE_SKU);
        skuOuts.setActRankingOut(actRankingMapper.selectActInfo(actRankingInVo));
    }

    @Override
    public Pager<ActGoodsSkuOut> actEndList(GoodsSkuInVo inVo) {
        Pager<ActGoodsSkuOut> result = new Pager<ActGoodsSkuOut>();
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setActId(inVo.getActId());
        actGoodsSkuInVo.setPage(inVo.getPage());
        actGoodsSkuInVo.setRows(inVo.getRows());
        int listTotal = actGoodsSkuMapper.selectHistoryTotal(actGoodsSkuInVo);
        if(listTotal > 0){
            List<ActGoodsSkuOut> actGoodsSkuOutList= actGoodsSkuMapper.selectHistoryList(actGoodsSkuInVo);
            result.setList(actGoodsSkuOutList);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    /**
     * 分页查询参与此活动的商品(首页)
     * @param inVo
     * @return
     */
    @Override
    public List<GoodsSkuOut> actListHome(GoodsSkuInVo inVo) {
        List<GoodsSkuOut> list = goodsSkuMapper.selectActListNotAutotrophy(inVo);
        this.setSalePointOutsByGoodsSkuOutList(list, inVo);//添加商品支持的销售点
        for (GoodsSkuOut skuOuts:list){
            ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
            actGoodsSkuInVo.setSkuId(skuOuts.getId());
            actGoodsSkuInVo.setActId(inVo.getActId());
            ActGoodsSkuOut actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            if (skuOuts.getShopId()>0){
                Shop shop=shopMapper.selectByPrimaryKey(skuOuts.getShopId());
                skuOuts.setShop(shop);
            }
            skuOuts.setActGoodsSkuOut(actGoodsSkuOut);
        }
        return list;
    }

    /**
     * 分页查询参与此活动的推荐商品
     * @param inVo
     * @return
     */
    @Override
    public Pager<GoodsSkuOut> actOutList(GoodsSkuInVo inVo) {
        Pager<GoodsSkuOut> result = new Pager<GoodsSkuOut>();
        int listTotal = goodsSkuMapper.selectActOutTotal(inVo);
        if(listTotal > 0){
            List<GoodsSkuOut> list = goodsSkuMapper.selectOutActList(inVo);
            this.setSalePointOutsByGoodsSkuOutList(list,inVo);//添加商品支持的销售点
            for (GoodsSkuOut skuOuts:list){
                if (skuOuts.getShopId()!=0){
                    Shop shop = shopMapper.selectByPrimaryKey(skuOuts.getShopId());
                    skuOuts.setShop(shop);
                }
                ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
                actGoodsSkuInVo.setSkuId(skuOuts.getId());
                actGoodsSkuInVo.setActId(inVo.getActId());
                ActGoodsSkuOut actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                skuOuts.setActGoodsSkuOut(actGoodsSkuOut);
            }
            result.setList(list);
            result.setRows(list.size());
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    /**
     * 不分页查询参与此活动的商品
     * @param inVo
     * @return
     */
    @Override
    public List<GoodsSkuOut> actListAll(GoodsSkuInVo inVo) {
        List<GoodsSkuOut> list = goodsSkuMapper.selectNoPageActList(inVo);
        this.setSalePointOutsByGoodsSkuOutList(list, inVo);//添加商品支持的销售点
        for (GoodsSkuOut skuOuts:list){
            ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
            actGoodsSkuInVo.setSkuId(skuOuts.getId());
            actGoodsSkuInVo.setActId(inVo.getActId());
            ActGoodsSkuOut actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            skuOuts.setActGoodsSkuOut(actGoodsSkuOut);
        }
        return list;
    }

    @Override
    @Transactional
    public Long addGoodsSkuAndAct(GoodsSkuInVo inVo) {

        /*
          1.添加goods_spu和goods_spu_spec和goods_spu_desc
          2.添加goods_sku和goods_sku_spec_value
          3.添加goods_promotion_rules和act_goods_sku
         */
        //1.添加goods_spu和goods_spu_spec和goods_spu_desc,返回spuId
        Long spuId = this.addGoodsSpuAndGoodsSpuDesc(inVo.getGoodsSpuInVo());

        //2.添加goods_sku和goods_sku_spec_value,返回goodsSkuId
        Long goodsSkuId = this.addGoodsSkuAndGoodsSkuSpecValue(inVo, spuId);

        //3.添加goods_promotion_rules和act_goods_sku
        List<ActGoodsSkuInVo> actGoodsSkuInVos = inVo.getActGoodsSkuInVos();
        for (ActGoodsSkuInVo actGoodsSkuInVo : actGoodsSkuInVos) {
            ActInfoOut actInfoOut = actInfoMapper.findActInfoById(actGoodsSkuInVo.getActId());
            //等线上兼容
           /* if(inVo.getActId().toString().equals(41)){
                    actGoodsSkuInVo.setPeopleNum(3);
            }*/
            GoodsPromotionRules goodsPromotionRules = actGoodsSkuInVo.getGoodsPromotionRules();
            goodsPromotionRules.setGoodsSkuId(goodsSkuId);
            goodsPromotionRules.setGoodsSkuCode(inVo.getSkuCode());
            goodsPromotionRules.setGoodsSkuName(inVo.getSkuName());
            int goodsPromotionRulesInsert = goodsPromotionRulesMapper.insert(goodsPromotionRules);
            Long goodsPromotionRulesId = goodsPromotionRules.getId();
            ActGoodsSku actGoodsSku = new ActGoodsSku();
            BeanUtils.copyProperties(actGoodsSkuInVo,actGoodsSku);
            actGoodsSku.setSkuId(goodsSkuId);
            actGoodsSku.setSkuCode(inVo.getSkuCode());
            actGoodsSku.setGoodsPrId(goodsPromotionRulesId);
            actGoodsSku.setApplyStatus(ActGoodsSku.APPLY_STATUS_SUCCESS);
            actGoodsSku.setState(ActGoodsSku.STATE_WAIT);
            /*if(goodsPromotionRules.getRuleType()==GoodsPromotionRules.RULE_TYPE_KJ){
                actGoodsSku.setPeopleNum(4);
            }*/
            /*替换*/
            if (actGoodsSkuInVo.getPeopleNum()!=null){
                actGoodsSku.setPeopleNum(actGoodsSkuInVo.getPeopleNum());
            }else {
                actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());
            }
            /*actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());*/
            int actGoodsSkuInsert = actGoodsSkuMapper.insert(actGoodsSku);

            if(actGoodsSkuInVo.getActTimeRules()!=null&&actGoodsSkuInVo.getActTimeRules().size()>0){
                for(ActTimeRules actTimeRules:actGoodsSkuInVo.getActTimeRules()){
                    actTimeRules.setRuleType(0);//关联类型:商品关联为0
                    actTimeRules.setRefId(actGoodsSku.getId());
                    actTimeRulesMapper.insert(actTimeRules);
                }
            }

            if(goodsPromotionRulesInsert<1||actGoodsSkuInsert<1){
                logger.error("添加goods_promotion_rules和act_goods_sku失败");
                throw new RuntimeException("添加goods_promotion_rules和act_goods_sku失败");
            }
        }

        //4.添加商品详细信息
        if(null!=inVo.getUseSkuDetail()&&inVo.getUseSkuDetail()==1&&null!=inVo.getGoodsSkuDetails()&&inVo.getGoodsSkuDetails().size()>0){
            for(GoodsSkuDetail goodsSkuDetail:inVo.getGoodsSkuDetails()){
                goodsSkuDetail.setGoodsSkuId(goodsSkuId);
                goodsSkuDetailMapper.insert(goodsSkuDetail);
            }
        }

        if(goodsSkuId!=null){
            this.setGoodsSkuTop(goodsSkuId);
        }
        return goodsSkuId;
    }

    @Override
    @Transactional
    public Long addPlatformGoodsSkuAndAct(GoodsSkuInVo inVo) {
        /*
          1.添加goods_spu和goods_spu_spec和goods_spu_desc
          2.添加goods_sku和goods_sku_spec_value
          3.添加goods_promotion_rules和act_goods_sku
         */
        //1.添加goods_spu和goods_spu_spec和goods_spu_desc,返回spuId
        Long spuId = this.addPlatformGoodsSpuAndGoodsSpuDesc(inVo.getGoodsSpuInVo());

        //2.添加goods_sku和goods_sku_spec_value和delivery_template和delivery_template_shop和delivery_method,返回goodsSkuId
        Long goodsSkuId = this.addPlatformGoodsSkuAndGoodsSkuSpecValue(inVo, spuId);

        //3.添加goods_promotion_rules和act_goods_sku
        List<ActGoodsSkuInVo> actGoodsSkuInVos = inVo.getActGoodsSkuInVos();
        for (ActGoodsSkuInVo actGoodsSkuInVo : actGoodsSkuInVos) {
            //等线上兼容
           /* if(inVo.getActId().toString().equals(41)){
                    actGoodsSkuInVo.setPeopleNum(3);
            }*/
            GoodsPromotionRules goodsPromotionRules = actGoodsSkuInVo.getGoodsPromotionRules();
            Long goodsPromotionRulesId = null;
            if(goodsPromotionRules!=null&&goodsPromotionRules.getActAmount()!=null) {
                goodsPromotionRules.setGoodsSkuId(goodsSkuId);
                goodsPromotionRules.setGoodsSkuCode(inVo.getSkuCode());
                goodsPromotionRules.setGoodsSkuName(inVo.getSkuName());
                goodsPromotionRulesMapper.insert(goodsPromotionRules);
                goodsPromotionRulesId = goodsPromotionRules.getId();
            }
            ActGoodsSku actGoodsSku = new ActGoodsSku();
            BeanUtils.copyProperties(actGoodsSkuInVo,actGoodsSku);
            actGoodsSku.setSkuId(goodsSkuId);
            actGoodsSku.setSkuCode(inVo.getSkuCode());
            actGoodsSku.setGoodsPrId(goodsPromotionRulesId);
            actGoodsSku.setApplyStatus(ActGoodsSku.APPLY_STATUS_SUCCESS);
            actGoodsSku.setState(ActGoodsSku.STATE_WAIT);
            int actGoodsSkuInsert = actGoodsSkuMapper.insert(actGoodsSku);
            if(actGoodsSkuInsert<1){
                logger.error("添加goods_promotion_rules和act_goods_sku失败");
                throw new RuntimeException("添加goods_promotion_rules和act_goods_sku失败");
            }
        }
        return goodsSkuId;
    }

    @Override
    @Transactional
    public Long addPlatformGoodsSkuAndSalePoint(GoodsSkuInVo inVo) {
        /*
          1.添加goods_spu和goods_spu_spec和goods_spu_desc
          2.添加goods_sku和goods_sku_spec_value
          3.添加goods_promotion_rules和act_goods_sku
         */
        //1.添加goods_spu和goods_spu_spec和goods_spu_desc,返回spuId
        Long spuId = this.addPlatformGoodsSpuAndGoodsSpuDesc(inVo.getGoodsSpuInVo());

        //2.添加goods_sku和goods_sku_spec_value和delivery_template和delivery_template_shop和delivery_method,返回goodsSkuId
        Long goodsSkuId = this.addPlatformGoodsSkuAndGoodsSkuSpecValue(inVo, spuId);

        //3.添加salepoint_goods
        this.addSalePointByGoodsSkuId(inVo.getSalePointInVo(), goodsSkuId);
        return goodsSkuId;
    }

    @Override
    @Transactional
    public Integer updateGoodsSkuAndAct(GoodsSkuInVo inVo) {
        GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(inVo.getId());
        //有关键信息修改时 需要将审核状态设置为待审核 商品设置为下架
        if(null!=inVo.getActId()&&inVo.getActId()==48){//参加万达会员优惠券活动的商品不需要审核
            inVo.setStatus(GoodsSku.STATUS_SJ);
            inVo.setAuditStatus(2);
        }else if(chenkIsAudit(inVo,goodsSku)){
            if(goodsSku.getStatus()==1){
                inVo.setAuditStatus(-1);//上架状态待审核
            }else{
                inVo.setAuditStatus(-2);//下架状态待审核
            }
            inVo.setStatus(GoodsSku.STATUS_XJ);
        }else{//没有关键信息修改时  商品直接设置为审核通过
            inVo.setAuditStatus(2);
        }

        /*
          1.更新goods_spu和goods_spu_spec和goods_spu_desc
          2.更新goods_sku和goods_sku_spec_value
          3.更新goods_promotion_rules和act_goods_sku
         */
        //1.更新goods_spu和goods_spu_spec和goods_spu_desc
        Integer goodsSpuUpdate = this.updateGoodsSpuAndGoodsSpuDesc(inVo.getGoodsSpuInVo());

        // 2.更新goods_sku和goods_sku_spec_value
        Integer goodsSkuUpdate = this.updateGoodsSkuAndGoodsSkuSpecValue(inVo);

        //3.更新goods_promotion_rules和act_goods_sku
        List<ActGoodsSkuInVo> actGoodsSkuInVos = inVo.getActGoodsSkuInVos();
        for (ActGoodsSkuInVo actGoodsSkuInVo : actGoodsSkuInVos) {
            ActInfoOut actInfoOut = actInfoMapper.findActInfoById(actGoodsSkuInVo.getActId());
            if(actGoodsSkuInVo.getId()==null) {
                GoodsPromotionRules goodsPromotionRules = actGoodsSkuInVo.getGoodsPromotionRules();
                goodsPromotionRules.setGoodsSkuId(inVo.getId());
                goodsPromotionRules.setGoodsSkuCode(inVo.getSkuCode());
                goodsPromotionRules.setGoodsSkuName(inVo.getSkuName());
                int goodsPromotionRulesInsert = goodsPromotionRulesMapper.insert(goodsPromotionRules);
                Long goodsPromotionRulesId = goodsPromotionRules.getId();
                ActGoodsSku actGoodsSku = new ActGoodsSku();
                BeanUtils.copyProperties(actGoodsSkuInVo, actGoodsSku);
                actGoodsSku.setSkuId(inVo.getId());
                actGoodsSku.setSkuCode(inVo.getSkuCode());
                actGoodsSku.setGoodsPrId(goodsPromotionRulesId);
                actGoodsSku.setApplyStatus(ActGoodsSku.APPLY_STATUS_SUCCESS);
                actGoodsSku.setState(ActGoodsSku.STATE_WAIT);
                /*替换*/
                if (actGoodsSkuInVo.getPeopleNum()!=null){
                    actGoodsSku.setPeopleNum(actGoodsSkuInVo.getPeopleNum());
                }else {
                    actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());
                }
                /*替换*/
               /* actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());*/
                int actGoodsSkuInsert = actGoodsSkuMapper.insert(actGoodsSku);
                this.updateActTimeRules(actGoodsSkuInVo.getActTimeRules(),actGoodsSku.getId());//更新活动时间规则
                if (goodsPromotionRulesInsert < 1 || actGoodsSkuInsert < 1) {
                    logger.error("添加goods_promotion_rules和act_goods_sku失败");
                    throw new RuntimeException("添加goods_promotion_rules和act_goods_sku失败");
                }
            }else if(actGoodsSkuInVo.getId()!=null&&actGoodsSkuInVo.getIsDeleted()==ActGoodsSku.IS_DELETED_NO){
                GoodsPromotionRules goodsPromotionRules = actGoodsSkuInVo.getGoodsPromotionRules();
                goodsPromotionRules.setGoodsSkuId(inVo.getId());
                goodsPromotionRules.setGoodsSkuCode(inVo.getSkuCode());
                goodsPromotionRules.setGoodsSkuName(inVo.getSkuName());
                int goodsPromotionRulesUpdate = goodsPromotionRulesMapper.updateByPrimaryKeySelective(goodsPromotionRules);
                ActGoodsSku actGoodsSku = new ActGoodsSku();
                BeanUtils.copyProperties(actGoodsSkuInVo, actGoodsSku);
                /*替换*/
                if (actGoodsSkuInVo.getPeopleNum()!=null){
                    actGoodsSku.setPeopleNum(actGoodsSkuInVo.getPeopleNum());
                }else {
                    actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());
                }
                /*替换*/
                /*actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());*/
                int actGoodsSkuUpdate = actGoodsSkuMapper.updateByPrimaryKeySelective(actGoodsSku);
                if(actGoodsSkuInVo.getActTimeRules()!=null&&actGoodsSkuInVo.getActTimeRules().size()>0){
                    this.updateActTimeRules(actGoodsSkuInVo.getActTimeRules(),actGoodsSku.getId());//更新活动时间规则
                }
                if (goodsPromotionRulesUpdate < 1 || actGoodsSkuUpdate < 1) {
                    logger.error("更新goods_promotion_rules和act_goods_sku失败");
                    throw new RuntimeException("更新goods_promotion_rules和act_goods_sku失败");
                }
            }else if(actGoodsSkuInVo.getId()!=null&&actGoodsSkuInVo.getIsDeleted()==ActGoodsSku.IS_DELETED_YES){
                int goodsPromotionRulesUpdate = goodsPromotionRulesMapper.deleteByPrimaryKey(actGoodsSkuInVo.getGoodsPromotionRules().getId());
                ActGoodsSku actGoodsSku = new ActGoodsSku();
                BeanUtils.copyProperties(actGoodsSkuInVo, actGoodsSku);
                /*替换*/
               if (actGoodsSkuInVo.getPeopleNum()!=null){
                    actGoodsSku.setPeopleNum(actGoodsSkuInVo.getPeopleNum());
                }else {
                    actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());
                }
                /*替换*/
                /*actGoodsSku.setPeopleNum(actInfoOut.getPeopleNum());*/
                int actGoodsSkuUpdate = actGoodsSkuMapper.updateByPrimaryKeySelective(actGoodsSku);
                this.updateActTimeRules(actGoodsSkuInVo.getActTimeRules(),actGoodsSku.getId());//更新活动时间规则
                if (goodsPromotionRulesUpdate < 1 || actGoodsSkuUpdate < 1) {
                    logger.error("更新goods_promotion_rules和act_goods_sku失败");
                    throw new RuntimeException("更新goods_promotion_rules和act_goods_sku失败");
                }
            }

        }

        this.updateGoodsDetail(inVo);//更新商品明细

        this.setGoodsSkuTop(inVo.getId());
        return goodsSkuUpdate;
    }

    //更新商品明细
    public void updateGoodsDetail(GoodsSkuInVo inVo){
        if(null!=inVo.getGoodsSkuDetails()&&inVo.getGoodsSkuDetails().size()>0){
            for(GoodsSkuDetail goodsSkuDetail:inVo.getGoodsSkuDetails()){
                if(goodsSkuDetail.getId()==null){
                    goodsSkuDetail.setGoodsSkuId(inVo.getId());
                    goodsSkuDetailMapper.insert(goodsSkuDetail);
                }else if (goodsSkuDetail.getId()!=null){
                    goodsSkuDetail.setGoodsSkuId(inVo.getId());
                    goodsSkuDetailMapper.updateByPrimaryKeySelective(goodsSkuDetail);
                }
            }
        }
    };

    //更新活动时间规则
    public void updateActTimeRules(List<ActTimeRules> actTimeRulesList,Long actGoodsSkuId){
            for(ActTimeRules actTimeRules:actTimeRulesList){
                if(actTimeRules.getId()==null){
                    actTimeRules.setRuleType(0);//关联类型(0 商品关联act_goods_sku
                    actTimeRules.setRefId(actGoodsSkuId);
                    actTimeRulesMapper.insert(actTimeRules);
                }else if (actTimeRules.getId()!=null){
                    actTimeRules.setRuleType(0);//关联类型(0 商品关联act_goods_sku
                    actTimeRules.setRefId(actGoodsSkuId);
                    actTimeRulesMapper.updateByPrimaryKeySelective(actTimeRules);
                }
            }
    }

    /**
     * 添加salepoint_goods
     * @param salePointInVo
     * @param goodsSkuId
     */
    public void addSalePointByGoodsSkuId(SalePointInVo salePointInVo,Long goodsSkuId){
        if(salePointInVo!=null&&salePointInVo.getSalePointIds()!=null&&salePointInVo.getSalePointIds().size()>0){
            for(int i = 0;i<salePointInVo.getSalePointIds().size();i++){
                SalePointGoods salePointGoods = new SalePointGoods();
                salePointGoods.setSalepointId(salePointInVo.getSalePointIds().get(i));
                salePointGoods.setSkuId(goodsSkuId);
                salePointGoods.setShopId(salePointInVo.getShopId());
                salePointGoodsMapper.insert(salePointGoods);
            }
        }
    }

    /**
     * 添加goods_spu和goods_spu_spec和goods_spu_desc,返回spuId
     * @param goodsSpuInVo
     * @return  spuId
     */
    public Long addGoodsSpuAndGoodsSpuDesc(GoodsSpuInVo goodsSpuInVo){
        DecimalFormat mFormat = new DecimalFormat("10000000");//确定格式，把1转换为001
        //1.添加goods_spu
        Long spuTotal = goodsSpuMapper.countTotal();
        String spuCode = mFormat.format(spuTotal + 1);
        goodsSpuInVo.setSpuCode(spuCode);
        goodsSpuInVo.setIsDeleted(GoodsSpu.GOODS_SPU_NO_DELETED);
        int goodsSpuInsert = goodsSpuMapper.insert(goodsSpuInVo);
        Long spuId = goodsSpuInVo.getId();
        //2.添加goods_spu_spec
        GoodsSpuSpec goodsSpuSpec = goodsSpuInVo.getGoodsSpuSpec();
        goodsSpuSpec.setSpuId(spuId);
        int goodsSpuSpecInsert = goodsSpuSpecMapper.insert(goodsSpuSpec);
        //3.添加goods_spu_desc
        GoodsSpuDesc goodsSpuDesc = goodsSpuInVo.getGoodsSpuDesc();
        goodsSpuDesc.setSpuId(spuId);
        int goodsSpuDescInsert = goodsSpuDescMapper.insert(goodsSpuDesc);
        if(goodsSpuInsert<1||goodsSpuSpecInsert<1||goodsSpuDescInsert<1){
            logger.error("添加goods_spu和goods_spu_spec和goods_spu_desc失败");
            throw new RuntimeException("添加goods_spu和goods_spu_spec和goods_spu_desc失败");
        }
        return spuId;
    }

    /**
     * 添加goods_spu和goods_spu_spec和goods_spu_desc,返回spuId
     * @param goodsSpuInVo
     * @return  spuId
     */
    public Long addPlatformGoodsSpuAndGoodsSpuDesc(GoodsSpuInVo goodsSpuInVo){
        DecimalFormat mFormat = new DecimalFormat("10000000");//确定格式，把1转换为001
        //1.添加goods_spu
        Long spuTotal = goodsSpuMapper.countTotal();
        String spuCode = mFormat.format(spuTotal + 1);
        goodsSpuInVo.setSpuCode(spuCode);
        goodsSpuInVo.setIsDeleted(GoodsSpu.GOODS_SPU_NO_DELETED);
        int goodsSpuInsert = goodsSpuMapper.insert(goodsSpuInVo);
        Long spuId = goodsSpuInVo.getId();
        //2.添加goods_spu_spec
        GoodsSpuSpec goodsSpuSpec = goodsSpuInVo.getGoodsSpuSpec();
        goodsSpuSpec.setSpuId(spuId);
        int goodsSpuSpecInsert = goodsSpuSpecMapper.insert(goodsSpuSpec);
        //3.添加goods_spu_desc
        GoodsSpuDesc goodsSpuDesc = goodsSpuInVo.getGoodsSpuDesc();
        goodsSpuDesc.setSpuId(spuId);
        int goodsSpuDescInsert = goodsSpuDescMapper.insert(goodsSpuDesc);
        if(goodsSpuInsert<1||goodsSpuSpecInsert<1||goodsSpuDescInsert<1){
            logger.error("添加goods_spu和goods_spu_spec和goods_spu_desc失败");
            throw new RuntimeException("添加goods_spu和goods_spu_spec和goods_spu_desc失败");
        }
        return spuId;
    }

    /**
     * 添加goods_sku和goods_sku_spec_value,返回goodsSkuId
     * @param inVo
     * @param spuId
     * @return goodsSkuId
     */
    public Long addGoodsSkuAndGoodsSkuSpecValue(GoodsSkuInVo inVo,Long spuId){
        //1.添加goods_sku
        DecimalFormat mFormat = new DecimalFormat("10000000");//确定格式，把1转换为001
        Long skuTotal = goodsSkuMapper.countTotal();
        String skuCode = mFormat.format(skuTotal + 1);
        inVo.setSkuCode(skuCode);
        inVo.setSpuId(spuId);
        inVo.setIsDeleted(GoodsSku.GOODS_SKU_NO_DELETED);
        inVo.setStatus(GoodsSku.STATUS_XJ);//商品默认为下架状态  审核通过后上架
        inVo.setAuditStatus(-1);//新增商品默认审核状态为新增待审核
        if(null!=inVo.getActId()&&inVo.getActId()==48){//参加万达会员优惠券活动的商品不需要审核
            inVo.setStatus(GoodsSku.STATUS_SJ);
            inVo.setAuditStatus(2);
        }
        if(null==inVo.getUseSkuDetail()){
            inVo.setUseSkuDetail(0);
        }
        if(null==inVo.getShowType()){
            inVo.setShowType(0);
        }
        int goodsSkuInsert = goodsSkuMapper.insert(inVo);
        Long goodsSkuId = inVo.getId();

        this.setGoodsSkuTop(goodsSkuId);//新增商品排在最前面
        //2.添加goods_sku_spec_value
        GoodsSkuSpecValue goodsSkuSpecValue = inVo.getGoodsSkuSpecValue();
        goodsSkuSpecValue.setSkuId(goodsSkuId);
        int goodsSkuSpecInsert = goodsSkuSpecValueMapper.insert(goodsSkuSpecValue);
        if(goodsSkuInsert<1||goodsSkuSpecInsert<1){
            logger.error("添加goods_sku和goods_sku_spec_value失败");
            throw new RuntimeException("添加goods_sku和goods_sku_spec_value失败");
        }
        return goodsSkuId;
    }

    /**
     * 添加goods_sku和goods_sku_spec_value和delivery_template和delivery_template_shop和delivery_method,返回goodsSkuId
     * @param inVo
     * @param spuId
     * @return goodsSkuId
     */
    public Long addPlatformGoodsSkuAndGoodsSkuSpecValue(GoodsSkuInVo inVo,Long spuId){
        //1.添加goods_sku
        DecimalFormat mFormat = new DecimalFormat("10000000");//确定格式，把1转换为001
        Long skuTotal = goodsSkuMapper.countTotal();
        String skuCode = mFormat.format(skuTotal + 1);
        inVo.setSkuCode(skuCode);
        inVo.setSpuId(spuId);
        inVo.setIsDeleted(GoodsSku.GOODS_SKU_NO_DELETED);
        inVo.setStatus(GoodsSku.STATUS_SJ);
        int goodsSkuInsert = goodsSkuMapper.insert(inVo);
        Long goodsSkuId = inVo.getId();
        //2.添加goods_sku_spec_value
        GoodsSkuSpecValue goodsSkuSpecValue = inVo.getGoodsSkuSpecValue();
        goodsSkuSpecValue.setSkuId(goodsSkuId);
        int goodsSkuSpecInsert = goodsSkuSpecValueMapper.insert(goodsSkuSpecValue);
        //3.添加delivery_template
        DeliveryTemplateInVo deliveryTemplateInVo = inVo.getDeliveryTemplateInVo();//运费模板入参
        if(deliveryTemplateInVo!=null&&deliveryTemplateInVo.getId()==null){
            DeliveryTemplate deliveryTemplate = new DeliveryTemplate();
            BeanUtils.copyProperties(deliveryTemplateInVo, deliveryTemplate);
            int deliveryTemplateInsert = deliveryTemplateMapper.insert(deliveryTemplate);
            //4.添加delivery_method
            List<DeliveryMethod> deliveryMethods = deliveryTemplateInVo.getDeliveryMethods();//运费模板详细信息
            this.setDeliveryMethodList(deliveryMethods);//按照大闸蟹的配送范围来搞,后期扩展这个地方要修改
            for (DeliveryMethod deliveryMethod : deliveryMethods) {
                deliveryMethod.setDeliveryTemplateId(deliveryTemplate.getId());
                int deliveryMethodInsert = deliveryMethodMapper.insert(deliveryMethod);
            }
            //5.添加delivery_template_shop
            DeliveryTemplateShop deliveryTemplateShop = new DeliveryTemplateShop();
            deliveryTemplateShop.setDeliveryTemplateId(deliveryTemplate.getId());
            deliveryTemplateShop.setShopId(deliveryTemplateInVo.getShopId());
            int deliveryTemplateShopInsert = deliveryTemplateShopMapper.insert(deliveryTemplateShop);
            deliveryTemplateInVo.setId(deliveryTemplate.getId());
        }
        GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
        goodsSkuInVo.setId(goodsSkuId);
        goodsSkuInVo.setDeliveryTemplateId(deliveryTemplateInVo.getId());
        int goodsSkuUpdate = goodsSkuMapper.updateByPrimaryKeySelective(goodsSkuInVo);
        if(goodsSkuInsert<1||goodsSkuSpecInsert<1||goodsSkuUpdate<1){
            logger.error("添加goods_sku和goods_sku_spec_value失败");
            throw new RuntimeException("添加goods_sku和goods_sku_spec_value失败");
        }
        return goodsSkuId;
    }

    /**
     * 添加商品支持的销售点
     * @param list
     * @param inVo
     */
    public void setSalePointOutsByGoodsSkuOutList(List<GoodsSkuOut> list,GoodsSkuInVo inVo){
        for (GoodsSkuOut goodsSkuOut : list) {
            if(goodsSkuOut.getSendType()==GoodsSku.SEND_TYPE_ALL||goodsSkuOut.getSendType()==GoodsSku.SEND_TYPE_MDZT){
                SalePointInVo salePointInVo = new SalePointInVo();
                salePointInVo.setGoodsSkuId(goodsSkuOut.getId());
                salePointInVo.setLocationX(inVo.getLocationX());
                salePointInVo.setLocationY(inVo.getLocationY());
                List<SalePointOut> salePointOuts = salePointMapper.listSalepointByGoodsSkuId(salePointInVo);
                goodsSkuOut.setSalePointOuts(salePointOuts);
            }
        }
    }

    /**
     * 添加销售点对应的图片组
     * @param salePointOuts
     */
    public void setSalepointTopPicsBySalePointOutList(List<SalePointOut> salePointOuts){
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
    }

    /**
     * 更新goods_spu和goods_spu_spec和goods_spu_desc
     * @param goodsSpuInVo
     */
    public Integer updateGoodsSpuAndGoodsSpuDesc(GoodsSpuInVo goodsSpuInVo){
        //1.更新goods_spu和goods_spu_spec和goods_spu_desc
        int goodsSpuUpdate = goodsSpuMapper.updateByPrimaryKeySelective(goodsSpuInVo);
        //2.更新goods_spu_spec
        GoodsSpuSpec goodsSpuSpec = goodsSpuInVo.getGoodsSpuSpec();
        goodsSpuSpec.setSpuId(goodsSpuInVo.getId());
        int goodsSpuSpecUpdate = goodsSpuSpecMapper.updateByPrimaryKeySelective(goodsSpuSpec);
        //3.更新goods_spu_desc
        GoodsSpuDesc goodsSpuDesc = goodsSpuInVo.getGoodsSpuDesc();
        int goodsSpuDescUpdate = 0;
        if(goodsSpuDesc!=null&&goodsSpuDesc.getId()==null){
            //3.添加goods_spu_desc
            goodsSpuDesc.setSpuId(goodsSpuInVo.getId());
            goodsSpuDescUpdate = goodsSpuDescMapper.insert(goodsSpuDesc);
        }else {
            goodsSpuDescUpdate = goodsSpuDescMapper.updateByPrimaryKeySelective(goodsSpuDesc);
        }
        if(goodsSpuUpdate<1||goodsSpuSpecUpdate<1||goodsSpuDescUpdate<1){
            logger.error("更新goods_spu和goods_spu_spec和goods_spu_desc失败");
            throw new RuntimeException("更新goods_spu和goods_spu_spec和goods_spu_desc失败");
        }
        return goodsSpuUpdate;
    }

    /**
     * 更新goods_sku和goods_sku_spec_value
     * @param inVo
     * @return goodsSkuId
     */
    public Integer updateGoodsSkuAndGoodsSkuSpecValue(GoodsSkuInVo inVo){
        //1.更新goods_sku
        inVo.setUpdateTime(new Date());
        int goodsSkuUpdate = goodsSkuMapper.updateByPrimaryKeySelective(inVo);
        //2.更新goods_sku_spec_value
        GoodsSkuSpecValue goodsSkuSpecValue = inVo.getGoodsSkuSpecValue();
        int goodsSkuSpecUpdate = goodsSkuSpecValueMapper.updateByPrimaryKeySelective(goodsSkuSpecValue);
        if(goodsSkuUpdate<1||goodsSkuSpecUpdate<1){
            logger.error("更新goods_sku和goods_sku_spec_value失败");
            throw new RuntimeException("更新goods_sku和goods_sku_spec_value失败");
        }
        return goodsSkuUpdate;
    }

    /**
     * 校验商品是否需要审核
     * @return
     */
    public boolean chenkIsAudit(GoodsSkuInVo inVo,GoodsSku goodsSku){
        //校验商品图片是否修改
        String skuPic = goodsSku.getSkuPic();
        if(inVo.getSkuPic()==null){
            if(skuPic!=null){
                return true;
            }
        }else{
            if(!inVo.getSkuPic().equals(skuPic)){
               return true;
            }
        }
        String goodsSkuPics = goodsSku.getGoodsSkuPics();//商品图片
        String goodsSkuPicsIn = inVo.getGoodsSkuPics();
        if(goodsSkuPicsIn==null){
           if(goodsSkuPics!=null){
               return true;
           }
        }else{
            if(goodsSkuPics==null){
                return true;
            }
            String[] pics = goodsSkuPics.split(",");
            String[] picsIn = goodsSkuPicsIn.split(",");
            if(pics.length!=picsIn.length){
                return true;
            }
            for(String pic:pics){
                if(!goodsSkuPicsIn.contains(pic)){
                    return true;
                }
            }
            for(String pic:picsIn){
                if(!goodsSkuPics.contains(pic)){
                    return true;
                }
            }
        }
        //校验单买价
        BigDecimal singlyPrice = goodsSku.getSinglyPrice();//单买价
        if(inVo.getSinglyPrice()==null){
            if(singlyPrice!=null){
                return true;
            }
        }else{
            if(singlyPrice==null){
                return true;
            }else{
                if(singlyPrice.doubleValue()!=inVo.getSinglyPrice().doubleValue()){
                    return true;
                }
            }
        }
        //校验购买价
        BigDecimal sellPrice = goodsSku.getSellPrice();//购买价
        if(inVo.getSellPrice()==null){
            if(sellPrice!=null){
                return true;
            }
        }else{
            if(sellPrice==null){
                return true;
            }else{
                if(sellPrice.doubleValue()!=inVo.getSellPrice().doubleValue()){
                    return true;
                }
            }
        }
        //校验类目
        GoodsSpuInVo goodsSpuInVo = inVo.getGoodsSpuInVo();
        GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(goodsSpuInVo.getId());
        if(goodsSpuInVo.getCategoryId()==null){
            if(goodsSpu.getCategoryId()!=null){
                return true;
            }
        }else{
           if(goodsSpu.getCategoryId()==null){
               return true;
           } else{
               if(goodsSpu.getCategoryId().longValue()!=goodsSpuInVo.getCategoryId().longValue()){
                   return true;
               }
           }
        }

        //校验描述图片
        String contentIn = goodsSpuInVo.getGoodsSpuDesc().getContent();
        GoodsSpuDesc goodsSpuDesc = goodsSpuDescMapper.selectBySpuId(goodsSpu.getId());
        if(goodsSpuDesc==null){
            if(contentIn!=null){
                List<String> contentPics = PicUtil.getImg(contentIn);
                if(contentPics!=null&&contentPics.size()>0){
                    return true;
                }
            }
        }else{
            String content = goodsSpuDesc.getContent();
            if(contentIn==null){
                return true;
            }else{
                List<String> contentPicsIn = PicUtil.getImg(contentIn);
                List<String> contentPics = PicUtil.getImg(content);
                if(contentPicsIn.size()!=contentPics.size()){
                    return true;
                }
                if(contentPicsIn.size()>0&&contentPics.size()>0){
                    for(String pic:contentPicsIn){
                        boolean flag=false;
                        for(String picIn:contentPicsIn){
                            if(pic.equals(picIn)){
                                flag=true;
                                break;
                            }
                        }
                        if(!flag){
                            return true;
                        }
                    }
                }
            }
        }

        //校验活动和规则
        List<ActGoodsSku> actGoodsSkus = actGoodsSkuMapper.selectListBySkuId(inVo.getId());//活动
        List<ActGoodsSkuInVo> actGoodsSkuInVos = inVo.getActGoodsSkuInVos();
        if(actGoodsSkus==null){
            if(actGoodsSkuInVos!=null){
                return true;
            }
        }else{
            if(actGoodsSkuInVos==null){
                return true;
            }else{
                int unDeleteCount = 0;
                for(ActGoodsSkuInVo actGoodsSkuInVo:actGoodsSkuInVos){
                    if(actGoodsSkuInVo.getIsDeleted()==0){
                        unDeleteCount++;
                    }
                }
                if(unDeleteCount>actGoodsSkus.size()){
                    return true;
                }
            }
        }
        Map<Long,GoodsPromotionRules> rulesMap = new HashMap<>();
        if(actGoodsSkus!=null){
            List<Long> ruleIds = new ArrayList<>();
            for(ActGoodsSku actGoodsSku:actGoodsSkus){
                ruleIds.add(actGoodsSku.getGoodsPrId());
            }
            List<GoodsPromotionRules>  goodsPromotionRulesList = goodsPromotionRulesMapper.selectByIds(ruleIds);
            for(GoodsPromotionRules rules:goodsPromotionRulesList){
                rulesMap.put(rules.getId(),rules);
            }
        }

        for (ActGoodsSkuInVo actGoodsSkuInVo : actGoodsSkuInVos) {
            GoodsPromotionRules goodsPromotionRules = actGoodsSkuInVo.getGoodsPromotionRules();
            if(goodsPromotionRules.getId()==null){
                return true;
            }else{
                GoodsPromotionRules existGoodsPrmotionRules = rulesMap.get(goodsPromotionRules.getId());
                if(existGoodsPrmotionRules==null){
                    return true;
                }else{
                    if(goodsPromotionRules.getActAmount().doubleValue()!=existGoodsPrmotionRules.getActAmount().doubleValue()){
                        return true;
                    }
                }
            }
        }

        if(goodsSku.getAuditStatus()!=2){
            return true;
        }

        return false;
    }

    @Override
    public List<GoodsSku> ListByshopId(Long shopId) {
        return goodsSkuMapper.listByShopId(shopId);
    }

    /**
     * 按照大闸蟹的配送范围来搞,后期扩展这个地方要修改
     * @param deliveryMethods
     */
    public void setDeliveryMethodList(List<DeliveryMethod> deliveryMethods){
        //List<String> list = new ArrayList<String>();
        //list.add("1-1|1-2|2-3|2-4|3-5|3-6|3-7|3-8|3-9|3-10|3-11|3-12|3-13|3-14|3-15|18-186|18-187|18-188|18-189|18-190|18-191|18-192|18-193|18-194|18-195|18-196|18-197|18-198|18-199|4-16|4-17|4-18|4-19|4-20|4-21|4-22|4-23|4-24|4-25|4-26|27-294|27-295|27-296|27-297|27-298|27-299|27-300|27-301|27-302|27-303|9-75|9-76|11-90|11-91|11-92|11-93|11-94|11-95|11-96|11-97|11-98|11-99|11-100|10-82|10-77|10-84|10-78|10-85|10-86|10-80|10-87|10-81|10-88|22-239|22-240|19-200|19-201|19-202|19-203|19-204|19-205|19-206|19-207|19-208|19-209|19-210|19-211|19-212|19-213|19-214|19-215|19-216|19-217|19-218|19-219|19-220|28-304|28-305|28-306|28-307|28-308|28-309|28-310|28-311|28-312|28-313|28-314|28-315|28-316|28-317|20-221|20-222|20-223|20-224|20-225|20-226|20-227|20-228|20-229|20-230|20-231|20-232|20-233|20-234|24-262|24-263|24-264|24-265|24-266|24-267|24-268|24-269|24-270");
        //list.add("6-39|6-40|6-41|6-42|6-43|6-44|6-45|6-46|6-47|6-48|6-49|6-50|6-51|6-52|5-27|5-28|5-29|5-30|5-31|5-32|5-33|5-34|5-35|5-36|5-37|5-38|23-241|23-242|23-243|23-244|23-245|23-246|23-247|23-248|23-249|23-250|23-251|23-252|23-253|23-254|23-255|23-256|23-257|23-258|23-259|23-260|23-261|12-101|12-102|12-103|12-104|12-105|12-106|12-107|12-108|12-109|12-110|12-111|12-112|12-113|12-114|12-115|12-116|13-117|13-118|13-119|13-120|13-121|13-122|13-123|13-124|13-125|21-235|21-236|29-318|29-319|29-320|29-321|29-322|29-323|29-324|29-325|25-271|25-272|25-273|25-274|25-275|25-276|25-277|25-278|25-279|25-280|25-281|25-282|25-283|25-284|25-285|25-286|8-62|8-63|8-64|8-65|8-66|8-67|8-68|8-69|8-70|8-71|8-72|8-73|8-74");
        //list.add("7-53|7-52|7-53|7-54|7-55|7-56|7-57|7-58|7-59|7-60|7-61|30-326|30-327|30-328|30-329|30-330|16-154|16-155|16-156|16-157|16-158|16-159|16-160|16-161|16-162|16-163|16-164|16-165|16-166|16-167|16-168|16-169|16-170|16-171|17-172|17-173|17-174|17-175|17-176|17-177|17-178|17-179|17-180|17-181|17-182|17-183|17-184|17-185|14-126|14-127|14-128|14-129|14-130|14-131|14-132|14-133|14-134|14-135|14-136|10-89|10-83|10-79|15-137|15-138|15-139|15-140|15-141|15-142|15-143|15-144|15-145|15-146|15-147|15-148|15-149|15-150|15-151|15-152|15-153|31-331|31-332|31-333|31-334|31-335|31-336|31-337|31-338|31-339|31-340|31-342|31-343|31-344|31-345|26-287|26-288|26-289|26-290|26-291|26-292|26-293");
        for (DeliveryMethod deliveryMethod : deliveryMethods) {
            deliveryMethod.setRegion("1-1|1-2|2-3|2-4|3-5|3-6|3-7|3-8|3-9|3-10|3-11|3-12|3-13|3-14|3-15|18-186|18-187|18-188|18-189|18-190|18-191|18-192|18-193|18-194|" +
                    "18-195|18-196|18-197|18-198|18-199|4-16|4-17|4-18|4-19|4-20|4-21|4-22|4-23|4-24|4-25|4-26|27-294|27-295|27-296|27-297|27-298|27-299|27-300|27-301|" +
                    "27-302|27-303|9-75|9-76|11-90|11-91|11-92|11-93|11-94|11-95|11-96|11-97|11-98|11-99|11-100|10-82|10-77|10-84|10-78|10-85|10-86|10-80|10-87|10-81|" +
                    "10-88|22-239|22-240|19-200|19-201|19-202|19-203|19-204|19-205|19-206|19-207|19-208|19-209|19-210|19-211|19-212|19-213|19-214|19-215|19-216|19-217|" +
                    "19-218|19-219|19-220|28-304|28-305|28-306|28-307|28-308|28-309|28-310|28-311|28-312|28-313|28-314|28-315|28-316|28-317|20-221|20-222|20-223|20-224|" +
                    "20-225|20-226|20-227|20-228|20-229|20-230|20-231|20-232|20-233|20-234|24-262|24-263|24-264|24-265|24-266|24-267|24-268|24-269|24-270|6-39|6-40|6-41|" +
                    "6-42|6-43|6-44|6-45|6-46|6-47|6-48|6-49|6-50|6-51|6-52|5-27|5-28|5-29|5-30|5-31|5-32|5-33|5-34|5-35|5-36|5-37|5-38|23-241|23-242|23-243|23-244|23-245|" +
                    "23-246|23-247|23-248|23-249|23-250|23-251|23-252|23-253|23-254|23-255|23-256|23-257|23-258|23-259|23-260|23-261|12-101|12-102|12-103|12-104|12-105|" +
                    "12-106|12-107|12-108|12-109|12-110|12-111|12-112|12-113|12-114|12-115|12-116|13-117|13-118|13-119|13-120|13-121|13-122|13-123|13-124|13-125|21-235|" +
                    "21-236|29-318|29-319|29-320|29-321|29-322|29-323|29-324|29-325|25-271|25-272|25-273|25-274|25-275|25-276|25-277|25-278|25-279|25-280|25-281|25-282|" +
                    "25-283|25-284|25-285|25-286|8-62|8-63|8-64|8-65|8-66|8-67|8-68|8-69|8-70|8-71|8-72|8-73|8-74|7-53|7-52|7-53|7-54|7-55|7-56|7-57|7-58|7-59|7-60|7-61|" +
                    "30-326|30-327|30-328|30-329|30-330|16-154|16-155|16-156|16-157|16-158|16-159|16-160|16-161|16-162|16-163|16-164|16-165|16-166|16-167|16-168|16-169|" +
                    "16-170|16-171|17-172|17-173|17-174|17-175|17-176|17-177|17-178|17-179|17-180|17-181|17-182|17-183|17-184|17-185|14-126|14-127|14-128|14-129|14-130|" +
                    "14-131|14-132|14-133|14-134|14-135|14-136|10-89|10-83|10-79|15-137|15-138|15-139|15-140|15-141|15-142|15-143|15-144|15-145|15-146|15-147|15-148|" +
                    "15-149|15-150|15-151|15-152|15-153|31-331|31-332|31-333|31-334|31-335|31-336|31-337|31-338|31-339|31-340|31-342|31-343|31-344|31-345|26-287|26-288|" +
                    "26-289|26-290|26-291|26-292|26-293");
        }
    }

    @Override
    public Integer setGoodsSkuTop(Long id) {
        GoodsSku goodsSku=goodsSkuMapper.selectByPrimaryKey(id);
        GoodsSkuInVo goodsSkuInVo=new GoodsSkuInVo();
        goodsSkuInVo.setId(id);
        goodsSkuInVo.setShopId(goodsSku.getShopId());
        if(goodsSku!=null&&goodsSku.getSortNum()!=null&&goodsSku.getSortNum()!=0){//首次添加的商品排序值为0，此时其他商品全部后移一位
            goodsSkuInVo.setSortNum(goodsSku.getSortNum());
        }
        goodsSkuMapper.updateSortNumBackOff(goodsSkuInVo);//排在前面的商品排序后移一位

        goodsSkuInVo.setSortNum(1);
        Integer re= goodsSkuMapper.updateByPrimaryKeySelective(goodsSkuInVo);//该商品排在第一位
        return re;
    }

    @Override
    public GoodsSkuRecommendOut dailyRecommend(GoodsSkuRecommendInVo goodsSkuRecommendInVo) {
        List<GoodsSkuRecommendOut> goodsSkuRecommendOutList = goodsSkuMapper.dailyRecommend(goodsSkuRecommendInVo);
        if (goodsSkuRecommendOutList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randRecommendIndex = random.nextInt(goodsSkuRecommendOutList.size());
        return goodsSkuRecommendOutList.get(randRecommendIndex);
    }

    @Transactional
    @Override
    public Integer auditGoodsSku(AduitGoodsSkuInVo inVo) {
        List<Long> skuIds = inVo.getIds();
        List<GoodsSku> goodsSkus = new ArrayList<>();
        List<GoodsSkuAuditLog> auditLogs = new ArrayList<>();
        List<GoodsSkuRejectLog> rejectLogs = new ArrayList<>();
        if(skuIds==null||skuIds.size()<=0){
            return -1;
        }
        for(Long skuId:skuIds){
            GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(skuId);
            if(goodsSku==null){
                return -1;
            }
            GoodsSku updateGoodsSku = new GoodsSku();
            updateGoodsSku.setUpdateTime(new Date());
            updateGoodsSku.setId(skuId);
            updateGoodsSku.setStatus(goodsSku.getStatus());
            Integer auditStatus = inVo.getAduitStatus();
            if(auditStatus!=2&&auditStatus!=3){
                return -1;
            }
            updateGoodsSku.setAuditStatus(auditStatus);
            GoodsSkuAuditLog auditLog = new GoodsSkuAuditLog();
            auditLog.setSkuId(goodsSku.getId());
            auditLog.setCreateTime(new Date());
            auditLog.setIsDeleted(0);
            auditLog.setUpdateTime(new Date());
            if(auditStatus==2){
                auditLog.setContent("审核通过");
                updateGoodsSku.setStatus(GoodsSku.STATUS_SJ);
//                if(goodsSku.getAuditStatus()==-1){//新增待审核  需将商品直接上架
//                    updateGoodsSku.setStatus(GoodsSku.STATUS_SJ);
//                }else{
//                    updateGoodsSku.setStatus(GoodsSku.STATUS_XJ);
//                }
            }else{
                auditLog.setContent("审核驳回");
                GoodsSkuRejectLog rejectLog = new GoodsSkuRejectLog();
                rejectLog.setCreateTime(new Date());
                rejectLog.setIsDeleted(0);
                rejectLog.setRejectPic(inVo.getRejectPics());
                rejectLog.setRejectResult(inVo.getRejectResult());
                rejectLog.setSkuId(skuId);
                rejectLog.setSmsSend(inVo.getSmsIsSend());
                rejectLog.setUpdateTime(new Date());
                rejectLogs.add(rejectLog);
                if(inVo.getSmsIsSend()==1){
                    ShopCashier adminCashier = shopCashierMapper.adminByShopId(goodsSku.getShopId());
                    if(adminCashier==null){
                        return 1;
                    }
                    User user = userMapper.selectByPrimaryKey(adminCashier.getCashierId());
                    SmsSendInVo smsSendInVo = new SmsSendInVo();
                    smsSendInVo.setUserId(user.getId());
                    smsSendInVo.setUserName(user.getUserName());
                    smsSendInVo.setSmsType(SmsSend.SMS_TYPE_GOODS_REJECT);
                    smsSendInVo.setShopMobile(user.getMobile());
                    String msg = MessageFormat.format(Constants.WINNER_GOODS_SKU_REJECT, goodsSku.getSkuName());
                    Map<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("userCode", Constants.WINNER_LOOK_USER_NAME);
                    paramMap.put("userPass", Constants.WINNER_LOOK_PASSWORD);
                    paramMap.put("DesNo", user.getMobile());
                    paramMap.put("Msg", msg);
                    logger.info("短信内容"+msg);
                    try {
                        String s = HttpRequestUtil.httpsPost(Constants.WINNER_LOOK_HTTPS_SEND_MESSAGE_URL, paramMap);
                        Document dom= DocumentHelper.parseText(s);
                        Element root=dom.getRootElement();
                        String ret=root.getText();
                        if (ret.length() > 10) {//短信发送成功
                            smsSendInVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
                            smsSendInVo.setRemark("OK");
                        } else {
                            smsSendInVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
                            smsSendInVo.setRemark(ret);
                        }
                        smsSendInVo.setSmsContent(msg);
                        smsSendService.create(smsSendInVo);
                    } catch (Exception e) {
                        logger.error("商品驳回短信为发送，异常信息："+e.toString());
                    }
                }
            }
            goodsSkus.add(updateGoodsSku);
            auditLogs.add(auditLog);
        }

        if(auditLogs.size()>0){
            goodsSkuAuditLogMapper.batchInsert(auditLogs);
        }
        if(goodsSkus.size()>0){
            goodsSkuMapper.batchUpdateStatusByKey(goodsSkus);
        }
        if(rejectLogs.size()>0){
            goodsSkuRejectLogMapper.batchInsert(rejectLogs);
        }
        for(GoodsSku sku:goodsSkus){
            Long skuId = sku.getId();
            this.setGoodsSkuTop(skuId);
        }
        return 1;
    }

    @Override
    public Pager<PageGoodsSkuVo> pageGoodsSkuForAudit(PageGoodsSkuCommand command) {
        Pager<PageGoodsSkuVo> result = new Pager<PageGoodsSkuVo>();
        int total = goodsSkuMapper.pageGoodsSkuForAuditTotal(command.getStartTime(),command.getEndTime(),command.getSkuName());
        if(total>0){
            int page = command.getPage();
            int rows = command.getRows();
            List<PageGoodsSkuVo> list = goodsSkuMapper.pageGoodsSkuForAudit(command.getStartTime(),command.getEndTime(),command.getSkuName(),(page-1)*rows,rows);
            List<Long> skuIds = new ArrayList<>();
            for(PageGoodsSkuVo vo:list){
                if(vo.getShopType()==null)continue;
                if(vo.getShopType().startsWith("1")){
                    vo.setShopType("正餐");
                }
                if(vo.getShopType().startsWith("48")){
                    vo.setShopType("轻餐");
                }
                skuIds.add(vo.getId());
                vo.setGoodsSkuPicsList(queryImgByIds(vo.getGoodsSkuPics()));
            }
            List<GoodsPromotionRules> rules = goodsPromotionRulesMapper.selectBySkuIds(skuIds);
            if(rules!=null){
                for(PageGoodsSkuVo vo:list){
                    for(GoodsPromotionRules rule:rules){
                        if(vo.getId().longValue()==rule.getGoodsSkuId().longValue()&&rule.getRuleType().intValue()==4){
                            vo.setActPrice(rule.getActAmount());
                        }
                        if(vo.getId().longValue()==rule.getGoodsSkuId().longValue()&&rule.getRuleType().intValue()==5){
                            vo.setSpikePrice(rule.getActAmount());
                        }
                    }
                }
            }
            result.setList(list);
            logger.info("列表详情："+list);
        }else{
            result.setList(new ArrayList<PageGoodsSkuVo>());
        }
        result.setPage(command.getPage());
        result.setTotal(total);
        return result;
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

    @Override
    public int batchDeleteGoodsSku(BatchDeleteGoodsSkuCommand command) {
        List<Long> skuIds = command.getSkuIds();
        if(skuIds==null||skuIds.size()<=0){
            throw new RuntimeException("请选择需要删除的商品");
        }
        try {
            goodsSkuMapper.batchDeleteGoodsSku(skuIds,new Date());
        }catch (Exception e){
            return -1;
        }
        return 1;
    }

    @Override
    public GoodsSkuInVo updateForVip(GoodsSkuInVo inVo) {
        if(inVo.getActGoodsSkuInVos()==null||inVo.getActGoodsSkuInVos().size()<1){
            return inVo;
        }
        for(ActGoodsSkuInVo actGoodsSkuInVo :inVo.getActGoodsSkuInVos()){
            if(actGoodsSkuInVo.getActId()!=null&&actGoodsSkuInVo.getActId()==48){
                inVo.setSellPrice(actGoodsSkuInVo.getGoodsPromotionRules().getJianAmount());//优惠券的单买价格设为面额
                actGoodsSkuInVo.getGoodsPromotionRules().setActAmount((actGoodsSkuInVo.getGoodsPromotionRules().getJianAmount().multiply(new BigDecimal(0.1))));//优惠券的会员价设为面额的10%
            }
        }
        return inVo;
    }
}
