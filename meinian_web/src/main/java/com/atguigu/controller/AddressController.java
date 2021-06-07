package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    @GetMapping("/findAllMaps")
    public Map findAllMaps(){
        List<Address> addressList= addressService.findAllMaps();
        Map<String,Object> map = new HashMap<>();
        List<Map> gridnMaps = new ArrayList<>();
        List<Map> nameMaps = new ArrayList<>();
        Map<String,String> m1 = null;
        Map<String,String> m2 = null;
        for (Address address : addressList) {
            m1= new HashMap<>();
            m2= new HashMap<>();
            m1.put("lng",address.getLng());
            m1.put("lat",address.getLat());
            m2.put("addressName",address.getAddressName());
            gridnMaps.add(m1);
            nameMaps.add(m2);
        }
        map.put("gridnMaps",gridnMaps);
        map.put("nameMaps",nameMaps);

        return map;

    }

    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return addressService.findPage(queryPageBean);
    }

    @PostMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){
        try {
            addressService.addAddress(address);
            return new Result(true, "地址保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"地址保存失败");
        }
    }

    @PostMapping("/deleteById")
    public Result deleteById(@RequestParam("id") Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true, "地址删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"地址删除失败");
        }
    }
}
