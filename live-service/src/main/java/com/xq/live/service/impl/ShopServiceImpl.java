package com.xq.live.service.impl;

import com.xq.live.common.*;
import com.xq.live.config.ConstantsConfig;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ShopCategoryService;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.ShopCategoryUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商家sevice实现类
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 **/
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private ShopEnterMapper shopEnterMapper;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SoShopLogMapper soShopLogMapper;

    @Autowired
    private SoWriteOffMapper soWriteOffMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderWriteOffMapper orderWriteOffMapper;

    @Autowired
    private ShopTopPicMapper shopTopPicMapper;

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Autowired
    private ShopPromotionRulesMapper shopPromotionRulesMapper;

    @Autowired
    private ShopCategoryMapper shopCategoryMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ConstantsConfig constantsConfig;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ShopZoneItemMapper shopZoneItemMapper;

    @Autowired
    private GoodsSkuDetailMapper goodsSkuDetailMapper;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Override
    public Shop getShopById(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public ShopOut findShopOutById(Long id) {
        ShopOut out = shopMapper.findShopOutById(id);
        if (out != null) {
            List<ShopTopPicOut> picOutList = shopTopPicMapper.selectByShopId(out.getId());
            List<Pair<String, String>> picList = new ArrayList<>();
            if (picOutList != null && picOutList.size() > 0) {
                for (ShopTopPicOut picOut : picOutList) {
                    picList.add(new Pair<String, String>(picOut.getAttachment().getSmallPicUrl(), picOut.getAttachment().getPicUrl()));    //小图和大图url
                }
            }
            String shopCate = out.getShopCate();
            if(shopCate!=null){
                if(StringUtils.isNotEmpty(shopCate)){
                    String[] regoinArr = shopCate.split("\\|");
                    List<String> stringList = Arrays.asList(regoinArr);
                    List<Long> shopCateInvo = new ArrayList<Long>();
                    if(regoinArr != null && regoinArr.length > 0){
                        for (String s : stringList) {
                            String[] sArr = s.split("-");
                            shopCateInvo.add(Long.valueOf(sArr[1]));
                        }
                    }
                    List<ShopCategory> shopCategories = shopCategoryMapper.selectShopCateByList(shopCateInvo);
                    out.setShopCategories(shopCategories);
                }
            }
            out.setShopTopPics(picList);
            if(out.getShopZoneItemId()!=null&&!out.getShopZoneItemId().equals(0L)){
                ShopZoneItem shopZoneItem = shopZoneItemMapper.selectByPrimaryKey(out.getShopZoneItemId());
                out.setShopZoneItem(shopZoneItem);
            }

        }
        return out;
    }

    @Override
    public Long addShop(Shop shop) {
        //保存记录到shop表
        int r = shopMapper.insert(shop);
        if (r < 1) {
            return null;
        }

        //更新user表，升级为商家账号，记录商家id
        User user = new User();
        user.setUserType(User.USER_TYPE_SJ);
        user.setShopId(shop.getId());
        user.setId(shop.getUserId());
        userMapper.updateUserType(user);

        //返回商家主键
        return shop.getId();
    }

    /**
     * 更新一条商家记录
     *
     * @param shop
     * @return
     */
    @Override
    public int updateShop(Shop shop) {
        return shopMapper.updateByPrimaryKeySelective(shop);
    }

    /**
     * 根据id删除一条商家记录
     *
     * @param id
     * @return
     */
    @Override
    public int deleteShopById(Long id) {
        return shopMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Pager<ShopOut> list(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        Integer listTotal = 0;
        if (inVo.getShopCate()!=null){
            String[] cate = inVo.getShopCate().split("\\|");

            for (int i =0;i<cate.length;i++){
                inVo.setShopCate(cate[i]);
                listTotal=shopMapper.listTotal(inVo);
                if (listTotal>5){
                    break;
                }
            }
//            if (listTotal<5){
//                inVo.setShopCate(null);
//            }
        }else {
            listTotal=shopMapper.listTotal(inVo);
        }

        result.setTotal(listTotal);
        if (listTotal != null && listTotal > 0) {
            List<ShopOut> list = shopMapper.list(inVo);
            /**
             * 将用户减免规则加入
             */
            for (ShopOut shopOut : list) {
                /**
                 * 及时读取人气数目
                 */
                String key = "shopPops_" + shopOut.getId().toString();
                Integer pops = redisCache.get(key, Integer.class);
                if(pops!=null){
                    shopOut.setPopNum(pops);
                }
                this.setShopCateName(shopOut);//商家经营品类
                this.selectShopRules(shopOut);//查询商家支持的规则
                this.setGoodsSku(shopOut);//设置推荐菜
                this.actGoodsSkuInfo(shopOut);//设置推荐菜(砍价>普通商品)
            }
            /**
             * 根据综合排序 0 口味 1服务 2 人气
             *//*
            if (inVo != null && inVo.getBrowSort()!= null &&  inVo.getBrowSort() == Shop.BROW_SORT_POP) {
                Collections.sort(list);
            }*/
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    private void setShopCateName(ShopOut shopOut){
//        String[] cate = shopOut.getShopCate().split("\\|");
//        String[] cates = new String[1];
//        cates=cate[0].split("-");
//        Integer[] shopcate = new Integer[cates.length];
//        for (int i =0;i<cates.length;i++){
//            shopcate[i]=Integer.valueOf(cates[i]);
//        }
//        ShopCategory shopCategory = shopCategoryMapper.selectByPrimaryKey(shopcate[shopcate.length-1].longValue());
//        shopOut.setShopCateName(shopCategory.getCategoryName());

        // 获取商家类目
        List<ShopCategory> shopCategoryList = shopCategoryService.list();
        shopOut.setShopCateName(ShopCategoryUtils.findShopCateMemo(shopCategoryList, shopOut.getShopCate(), false));
    }

    private void actGoodsSkuInfo(ShopOut shopOut){
        GoodsSkuInVo inVo = new GoodsSkuInVo();
        inVo.setShopId(shopOut.getId());
        inVo.setActId(new Long(41));
        List<GoodsSkuOut> list = goodsSkuMapper.selectActList(inVo);
        if (list!=null&&list.size()>0){
            ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
            actGoodsSkuInVo.setSkuId(list.get(0).getId());
            actGoodsSkuInVo.setActId(inVo.getActId());
            ActGoodsSkuOut actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            list.get(0).setActGoodsSkuOut(actGoodsSkuOut);
            shopOut.setGoodsSkuOut(list.get(0));
        }else {
            GoodsSku goodsSku = goodsSkuMapper.selectOneGoodsSkuByShopId(shopOut.getId());
            GoodsSkuOut goodsSkuOut = new GoodsSkuOut();
            if (goodsSku!=null&&goodsSku.getId()!=null){
                BeanUtils.copyProperties(goodsSku, goodsSkuOut);
            }
            shopOut.setGoodsSkuOut(goodsSkuOut);
        }

    }


    @Override
    public List<ShopOut> listHome(ShopInVo inVo) {
            List<ShopOut> list = shopMapper.list(inVo);
            /**
             * 将用户减免规则加入
             */
            for (ShopOut shopOut : list) {
                /**
                 * 及时读取人气数目
                 */
                String key = "shopPops_" + shopOut.getId().toString();
                Integer pops = redisCache.get(key, Integer.class);
                if(pops!=null){
                    shopOut.setPopNum(pops);
                }
                this.selectShopRules(shopOut);//查询商家支持的规则
                this.setGoodsSku(shopOut);//设置推荐菜
        }
        return list;
    }

    @Override
    public Pager<ShopOut> listForChuangXiang(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        List<ShopOut> list = shopMapper.listForChuanXiang(inVo);
        for (ShopOut shopOut : list) {
            /**
             * 及时读取人气数目
             */
            String key = "shopPops_" + shopOut.getId().toString();
            Integer pops = redisCache.get(key, Integer.class);
            if(pops!=null){
                shopOut.setPopNum(pops);
            }
            this.selectShopRules(shopOut);//查询商家支持的规则
            this.setGoodsSku(shopOut);//设置推荐菜
        }
        result.setTotal(list.size());
        result.setList(list);
        result.setPage(inVo.getPage());
        return result;

    }

    @Override
    public Map<String, List<ShopOut>> listForHomePage(ShopInVo inVo) {
        Map<String, List<ShopOut>> map = new HashMap<String,List<ShopOut>>();
        List<ShopOut> shopOuts = shopMapper.listForChuanXiang(inVo);
        map.put("CX",shopOuts);
        inVo.setBusinessCate("海鲜");
        shopOuts = shopMapper.list(inVo);
        map.put("HX",shopOuts);
        inVo.setBusinessCate("火锅");
        shopOuts = shopMapper.list(inVo);
        map.put("HG",shopOuts);
        inVo.setBusinessCate("烧烤");
        shopOuts = shopMapper.list(inVo);
        map.put("SK",shopOuts);
        inVo.setBusinessCate("西餐");
        shopOuts = shopMapper.list(inVo);
        map.put("XC",shopOuts);
        inVo.setBusinessCate("自助餐");
        shopOuts = shopMapper.list(inVo);
        map.put("ZZ",shopOuts);
        inVo.setBusinessCate("商务");
        shopOuts = shopMapper.list(inVo);
        map.put("SW",shopOuts);
        inVo.setBusinessCate("聚会");
        shopOuts = shopMapper.list(inVo);
        map.put("JH",shopOuts);
        inVo.setBusinessCate("约会");
        shopOuts = shopMapper.list(inVo);
        map.put("YH",shopOuts);
        return map;
    }

    @Override
    public List<ShopOut> top(ShopInVo inVo) {
        return shopMapper.list(inVo);
    }

    @Override
    public Shop detail(ShopInVo inVo) {
        /**
         * 1、查询用户是否存在访问记录
         * 2、记录用户访问日志
         */
        AccessLog accessLog = new AccessLog();
        accessLog.setUserId(inVo.getUserId());
        accessLog.setUserName(inVo.getUserName());
        accessLog.setUserIp(inVo.getUserIp());
        accessLog.setSource(inVo.getSourceType());
        accessLog.setRefId(inVo.getId());
        accessLog.setBizType(AccessLog.BIZ_TYPE_SHOP_VIEW);
        int cnt = accessLogMapper.checkRecordExist(accessLog);
        if (cnt == 0) {
            try {
                int logCnt = accessLogMapper.insert(accessLog);
                /*if (logCnt > 0) {
                    shopMapper.updatePopNum(inVo.getId());  //增加人气数值l
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //3、根据id查询商家信息
        Shop shop = shopMapper.selectByPrimaryKey(inVo.getId());
        return shop;
    }

    @Override
    public Shop getShopByUserId(Long userId) {
        return shopMapper.getShopByUserId(userId);
    }

    @Override
    public ShopOut getShopByCode(String code) {
        ShopOut out = shopMapper.findShopOutByCode(code);
        if (out!=null){
            return out;
        }
        return null;
    }


    /**
     * 生成商家详情二维码图片并上传到腾讯云服务器(有背景图片的二维码)
     * @param out
     * @return
     */
    @Override
    public String uploadQRCodeToCosByInfo(ShopOut out) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator +"ShopInfo"+out.getShopCode() + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/service?flag="+1+"&shopCode="+out.getShopCode();
        //生成logo图片到destPath
        try {
            ShopCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UploadServiceImpl uploadService = new UploadServiceImpl();
        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "shopcode");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "shopcode");
            }
            if (imgUrl!=null){
                break;
            }
            if (i==4){
                break;
            }
        }while (true);
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }
        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }

    /**
     * 生成商家订单二维码图片并上传到腾讯云服务器(有背景图片的二维码)
     * @param out
     * @return
     */
    @Override
    public String uploadQRCodeToCosBySo(ShopOut out) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator +"ShopSoInfo"+ out.getShopCode() + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/service?flag="+2+"&shopCode="+out.getShopCode();
        //生成logo图片到destPath
        try {
            ShopCodeBySoUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UploadServiceImpl uploadService = new UploadServiceImpl();
        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "shopcode");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "shopcode");
            }
            if (imgUrl!=null){
                break;
            }
            if (i==4){
                break;
            }
        }while (true);

        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }
        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }

    /**
     * 生成商家订单二维码图片并上传到腾讯云服务器(无背景图片的二维码)
     * @param out
     * @return
     */
    @Override
    public String uploadQRCodeToBySo(ShopOut out) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator +"ShopSo"+ out.getShopCode() + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/service?flag="+2+"&shopCode="+out.getShopCode();
        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UploadServiceImpl uploadService = new UploadServiceImpl();
        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "shopcode");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "shopcode");
            }
            if (imgUrl!=null){
                break;
            }
            if (i==4){
                break;
            }
        }while (true);

        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }
        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }

    /**
     * 生成商家详情二维码图片并上传到腾讯云服务器(无背景图片的二维码)
     * @param out
     * @return
     */
    @Override
    public String uploadQRCodeToByInfo(ShopOut out) {
        String imagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static" + File.separator + "images" + File.separator + "logo.jpg";
        String destPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "upload" + File.separator +"Shop"+out.getShopCode() + ".jpg";
        String text = constantsConfig.getDomainXqUrl() + "/service?flag="+1+"&shopCode="+out.getShopCode();
        //生成logo图片到destPath
        try {
            QRCodeUtil.encode(text, imagePath, destPath, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UploadServiceImpl uploadService = new UploadServiceImpl();
        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadService.uploadFileToCos(destPath, "shopcode");
        int i=0;
        do {
            i++;
            if (imgUrl==null){
                imgUrl=uploadService.uploadFileToCos(destPath, "shopcode");
            }
            if (imgUrl!=null){
                break;
            }
            if (i==4){
                break;
            }
        }while (true);
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }
        //删除服务器上临时文件
        uploadService.deleteTempImage(new Triplet<String, String, String>(destPath, null, null));
        return imgUrl;
    }

    @Override
    public List<ActShopByShopIdOut> listForActByShopId(ShopInVo inVo) {
        List<ActShopByShopIdOut> result= shopMapper.listForActByShopId(inVo);
        return result;
    }

    /**
     * 通过shopid和时间查询所有分店数据
     * @param inVo
     * @return
     */
    @Override
    public List<ShopForSubOut> listSubByShopId(ShopInVo inVo) {

        List<ShopForSubOut> shopForSubOut = new ArrayList<ShopForSubOut>();
        List<Shop> listforshop= shopMapper.selectByParentId(inVo.getId());
        Integer allsolist=0;//所有分店核销总数
        Integer allsoWritelist=0;//所有分店核销总数
        BigDecimal allsoAmount=BigDecimal.ZERO;//所有分店总营业额

        if (listforshop==null||listforshop.size()<1){
            return null;
        }
        for (Shop shop:listforshop){
            SoInVo soInVo= new SoInVo();
            soInVo.setShopId(shop.getId());
            soInVo.setBeginTime(inVo.getBeginTime());
            soInVo.setEndTime(inVo.getEndTime());
            SoShopLog aa = soShopLogMapper.totalAmount(soInVo);//只查询平台代收的商家订单
            SoOut bb = soMapper.totalAmount(soInVo);//只查询已核销的食典券和已核销的砍价券
            BigDecimal a = BigDecimal.ZERO;
            BigDecimal b = BigDecimal.ZERO;
            if(aa!=null){
                a =  aa.getSoAmount().subtract(aa.getServicePrice()).setScale(2, BigDecimal.ROUND_UP);
            }
            if(bb!=null){
                b =  bb.getSoAmount().subtract(bb.getServicePrice()).setScale(2, BigDecimal.ROUND_UP);
            }
            BigDecimal soAmount = a.add(b).setScale(2, BigDecimal.ROUND_UP);//总营业额

            Integer sku=soMapper.skulistTotal(soInVo);//根据shopid获取一段时间内的所有食典券和砍菜券订单数量
            Integer so=soMapper.solistTotal(soInVo);//根据shopid获取一段时间内的所有商家订单数量
            Integer all=sku+so;//总订单数量

            ShopForSubOut subOut = new ShopForSubOut();
            subOut.setId(shop.getId());
            subOut.setShopName(shop.getShopName());
            subOut.setIsSub(shop.getIsSub());
            subOut.setSolist(all);
            allsolist=allsolist+all;
            subOut.setSoAmount(soAmount);
            allsoAmount=allsoAmount.add(soAmount);
            SoWriteOffInVo soWriteOffInVo= new SoWriteOffInVo();
            soWriteOffInVo.setShopId(shop.getId());
            soWriteOffInVo.setBegainTime(inVo.getBeginTime());
            soWriteOffInVo.setEndTime(inVo.getEndTime());
            int total = soWriteOffMapper.listTotal(soWriteOffInVo);//查看所有核销券数的总数量
            subOut.setSoWriteoffList(total);
            allsoWritelist=allsoWritelist+total;
            shopForSubOut.add(subOut);
        }

        shopForSubOut.get(0).setAllsoAmount(allsoAmount);
        shopForSubOut.get(0).setAllsolist(allsolist);
        shopForSubOut.get(0).setAllsoWritelist(allsoWritelist);
        return shopForSubOut;
    }

    @Override
    public List<SubbranchOut> subbranchList(ShopInVo inVo) {
        List<Shop> listforshop= shopMapper.selectByParentId(inVo.getId());
        if (listforshop==null||listforshop.size()<1){
            return null;
        }
        Integer allOrderTotle=0;//所有分店订单总数
        Integer allOrderWriteTotle=0;//所有分店核销总数
        BigDecimal allOrderAmount=BigDecimal.ZERO;//所有分店总营业额
        Map<Map<String,String>,List<Map<String,String>>> mapListMap = new HashMap<>();

        List<SubbranchOut> subbranchOutList = new ArrayList<SubbranchOut>();
        //List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (Shop shop:listforshop){
            OrderInfoInVo infoInVo = new OrderInfoInVo();
            infoInVo.setShopId(shop.getId());
            infoInVo.setBeginTime(inVo.getBeginTime());
            infoInVo.setEndTime(inVo.getEndTime());

            OrderInfoOut orderInfoOut = orderInfoMapper.queryCashAmount(infoInVo);
            OrderInfoOut shopTurnover = orderInfoMapper.queryShopTurnover(infoInVo);
            OrderWriteOffInVo orderWriteOffInVo = new OrderWriteOffInVo();
            orderWriteOffInVo.setShopId(shop.getId());
            orderWriteOffInVo.setBeginTime(inVo.getBeginTime());
            orderWriteOffInVo.setEndTime(inVo.getEndTime());
            Integer orderWriteOffTotal = orderWriteOffMapper.listTotal(orderWriteOffInVo);
            allOrderTotle=allOrderTotle+shopTurnover.getItemTotal();//订单数
            allOrderWriteTotle=allOrderWriteTotle+orderWriteOffTotal;//核销数
            allOrderAmount=allOrderAmount.add(orderInfoOut.getAllPrice());

            SubbranchOut subbranchOut = new SubbranchOut();
            subbranchOut.setShopId(shop.getId());//商家id
            subbranchOut.setShopName(shop.getShopName());//商家名
            subbranchOut.setDuiPrice(orderInfoOut.getDuiPrice());//对账的所有实际营业额
            subbranchOut.setNoDuiPrice(orderInfoOut.getNoDuiPrice());//未对账的所有实际营业额
            subbranchOut.setAllPrice(orderInfoOut.getAllPrice());//所有实际营业额
            subbranchOut.setShopTurnover(shopTurnover.getAllPrice());//所有理论的营业额
            subbranchOut.setOrderTotal(shopTurnover.getItemTotal());//订单的数目
            subbranchOut.setOrderWriteOffTotal(orderWriteOffTotal);//核销票券的数目
            subbranchOutList.add(subbranchOut);
        }
        for (SubbranchOut subbranchOut:subbranchOutList){
            subbranchOut.setAllOrderAmount(allOrderAmount);
            subbranchOut.setAllOrderWriteTotle(allOrderWriteTotle);
            subbranchOut.setAllOrderTotle(allOrderTotle);
        }



        return subbranchOutList;
    }

    @Override
    public List<Map<String, String>> subbranchListV1(ShopInVo inVo) {
        Date beginTime = inVo.getBeginTime();
        Date endTime = inVo.getEndTime();
        List<Shop> listforshop= shopMapper.selectByParentId(inVo.getId());
        if (listforshop==null||listforshop.size()<1){
            return null;
        }
        Integer allOrderWriteTotle=0;//所有分店核销总数
        BigDecimal allOrderAmount=BigDecimal.ZERO;//所有分店总营业额
        List<Map<String, String>> mapList = new ArrayList<>();

        //获取日期前一天
        Date beginTimeBefore = this.getSpecifiedDay(beginTime, -1);
        Date endTimeBefore = this.getSpecifiedDay(endTime, -1);

        for (Shop shop:listforshop){
            //当天日期
            OrderInfoInVo newInVo = new OrderInfoInVo();
            newInVo.setShopId(shop.getId());
            newInVo.setBeginTime(beginTime);
            newInVo.setEndTime(endTime);

            OrderInfoInVo beforeInVo = new OrderInfoInVo();
            beforeInVo.setShopId(shop.getId());
            beforeInVo.setBeginTime(beginTimeBefore);
            beforeInVo.setEndTime(endTimeBefore);

            //查询商家端商家订单营业额
            OrderInfoOut shopOrderTurnover = orderInfoMapper.queryShopOrderTurnover(newInVo);
            //查询商家端商家昨日订单营业额
            OrderInfoOut beforeShopOrderTurnover = orderInfoMapper.queryShopOrderTurnover(beforeInVo);
            OrderWriteOffInVo orderWriteOffInVo = new OrderWriteOffInVo();
            orderWriteOffInVo.setShopId(shop.getId());
            orderWriteOffInVo.setBeginTime(beginTime);
            orderWriteOffInVo.setEndTime(endTime);
            OrderWriteOffInVo orderWriteOffBeforeInVo = new OrderWriteOffInVo();
            orderWriteOffBeforeInVo.setShopId(shop.getId());
            orderWriteOffBeforeInVo.setBeginTime(beginTimeBefore);
            orderWriteOffBeforeInVo.setEndTime(endTimeBefore);
            //查询商家端用户订单营业额和核销数目
            List<OrderWriteOffOut> orderWriteOffOuts = orderWriteOffMapper.selectTotalAmount(orderWriteOffInVo);
            //查询商家端用户昨日订单营业额和核销数目
            List<OrderWriteOffOut> orderWriteOffOutsBefore = orderWriteOffMapper.selectTotalAmount(orderWriteOffBeforeInVo);
            Map<String,String> map = new HashMap<String,String>();
            map.put("shopId", shop.getId().toString());
            map.put("shopName", shop.getShopName().toString());
            map.put("allShopOrderPrice", shopOrderTurnover.getAllShopOrderPrice().toString());//商家端商家订单总营业额
            map.put("beforeAllShopOrderPrice",beforeShopOrderTurnover.getAllShopOrderPrice().toString());//昨日商家端商家订单总营业额
            allOrderAmount.add(shopOrderTurnover.getAllShopOrderPrice());
            if(orderWriteOffOuts!=null&&orderWriteOffOuts.size()>0) {
                OrderWriteOffOut orderWriteOffOut = orderWriteOffOuts.get(0);
                map.put("orderWriteOffTotal", orderWriteOffOut.getTotalItem().toString());//核销票券的数目
                map.put("allRealShopUnitPrice",orderWriteOffOut.getTotalShopPrice().toString());//商家端用户订单总营业额
                map.put("allShopTurnover",orderWriteOffOut.getTotalShopPrice().
                        add(shopOrderTurnover.getAllShopOrderPrice()).toString());//商家总营业额
                OrderWriteOffOut beforeOrderWriteOffOut = orderWriteOffOutsBefore.get(0);
                map.put("beforeOrderWriteOffTotal", beforeOrderWriteOffOut.getTotalItem().toString());//昨日核销票券的数目
                map.put("beforeAllRealShopUnitPrice",beforeOrderWriteOffOut.getTotalShopPrice().toString());//昨日商家端用户订单总营业额
                map.put("beforeAllShopTurnover", beforeOrderWriteOffOut.getTotalShopPrice().
                        add(beforeShopOrderTurnover.getAllShopOrderPrice()).toString());//昨日商家总营业额

                allOrderWriteTotle=allOrderWriteTotle+orderWriteOffOut.getTotalItem();
                allOrderAmount=allOrderAmount.add(orderWriteOffOut.getTotalShopPrice());
            }
            mapList.add(map);
        }
        for (Map<String, String> subbranchOut:mapList){
            subbranchOut.put("allOrderWriteTotle",allOrderWriteTotle.toString());
            subbranchOut.put("allOrderAmount",allOrderAmount.toString());
        }
        return mapList;
    }

    /**
     * 获取指定日期的前(后)n天
     * @param date
     * @return
     */
    private Date getSpecifiedDay(Date date,int n){
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, n);  //设置为前(后)n天
        return calendar.getTime();   //得到前一天的时间
    }

    @Override
    public Pager<ShopOut> listForSkuAllocation(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        int listTotal = shopMapper.listTotalForSkuAllocation(inVo);
        if (listTotal > 0) {
            List<ShopOut> list = shopMapper.listForSkuAllocation(inVo);
            for (ShopOut shopOut : list) {
                SkuInVo vo = new SkuInVo();
                vo.setSkuType(Sku.SKU_TYPE_DHC);
                vo.setShopId(shopOut.getId());
                vo.setIsDeleted(Sku.SKU_NO_DELETED);
                List<SkuForTscOut> listDhc = skuMapper.queryDhcList(vo);
                shopOut.setSkuForTscOuts(listDhc);
                /**
                 * 及时读取人气数目
                 */
                String key = "shopPops_" + shopOut.getId().toString();
                Integer pops = redisCache.get(key, Integer.class);
                if(pops!=null){
                    shopOut.setPopNum(pops);
                }
                this.selectShopRules(shopOut);//查询商家支持的规则
                this.setGoodsSku(shopOut);//设置推荐菜
            }
            result.setList(list);
            }
        result.setTotal(listTotal);
        result.setPage(inVo.getPage());
        return result;
    }

    /**
     * 根据shopId去查询商家支持的规则
     * @param shopOut
     * @return
     */
    @Override
    public void selectShopRules(ShopOut shopOut){
        List<String> stringList = new ArrayList<String>();
        List<ShopPromotionRulesOut> shopPromotionRulesOuts = shopPromotionRulesMapper.selectByShopId(shopOut.getId());
        if(shopPromotionRulesOuts!=null&&shopPromotionRulesOuts.size()>0){
            for (ShopPromotionRulesOut shopPromotionRulesOut : shopPromotionRulesOuts) {
                stringList.add(shopPromotionRulesOut.getRuleDesc());
            }
        }
        shopOut.setRuleDescs(stringList);
    }

    @Override
    public String selectNewShop() {
        List<Shop> shops = shopMapper.selectNewShop();
        StringBuilder re = new StringBuilder();
        for (Shop shop : shops) {
            re.append(shop.getShopName()+",");
        }
        return re.toString();
    }

    @Override
    public List<Shop> DuplicateCheckingByShopName(Shop shop) {
        Shop hp = shopMapper.selectByPrimaryKey(shop.getId());
        if (shop.getShopName().equals(hp.getShopName())){
            return null;
        }
        ShopEnter record = new ShopEnter();
        record.setShopName(shop.getShopName());
        ShopEnterOut shopEnterOut = shopEnterMapper.selectByDuplicateChecking(record);
        List<Shop> shops = new ArrayList<Shop>();
        if (shopEnterOut!=null&&shopEnterOut.getId()!=null){
            shops.add(shop);
            return shops;
        }
        shops=shopMapper.DuplicateCheckingByShopName(shop.getShopName());
        return shops;
    }


    /**
     * 设置推荐菜
     * @param shopOut
     */
    private void setGoodsSku(ShopOut shopOut) {
        GoodsSku goodsSku = goodsSkuMapper.selectOneGoodsSkuByShopId(shopOut.getId());
        shopOut.setGoodsSku(goodsSku);
    }

    @Override
    public Pager<ShopOut> listForGoodsSku(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        Integer listTotal=shopMapper.listForGoodsSkuTotal(inVo);
        if(listTotal > 0){
            List<ShopOut> list = shopMapper.listForGoodsSku(inVo);
            for(ShopOut shopOut:list){
                this.setShopCateName(shopOut);
                this.setGoodsSkuList(shopOut);
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    /**
     * 设置菜品列表
     * @param shopOut
     */
    private void setGoodsSkuList(ShopOut shopOut) {
        GoodsSkuInVo inVo=new GoodsSkuInVo();
        inVo.setStart(0);
        inVo.setRows(10);
        inVo.setShopId(shopOut.getId());
        inVo.setStatus(GoodsSku.STATUS_SJ);
        inVo.setActId(41L);
       /* List<GoodsSkuOut> goodsSku = goodsSkuMapper.listForFastFood(inVo);
        shopOut.setGoodsSkuList(goodsSku);*/
        List<GoodsSkuOut> list =goodsSkuMapper.listForShopAct(inVo);
        if(list!=null&&list.size()>0){
            shopOut.setGoodsSkuList(list);
        }
        /*int listTotal = goodsSkuMapper.listTotal(inVo);
        if(listTotal > 0){
            List<GoodsSkuOut> list = goodsSkuMapper.list(inVo);
            //为了避免查询过多,只有在查询某个店铺的商品的时候,查询对应的活动列表
            if(inVo.getShopId()!=null) {
                for (GoodsSkuOut goodsSkuOut : list) {
                    this.setActInfoDetailAndActInfoList(inVo, goodsSkuOut);
                }
            }
            shopOut.setGoodsSkuList(list);
        }*/


    }

    /**
     * 添加商品参与的活动列表,和对应某个活动的活动详情  从goodsSkuService中复制过来的
     * @param inVo
     * @param goodsSkuOut
     */
    public void setActInfoDetailAndActInfoList(GoodsSkuInVo inVo,GoodsSkuOut goodsSkuOut){
        ActGoodsSkuInVo actGoodsSkuInVo = new ActGoodsSkuInVo();
        actGoodsSkuInVo.setSkuId(goodsSkuOut.getId());
        //查询商品参与的活动列表
        List<ActGoodsSkuOut> actGoodsSkuOuts = actGoodsSkuMapper.selectActInfoListByGoodsSkuId(actGoodsSkuInVo);
        goodsSkuOut.setActGoodsSkuOuts(actGoodsSkuOuts);

        if (inVo.getActId()!=null){
            //添加活动类目
            actGoodsSkuInVo.setActId(inVo.getActId());
            ActGoodsSkuOut actGoodsSkuOut =actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
            goodsSkuOut.setActGoodsSkuOut(actGoodsSkuOut);
        }

        if(null!=goodsSkuOut.getUseSkuDetail()){//设置商品详细信息
            List<GoodsSkuDetail> goodsSkuDetails=goodsSkuDetailMapper.listBySkuId(goodsSkuOut.getId());
            goodsSkuOut.setGoodsSkuDetails(goodsSkuDetails);
        }
    }

    @Override
    public Pager<ShopOut> listNormal(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        Integer listTotal=shopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal != null && listTotal > 0) {
            List<ShopOut> list = shopMapper.list(inVo);

            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Pager<ShopOut> listForVip(ShopInVo inVo) {
        Pager<ShopOut> result = new Pager<ShopOut>();
        Integer listTotal=shopMapper.listForVipTotal(inVo);
        if(listTotal > 0){
            List<ShopOut> list = shopMapper.listForVip(inVo);
            for(ShopOut shopOut:list){
                this.setVipGoodsSkuForShop(shopOut);
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    private  void setVipGoodsSkuForShop(ShopOut shopOut){
        GoodsSkuInVo goodsSkuInVo=new GoodsSkuInVo();
        goodsSkuInVo.setShopId(shopOut.getId());
        goodsSkuInVo.setShowType(1);//优惠券的商品showType为1
        goodsSkuInVo.setStart(0);
        goodsSkuInVo.setRows(10);
        goodsSkuInVo.setStatus(1);
        //优惠券列表
        List<GoodsSkuOut> vipCouponlist = goodsSkuMapper.list(goodsSkuInVo);
        for(GoodsSkuOut goodsSkuOut:vipCouponlist){
            this.setActInfoDetailAndActInfoList(goodsSkuInVo,goodsSkuOut);
        }

        //单品套餐列表
        goodsSkuInVo.setShowType(2);//单品套餐的商品showType为2
        List<GoodsSkuOut> vipGoodslist = goodsSkuMapper.list(goodsSkuInVo);
        for(GoodsSkuOut goodsSkuOut1:vipGoodslist){
            this.setActInfoDetailAndActInfoList(goodsSkuInVo,goodsSkuOut1);
        }
        vipCouponlist.addAll(vipGoodslist);
        shopOut.setGoodsSkuList(vipCouponlist);

    }
}
