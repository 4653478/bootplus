package com.age.controller.admin;

import com.age.config.CacheConfig;
import com.age.config.CacheManagerConfig;
import com.age.config.EncryptConfig;
import com.age.datasources.DataSource;
import com.age.datasources.DataSourceContextHolder;
import com.age.entity.AhUserEntity;
import com.age.frame.cache.CacheNameConstant;
import com.age.frame.cache.annotation.MyCacheable;
import com.age.service.AhUserService;
import com.age.util.PageUtils;
import com.age.util.R;
import com.age.util.http.HttpClient;
import com.age.util.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.vdurmont.emoji.EmojiParser;
import net.sf.ehcache.CacheManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.age.util.encry.AesEncryptUtil.encrypt;


@RestController
@RequestMapping("/admin/sys/ahuser")

public class AhUserController extends AbstractController {

    @Resource
    private AhUserService ahUserService;

    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/list")
    @RequiresPermissions("sys:ahuser:list")
    @ResponseBody
    public R list(Integer offset, Integer limit, String sort, String order,
                  @RequestParam(name = "search", required = false) String search) {

        String did = null;
        Map<String, String> searchList = parseObject(search, "q_did");
        if (null != searchList) {
            did = searchList.get("q_did");
        }
        offset = (offset / limit) + 1;
        Boolean flag = null; // 排序逻辑
        if (StringUtils.isNoneBlank(order)) {
            if (order.equalsIgnoreCase("asc")) {
                flag = true;
            } else {
                flag = false;
            }
        }
        Page<AhUserEntity> adminList = ahUserService.queryListByPage(offset, limit, did, sort, flag);
        PageUtils pAgeUtil = new PageUtils(adminList.getRecords(), adminList.getTotal(), adminList.getSize(),
                adminList.getCurrent());
        return R.ok().put("page", pAgeUtil);
    }


    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/info/{id}")
    @ResponseBody
    @RequiresPermissions("sys:ahuser:info")
    public R info(@PathVariable("id") Long id) {
        AhUserEntity user = ahUserService.selectById(id);

        return R.ok().put("user", user);
    }

    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/save")
    @ResponseBody
    @RequiresPermissions("sys:ahuser:save")
    public R save(@Valid AhUserEntity user, BindingResult result)
            throws Exception {
        if (result.hasErrors()) { // 验证有误
            return R.error(result.getFieldError().getDefaultMessage());
        }
        user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));


        ahUserService.save(user);
        return R.ok();
    }


    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/update")
    @ResponseBody
    @RequiresPermissions("sys:ahuser:update")
    public R update(AhUserEntity user) {
        user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        ahUserService.updateUser(user);
        return R.ok();
    }


    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("sys:ahuser:delete")
    public R delete(@RequestParam("ids") String ids) {
        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] userIds = toArrays(jsonArray);
        if (userIds.length < 1) {
            return R.error("删除的用户为空");
        }

        ahUserService.deleteBatch(userIds);
        return R.ok();
    }


}
