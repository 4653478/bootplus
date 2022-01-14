<!DOCTYPE html>
<html>
<head>
    <title>AH用户管理</title>
    [#include "/admin/header.ftl"]
    <link rel="stylesheet" href="${rc.contextPath}/statics/common/bootstrap-table/bootstrap-table.min.css"/>
    <link rel="stylesheet" href="${rc.contextPath}/statics/common/icheck/flat/green.css"/>
    <link rel="stylesheet" href="${rc.contextPath}/statics/common/bootstrap-switch/css/bootstrap-switch.min.css"/>
</head>
<body class="gray-bg" style="display:none;">
<div class="wrapper wrapper-content">
    <div id="row" class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>用户列表</h5>

                    <div class="ibox-tools"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a> <a
                            class="close-link"><i class="fa fa-times"></i></a></div>
                </div>
                <div class="ibox-content form-horizontal">
                    <div class="row row-lg">
                        <form id="searchform">

                            <div class="col-sm-3">
                                <div class="input-group">
                                    <div class="input-group-btn">
                                        <button data-toggle="dropdown" class="btn btn-default dropdown-toggle"
                                                type="button" aria-expanded="false">DID
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="q_did" placeholder="">
                                </div>
                            </div>
                            <div class="col-sm-3">
                                <button type="button" class="btn btn-primary" onclick="search()">
                                    <i class="fa fa-search"></i>&nbsp;搜索
                                </button>
                            </div>
                        </form>
                        <div class="col-sm-12">
                            <div class="example-wrap">
                                <div class="example">
                                    <div id="toolbar" class="btn-group m-t-sm">
                                        [@shiro.hasPermission name="sys:ahuser:save"]
                                        <button type="button" style="margin-right:10px;" class="btn btn-primary"
                                                title="创建用户" onclick="add($('#showHandle'))"><i
                                                class="glyphicon glyphicon-plus"></i> 创建用户
                                        </button>
                                        [/@shiro.hasPermission]
                                        [@shiro.hasPermission name="sys:ahuser:delete"]
                                        <button type="button" style="margin-right:10px;" class="btn btn-danger"
                                                title="批量删除" onclick="del($('#table'))"><i
                                                class="glyphicon glyphicon-remove"></i> 批量删除
                                        </button>
                                        [/@shiro.hasPermission]
                                    </div>
                                    <table id="table"
                                           data-toggle="table"
                                           data-height="600"
                                           data-search="false"
                                           data-search-on-enter-key="false"
                                           data-show-refresh="true"
                                           data-show-toggle="true"
                                           data-show-export="true"
                                           data-show-columns="true"
                                           data-url="${rc.contextPath}/admin/sys/ahuser/list" [#-- 服务器数据URL --]
                                           data-pagination="true"
                                           data-page-size="20"
                                           data-page-list="[20, 50, 100, 200]"
                                           data-side-pagination="server"
                                           data-striped="true"
                                           data-pagination="true"
                                           data-sort-name="createTime" [#-- 默认排序字段 --]
                                           data-sort-order="desc" [#-- 默认排序顺序 可选asc desc --]
                                           data-toolbar="#toolbar" [#-- 指定工具类元素 --]
                                           data-click-to-select="true" [#-- 设置true 将在点击行时，自动选择rediobox 和 checkbox --]
                                           data-single-select="false" [#-- 设置True 将禁止多选 --]
                                           data-unique-id="id" [#-- 填写主键ID即可 --][#-- 官方文档:http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/ --]
                                    	   data-response-handler="defaultBootstarpTableHandler">
                                    <thead>
                                    <tr>
                                        <th data-checkbox="true"></th>
                                        <th data-field="wxName" data-halign="center" data-align="center"
                                            data-sortable="true">微信用户名
                                        </th>
                                        <th data-field="wxId" data-halign="center" data-align="center"
                                            data-sortable="true">微信ID
                                        </th>
                                        <th data-field="gender" data-halign="center" data-align="center"
                                            data-sortable="true">性别
                                        </th>
                                        <th data-field="province" data-halign="center" data-align="center"
                                            data-sortable="true">省份
                                        </th>
                                        <th data-field="city" data-halign="center" data-align="center"
                                            data-sortable="true">市区
                                        </th>
                                        <th data-field="did" data-halign="center" data-align="center"
                                            data-sortable="true">DID
                                        </th>
                                        <th data-field="loginTime"
                                            data-sortable="true" data-halign="center" data-align="center">入院时间
                                        </th>
                                        <th data-field="logoutTime"
                                            data-sortable="true" data-halign="center" data-align="center">出院时间
                                        </th>
                                        <th data-field="createTime"
                                            data-sortable="true" data-halign="center" data-align="center">创建时间
                                        </th>
                                        <th data-formatter="actionFormatter" data-events="actionEvents"
                                            data-halign="center" data-align="center">操作
                                        </th>
                                    </tr>
                                    </thead>
                                    </table>
                                    <input type='hidden' id="handle"
                                        [@shiro.hasPermission name="sys:ahuser:update" ] data-update="true" [/@shiro.hasPermission]
                                    	[@shiro.hasPermission name="sys:ahuser:delete"] data-delete="true"[/@shiro.hasPermission]/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="showHandle" style="display:none;">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5><span id="title">创建AH用户<span><small></small></h5>
                <div class="ibox-tools"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a> <a
                        class="close-link"><i class="fa fa-times"></i></a></div>
            </div>
            <div class="ibox-content form-horizontal">
                <form id="form">
                    <input type='hidden' name="id"/>

                    <div class="form-group m-t">
                        <label class="col-sm-2 col-xs-offset-1 control-label">微信用户名：</label>

                        <div class="col-sm-6">
                            <input type="text" maxlength="15" class="form-control" name="wxName">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 col-xs-offset-1 control-label">微信Id：</label>

                        <div class="col-sm-6">
                            <input type="text" maxlength="50" class="form-control" name="wxId">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 col-xs-offset-1 control-label">DID：</label>

                        <div class="col-sm-6">
                            <input type="text" maxlength="50" class="form-control" name="did">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 col-xs-offset-1 control-label">入院时间：</label>

                        <div class="col-sm-6">
                            <input type="text" maxlength="50" class="form-control" name="loginTime" placeholder="yyyy-MM-dd HH:mm:ss">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-2 col-xs-offset-1 control-label">出院时间：</label>

                        <div class="col-sm-6">
                            <input type="text" maxlength="50" class="form-control" name="logoutTime" placeholder="yyyy-MM-dd HH:mm:ss">
                        </div>
                    </div>

                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <div class="col-sm-12 text-center">
                            <button type="button" class="btn btn-success" onclick="saveOrUpdate(this);">提交
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- bootstrapvalidator-master前端验证框架 -->
<script src="${rc.contextPath}/statics/common/bootstrapvalidator/js/bootstrapValidator.min.js"></script>
<!-- Bootstrap table -->
<script src="${rc.contextPath}/statics/common/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${rc.contextPath}/statics/common/bootstrap-table/extensions/export/bootstrap-table-export.js"></script>
<script src="${rc.contextPath}/statics/common/bootstrap-table/tableExport.js"></script>
<script src="${rc.contextPath}/statics/common/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<!-- 自定义js -->
<script src="${rc.contextPath}/statics/js/admin/sys/ahuser.js"></script>
<!-- iCheck -->
<script src="${rc.contextPath}/statics/common/icheck/icheck.min.js"></script>
<script src="${rc.contextPath}/statics/common/bootstrap-switch/js/bootstrap-switch.min.js"></script>
[#-- 邮箱自动补全 --]
[#assign inputEmail="input[name='email']"][#-- INPUT元素--]
[#assign form="#form"]
[#include "/admin/autoEmail.ftl"]
[#-- 页面加载进度条 --]
[#assign parentName="#row"][#-- 默认为Body --]
[#include "/admin/nprogress.ftl"]
[#-- 页面加载进度条 --]
[#assign parentName="#form"]
[#assign isFirst=true][#-- 默认为Body --]
[#include "/admin/nprogress.ftl"]
</body>
</html>