package com.xq.live.web.controllerForApp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ${DESCRIPTION}
 *
 * @author lipeng
 * @date 2018-03-11 11:09
 * @copyright:hbxq
 **/
@Controller
@RequestMapping("/app/m")
public class RichScanForAppController {
    @RequestMapping(value="/hx",method= RequestMethod.GET)
    public String getScan(Model model){
        return "scan";
    }
}
