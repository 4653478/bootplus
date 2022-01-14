<!DOCTYPE html>
<html>
<head>
    <title>AH消息管理</title>
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
                    <h5>消息列表</h5>

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
                                                type="button" aria-expanded="false">发送人ID
                                        </button>
                                    </div>
                                    <input type="text" class="form-control" name="q_fromUser" placeholder="">
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

                                    [#--<div id="toolbar" class="btn-group m-t-sm">
                                        [@shiro.hasPermission name="sys:ahmsgkf:save"]
                                        <button type="button" style="margin-right:10px;" class="btn btn-primary"
                                                title="创建" onclick="add($('#showHandle'))"><i
                                                class="glyphicon glyphicon-plus"></i> 创建
                                        </button>
                                        [/@shiro.hasPermission]
                                        [@shiro.hasPermission name="sys:ahmsgkf:delete"]
                                        <button type="button" style="margin-right:10px;" class="btn btn-danger"
                                                title="批量删除" onclick="del($('#table'))"><i
                                                class="glyphicon glyphicon-remove"></i> 批量删除
                                        </button>
                                        [/@shiro.hasPermission]
                                    </div>--]

                                    <table id="table"
                                           data-toggle="table"
                                           data-height="600"
                                           data-search="false"
                                           data-search-on-enter-key="false"
                                           data-show-refresh="true"
                                           data-show-toggle="true"
                                           data-show-export="true"
                                           data-show-columns="true"
                                           data-url="${rc.contextPath}/admin/sys/ahmsg/getdata/msgusers" [#-- 服务器数据URL --]
                                           data-pagination="true"
                                           data-page-size="10"
                                           data-page-list="[10, 20, 50, 100]"
                                           data-side-pagination="server"
                                           data-striped="true"
                                           data-pagination="true"
                                           data-sort-name="create_time" [#-- 默认排序字段 --]
                                           data-sort-order="desc" [#-- 默认排序顺序 可选asc desc --]
                                           data-toolbar="#toolbar" [#-- 指定工具类元素 --]
                                           data-click-to-select="true" [#-- 设置true 将在点击行时，自动选择rediobox 和 checkbox --]
                                           data-single-select="false" [#-- 设置True 将禁止多选 --]
                                           data-unique-id="id" [#-- 填写主键ID即可 --][#-- 官方文档:http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/ --]
                                    	   data-response-handler="defaultBootstarpTableHandler">
                                    <thead>
                                    <tr>
                                        <th data-checkbox="true"></th>
                                        <th data-field="from_id" data-halign="center" data-align="center" data-sortable="true">发送人ID</th>
                                        <th data-field="from_name"  data-halign="center" data-align="center" data-sortable="true">发送人</th>
                                        <th data-field="from_avatar" data-formatter="formatImage" data-sortable="false" data-halign="center" data-align="center">头像</th>
                                        <th data-field="from_did" data-halign="center" data-align="center" data-sortable="true">DID</th>
                                        <th data-field="hosp_name" data-halign="center" data-align="center" data-sortable="true">医院</th>
                                        <th data-field="dept_name" data-halign="center" data-align="center" data-sortable="true">科室</th>
                                        <th data-field="bed" data-halign="center" data-align="center" data-sortable="true">床位</th>
                                        <th data-field="msg" data-formatter="formatMsgAsyn" data-halign="center" data-align="center" data-sortable="true">最新消息</th>
                                        <th data-field="create_time" data-sortable="true" data-halign="center" data-align="center">发送时间</th>
                                        <th data-field="unread_count" data-halign="center" data-align="center" data-sortable="true">未读消息数</th>
                                        <th data-formatter="actionFormatter" data-events="actionEvents" data-halign="center" data-align="center">操作</th>
                                    </tr>
                                    </thead>
                                    </table>
                                    <input type='hidden' id="handle"
                                        [@shiro.hasPermission name="sys:ahmsgkf:update" ] data-update="true" [/@shiro.hasPermission]
                                    	[#--[@shiro.hasPermission name="sys:ahmsgkf:delete"] data-delete="true"[/@shiro.hasPermission]/>--]
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
                <h5><span id="title">创建</span><small></small></h5>
                <div class="ibox-tools"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a> <a
                        class="close-link"><i class="fa fa-times"></i></a></div>
            </div>

            <div  id="dvChatMsg" style="overflow:auto;height: 350px;border:1px solid #eeeeee">
                <table id="tbChatMsg" style="width: 100%;height: 100%">

                </table>
            </div>
            <div><span style="color: blue" onclick="refresh(this);">[刷新]</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: blue" onclick="loadMore(this);"> [加载历史记录] </span></div>
            <div class="hr-line-dashed"></div>
            <div style="vertical-align: center">
                [#--<input type="text" maxlength="500" style="width: 100%;height: 40px" name="msg">--]
                <textarea id="msgReply" name="msg" cols="50" rows="2" ></textarea>
                <button type="button" onclick="replyMsg(this);">发送</button>


                <!--如有multiple属性。可以选择多个文件 -->
                <br>发送文件：<input type="file" name="" id="ipt" onchange="fileSel();" />
                <img id="loading" src="../img/loading.gif" alt="" style="display: none;">
                <!-- bootstrap中引入条件 -->
                <div class="progress" style="margin-top: 10px;width: 100px;margin-left: 10px;">
                    <div id="progress" class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
                        0%
                    </div>
                </div>


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
<script src="${rc.contextPath}/statics/js/admin/sys/ahmsgkf.js"></script>
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