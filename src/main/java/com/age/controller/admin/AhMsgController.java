package com.age.controller.admin;

import com.age.config.CacheManagerConfig;
import com.age.datasources.DataSource;
import com.age.datasources.DataSourceContextHolder;
import com.age.entity.AhMsgEntity;
import com.age.entity.AhUserEntity;
import com.age.frame.cache.CacheNameConstant;
import com.age.frame.cache.annotation.MyCacheable;
import com.age.service.AhMsgService;
import com.age.service.AhUserService;
import com.age.util.PageUtils;
import com.age.util.R;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/admin/sys/ahmsg")

public class AhMsgController extends AbstractController {

    @Resource
    private AhMsgService ahMsgService;

    /**
     * 所有用户列表
     */
    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/list")
    @RequiresPermissions("sys:ahmsg:list")
    @ResponseBody
    public R list(Integer offset, Integer limit, String sort, String order,
                  @RequestParam(name = "search", required = false) String search) {

        int from_user = 0;
        Map<String, String> searchList = parseObject(search, "q_fromUser");
        if (null != searchList) {
            from_user = Integer.parseInt(searchList.get("q_fromUser"));
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
        Page<AhMsgEntity> adminList = ahMsgService.queryListByPage(offset, limit, from_user, sort, flag);
        PageUtils pAgeUtil = new PageUtils(adminList.getRecords(), adminList.getTotal(), adminList.getSize(),adminList.getCurrent());
        return R.ok().put("page", pAgeUtil);
    }

    /**
     * 用户信息
     */
    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/info/{id}")
    @ResponseBody
    @RequiresPermissions("sys:ahmsg:info")
    public R info(@PathVariable("id") Long id) {
        AhMsgEntity user = ahMsgService.selectById(id);

        return R.ok().put("user", user);
    }


    /**
     * 保存用户
     *
     * @throws Exception
     */
    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/save")
    @ResponseBody
    @RequiresPermissions("sys:ahmsg:save")
    public R save(@Valid AhMsgEntity user, BindingResult result)
            throws Exception {
        if (result.hasErrors()) { // 验证有误
            return R.error(result.getFieldError().getDefaultMessage());
        }
        user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        UUID uuid = UUID.randomUUID();
        user.setUid(uuid.toString());
        user.setUnread(1);

        ahMsgService.save(user);
        return R.ok();
    }


    /**
     * 删除用户
     */
    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("sys:ahmsg:delete")
    public R delete(@RequestParam("ids") String ids) {
        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] userIds = toArrays(jsonArray);
        if (userIds.length < 1) {
            return R.error("删除的消息为空");
        }

        ahMsgService.deleteBatch(userIds);
        return R.ok();
    }


}
