package com.xq.live.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-11 11:09
 * @copyright:hbxq
 **/
@Controller
@RequestMapping("/m")
public class RichScanController {
    @RequestMapping(value="/hx",method= RequestMethod.GET)
    public String getScan(Model model){
        return "scan";
    }

    @RequestMapping(value="/act",method= RequestMethod.GET)
    public String getAct(Model model){
        return "act";
    }
}
