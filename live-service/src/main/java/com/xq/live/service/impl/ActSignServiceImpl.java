package com.xq.live.service.impl;

import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.*;
import com.xq.live.exception.ActSignFailException;
import com.xq.live.model.ActShop;
import com.xq.live.model.ActSign;
import com.xq.live.model.Sku;
import com.xq.live.model.User;
import com.xq.live.service.ActSignService;
import com.xq.live.vo.in.ActSignInVo;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;

/**
 * 新平台活动报名材料接口
 * Created by lipeng on 2018/4/28.
 */
@Service
public class ActSignServiceImpl implements ActSignService{

    @Autowired
    private ActSignMapper actSignMapper;

    @Autowired
    private ActUserMapper actUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ActShopMapper actShopMapper;

    @Value("${jwt.password.salt}")
    private String salt;


    @Override
    public ActSign isSign(ActSignInVo inVo) {
        return actSignMapper.isSign(inVo);
    }

    @Override
    @Transactional
    public Long add(ActSignInVo inVo) throws ActSignFailException{
        if(inVo.getType()==ActSign.ACT_SIGN_TYPE_PLAYER) {
            //放在最上面的意义是为了不让插入数据的时候，直接把id也带进去了
            ActUserInVo invoUser = new ActUserInVo();
            BeanUtils.copyProperties(inVo, invoUser);
            invoUser.setUserId(inVo.getRefId());

            int insert1 = actSignMapper.insert(inVo);
            if(insert1<1){
                throw new ActSignFailException("报名失败，请检查报名材料!");
            }

            int i = actUserMapper.countByActId(inVo.getActId());
            /*DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
            String s = mFormat.format(i + 1);*/
            String s =(i + 1)+"";
            invoUser.setUserCode(s);
            int insert = actUserMapper.insert(invoUser);//插入数据
            if (insert < 1) {
                throw new ActSignFailException("报名失败，请检查报名材料!");
            }
            User user = userMapper.selectByPrimaryKey(invoUser.getUserId());
            user.setAge(inVo.getAge());
            user.setHeight(inVo.getHeight());
            //修改密码----begain
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(user.getUserName() + salt);//密码为userName + salt
            user.setPassword(encoder.encode(user.getPassword()));
            //end
            int k = userMapper.updateByPrimaryKeySelective(user);//修改user表的信息
            if (k < 1) {
                throw new ActSignFailException("报名失败，请检查报名材料!");
            }

            return invoUser.getId();
        }else{
            ActShop actShop = new ActShop();
            BeanUtils.copyProperties(inVo, actShop);
            actShop.setShopId(inVo.getRefId());

            //判断商家给的推荐菜是否已经在推荐菜列表且与其关联上了
            SkuInVo skuInVo = new SkuInVo();
            skuInVo.setShopId(inVo.getRefId());
            skuInVo.setSkuName(inVo.getSkuName());
            SkuForTscOut tscBySkuNameAndShopId = skuMapper.getTscBySkuNameAndShopId(skuInVo);

            Sku sku = new Sku();
            Long skuId;
            if(tscBySkuNameAndShopId ==null) {


                BeanUtils.copyProperties(inVo, sku);
                sku.setSkuType(Sku.SKU_TYPE_TSC);
                sku.setStockNum(9999);
                //先把数据插入到sku表中，标注状态为已删除状态不展示，等审核通过之后，人工把状态改成0
                sku.setIsDeleted(Sku.SKU_NO_DELETED);
                String skuCode = RandomStringUtil.getRandomCode(8, 0);
                String code = isSku(skuCode);//判断给的skuCode是否已存在数据，返回一个不存在数据的code
                sku.setSkuCode(code);
                int res = skuMapper.insert(sku);
                if (res < 1) {
                    throw new ActSignFailException("报名失败，请检查报名材料!");
                }
                skuId = sku.getId();

                //将推荐菜与shop_id关联起来
                SkuInVo vo = new SkuInVo();
                vo.setShopId(inVo.getRefId());
                vo.setId(skuId);
                int i = skuMapper.insertSkuShop(vo);
                if(i<1){
                    throw new ActSignFailException("报名失败，请检查报名材料!");
                }
            }else {
                skuId = tscBySkuNameAndShopId.getId();
            }


            inVo.setSkuId(skuId);
            int insert1 = actSignMapper.insert(inVo);
            if(insert1<1){
                throw new ActSignFailException("报名失败，请检查报名材料!");
            }

            int i = actShopMapper.countByActId(actShop.getActId());
            DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
            String s = mFormat.format(i + 1);
            actShop.setShopCode(s);
            int ret = actShopMapper.insert(actShop);
            if(ret < 1){
                 throw new ActSignFailException("报名失败，请检查报名材料!");
            }

            return actShop.getId();
        }
    }

    /**
     * 判断给的skuCode是否已存在数据，返回一个不存在数据的code
     * @param skuCode
     * @return
     */
    public String isSku(String skuCode){
        String code = skuCode;
        Sku sku = new Sku();
        sku = skuMapper.selectBySkuCode(code);
        while (sku !=null){
            code = RandomStringUtil.getRandomCode(8, 0);
            sku = skuMapper.selectBySkuCode(code);
        }
        return code;
    }
}
