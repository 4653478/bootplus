package com.age.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.age.entity.SysMenuEntity;
import com.age.frame.controller.AbstractController;
import com.age.service.SysMenuService;
import com.age.util.PageUtils;
import com.age.util.R;
import com.age.util.config.Constant;
import com.age.util.exception.RRException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 系统菜单
 *
 * @author age
 * @Email 4653478@qq.com
 */
@RestController
@RequestMapping("/admin/sys/menu")
public class SysMenuController extends AbstractController<SysMenuController> {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 所有菜单列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public R list(Integer offset, Integer limit, String sort, String order,
                  @RequestParam(name = "search", required = false) String search) {
        Map<String, String> searchList = parseObject(search, "q_parentName", "q_menuName");
        String parentName = null;
        String menuName = null;
        if (null != searchList) {
            parentName = searchList.get("q_parentName");
            menuName = searchList.get("q_menuName");
        }
        Map<String, Object> map = new HashMap<String, Object>(6);
        map.put("offset", offset);
        map.put("limit", limit);
        // 没用到MyBatisPlus只能这样先写
        map.put("sort", "orderNum".equalsIgnoreCase(sort) ? "order_num" : sort);
        map.put("order", order);
        map.put("parentName", parentName);
        map.put("menuName", menuName);

        // 查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryList(map);
        int total = sysMenuService.queryTotal(map);
        PageUtils pAgeUtil = new PageUtils(menuList, total, limit, (offset / limit) + 1);
        return R.ok().put("page", pAgeUtil);
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public R select() {
        // 查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();
        // 添加顶级菜单
        SysMenuEntity root = new SysMenuEntity();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        return R.ok().put("menuList", menuList);
    }

    /**
     * 角色授权菜单
     */
    @RequestMapping("/perms")
    @RequiresPermissions("sys:menu:perms")
    public R perms() {
        // 查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryList(new HashMap<String, Object>(0));

        return R.ok().put("menuList", menuList);
    }

    /**
     * 菜单信息
     */
    @RequestMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public R info(@PathVariable("menuId") Long menuId) {
        SysMenuEntity menu = sysMenuService.selectById(menuId);
        return R.ok().put("menu", menu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public R save(SysMenuEntity menu) {
        // 数据校验
        verifyForm(menu);
        if (sysMenuService.insert(menu)) {
            // 清空菜单缓存
            sysMenuService.clearUserMenuList(getAdminId());
            return R.ok();
        } else {
            return R.error("保存失败!");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public R update(SysMenuEntity menu) {
        // 数据校验
        verifyForm(menu);
        if (sysMenuService.updateById(menu)) {
            // 清空菜单缓存
            sysMenuService.clearUserMenuList(getAdminId());
            return R.ok();
        } else {
            return R.error("修改失败!");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:menu:delete")
    public R delete(@RequestParam("menuIds") String ids) {
        JSONArray jsonArray = JSONArray.parseArray(ids);
        Long[] menuIds = toArrays(jsonArray);
        for (Long menuId : menuIds) {
            if (menuId.longValue() <= 16) {
                return R.error("系统菜单，不能删除");
            }
        }

        List<Long> menuIdsFlag = new ArrayList<Long>();
        Collections.addAll(menuIdsFlag, menuIds);
        boolean flag;
        for (Long menuId : menuIds) {
            flag = true;
            // 判断是否有子菜单或按钮
            List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId, null);
            for (SysMenuEntity sysMenuEntity : menuList) {
                if (menuIdsFlag.contains(sysMenuEntity.getMenuId())) {
                    // 已经存在要删除的子菜单或按钮
                    flag = false;
                } else {
                    flag = true;
                }
            }
            if (menuList.size() > 0 && flag) {
                return R.error("请先删除子菜单或按钮");
            }
        }

        sysMenuService.deleteBatch(menuIds);

        // 清空菜单缓存
        sysMenuService.clearUserMenuList(getAdminId());

        return R.ok();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenuEntity menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new RRException("上级菜单不能为空");
        }

        // 菜单
        if (menu.getType().equals(Constant.MenuType.MENU.getValue())) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RRException("菜单URL不能为空");
            }
        }

        // 上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getType() != Constant.MenuType.CATALOG.getValue()) {
            SysMenuEntity parentMenu = sysMenuService.selectById(menu.getParentId());
            if (null == parentMenu) {
                throw new RRException("请先选择上级菜单");
            }
            parentType = parentMenu.getType();
        }

        // 目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() || menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        // 按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new RRException("上级菜单只能为菜单类型");
            }
            return;
        }
    }
}
