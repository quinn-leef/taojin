package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

//    /**
//     * 搜索
//     * @param searchMap
//     * @return
//     */
//    @GetMapping(value = "/list")
//    public String search(@RequestParam(required = false) Map searchMap, Model model){
//        //调用changgou-service-search微服务
//        Map resultMap = skuFeign.search(searchMap);
//        model.addAttribute("result",resultMap);
//        return "search";
//    }

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model){
        //调用changgou-service-search微服务
        Map<String,Object> resultMap = skuFeign.search(searchMap);
        //搜索数据结果
        model.addAttribute("result",resultMap);
        //搜索条件
        model.addAttribute("searchMap",searchMap);

        //4.记住之前的URL
        //拼接url
        String[] urls = url(searchMap);
        model.addAttribute("url",urls[0]);
        model.addAttribute("sortUrl",urls[1]);

        Page<SkuInfo> page = new Page<>(
                Long.parseLong(resultMap.get("totalPages").toString()),
                Integer.parseInt(resultMap.get("pageNum").toString()),
                Integer.parseInt(resultMap.get("pageSize").toString())
        );
        model.addAttribute("page", page);
        return "search";
    }

//    private String url(Map<String, String> searchMap) {
//        String url = "/search/list";
//        if(searchMap!=null && searchMap.size()>0){
//            url+="?";
//            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
//                String key = stringStringEntry.getKey();// keywords / brand  / category
//                String value = stringStringEntry.getValue();//华为  / 华为  / 笔记本
//                if(key.equals("pageNum")){
//                    continue;
//                }
//                url+=key+"="+value+"&";
//            }
//
//            //去掉多余的&
//            if(url.lastIndexOf("&")!=-1){
//                url =  url.substring(0,url.lastIndexOf("&"));
//            }
//
//        }
//        return url;
//    }

    private String[] url(Map<String, String> searchMap) {// { spec_网络:"移动4G","keywords":"华为"}
        String url = "/search/list"; // a/b?id=1&
        String sortUrl = "/search/list"; // a/b?id=1&
        if (searchMap != null) {
            url += "?";
            for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
                //如果是排序 则 跳过 拼接排序的地址 因为有数据
                if(stringStringEntry.getKey().equals("sortField") || stringStringEntry.getKey().equals("sortRule")){
                    continue;
                }
                url += stringStringEntry.getKey() + "=" + stringStringEntry.getValue() + "&";
                sortUrl += stringStringEntry.getKey() + "=" + stringStringEntry.getValue() + "&";

            }
            if(url.lastIndexOf("&")!=-1) {
                url = url.substring(0, url.lastIndexOf("&"));
                sortUrl = url.substring(0, url.lastIndexOf("&"));
            }

        }
        return new String[]{url, sortUrl};
    }

}