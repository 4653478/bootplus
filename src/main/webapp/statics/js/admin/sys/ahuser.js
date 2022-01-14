/**
 * 表单验证
 */
$(function() {
	$('#form')
			.bootstrapValidator(
					{
						message : 'This value is not valid',
						feedbackIcons : {
							valid : 'glyphicon glyphicon-ok',
							invalid : 'glyphicon glyphicon-remove',
							validating : 'glyphicon glyphicon-refresh'
						},
						fields : {
							'wxName' : {
								validators : {
									notEmpty : {
										message : '微信名不能为空'
									}
								}
							},
							'wxId' : {
								validators : {
									notEmpty : {
										message : '微信ID不能为空'
									},
									/*regexp : {
										regexp : /^1[3|4|5|7|8]\d{9}$/,
										message : '手机号码格式不正确'
									}*/
								}
							},
							'did' : {
								validators : {
									notEmpty : {
										message : 'DID不能为空'
									},
									/*regexp : {
										regexp : /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
										message : '电子邮箱格式不正确'
									}*/
								}
							}
						}
					});
});

// 筛选数据
function search() {
	var params = $('#table').bootstrapTable('getOptions');
	params.queryParams = function(params) {
		// 定义参数
		var search = {};
		// 遍历form 组装json
		$.each($("#searchform").serializeArray(), function(i, field) {
			// 可以添加提交验证
			search[field.name] = field.value;
		});

		// 参数转为json字符串，并赋给search变量 ,JSON.stringify <ie7不支持，有第三方解决插件
		params.search = JSON.stringify(search);
		return params;
	}
	$('#table').bootstrapTable('refresh', params);
}

var user;
/**
 * ICheck选择样式
 * 
 * @returns
 */
function radio() {
	$('#showHandle input').iCheck({
		checkboxClass : 'icheckbox_flat-green',
		radioClass : 'iradio_flat-green'
	});
}

// 状态
function formatStatus(value, row, index) {
	if (value == null) {
		return "";
	}
	if (value === 1) {
		return '<input type="checkbox" name="my-checkbox" data-userId="'
				+ row.userId + '" checked>';
	}
	if (value === 0) {
		return '<input type="checkbox" name="my-checkbox" data-userId="'
				+ row.userId + '" >';
	}
}

// 开关切换(确保在Dom元素加载后渲染)
$("#table")
		.on(
				"load-success.bs.table",
				function() {
					$("input[name='my-checkbox']").bootstrapSwitch({
						onText : "启用",
						offText : "禁用",
						size : "mini"
					});
					// Dom元素加载后才能绑定触发事件
					$('input[name="my-checkbox"]')
							.on(
									'switchChange.bootstrapSwitch',
									function(event, state) {
										var index = layer.load(1, {
											shade : [ 0.3, '#fff' ]
										// 0.1透明度的白色背景
										});
										var userId = $(this)
												.attr("data-userId");
										if (userId == null) {
											layer.close(index);
											return false;
										}
										$
												.ajax({
													type : "POST",
													url : 'ahuser/updateStatus',
													headers : {
														'Content-Type' : 'application/x-www-form-urlencoded'
													},
													data : {
														userId : userId,
														state : state
													},
													success : function(r) {
														layer.close(index);
														if (r.code === 0) {
														} else {
															layer
																	.alert(
																			r.msg,
																			{
																				icon : 2
																			});
														}
													}
												});
									});
				});

var handle = $("#handle");
var data_update = $(handle).attr("data-update");
var data_delete = $(handle).attr("data-delete");
// BootStrapTable自定义操作
function actionFormatter(e, value, row, index) {
	if (null == data_update && null == data_delete) {
		return "";
	} else if (null == data_update && null != data_delete) {
		return [
				'<a class="remove text-danger" href="javascript:void(0)" title="删除">',
				'<i class="glyphicon glyphicon-remove"></i>删除', '</a>' ]
				.join('');
	} else if (null != data_update && null == data_delete) {
		return [
				'<a class="edit text-warning" href="javascript:void(0)" title="编辑">',
				'<i class="glyphicon glyphicon-edit"></i>编辑', '</a>' ].join('');
	} else {
		return [
				'<a class="edit m-r-sm text-warning" href="javascript:void(0)" title="编辑">',
				'<i class="glyphicon glyphicon-edit"></i>编辑',
				'</a>',
				'<a class="remove text-danger" href="javascript:void(0)" title="删除">',
				'<i class="glyphicon glyphicon-remove"></i>删除', '</a>' ]
				.join('');
	}
}

// Table操作
window.actionEvents = {
	// 编辑
	'click .edit' : function(e, value, row, index) {
		user_update(index, row.id);
	},
	// 删除
	'click .remove' : function(e, value, row, index) {
		user_delete(index, row.id);
	}
};

/*
 * 删除用户
 */
function user_delete(index, value) {
	var userIds = new Array();
	userIds[0] = value;
	layer.confirm('确认要删除该用户吗？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		$.ajax({
			type : 'delete',
			dataType : 'json',
			url : 'ahuser/delete?ids=' + JSON.stringify(userIds),
			success : function(result) {
				if (result.code === 0) {
					$('#table').bootstrapTable('hideRow', {
						index : index
					}); // 移除Table的一行
					layer.msg('该用户删除成功!', {
						icon : 1,
						time : 1000
					});
				} else {
					layer.alert(result.msg, {
						icon : 2
					});
				}
			}
		})
	});
}

// 修改用户
function user_update(index, value) {
	$("#title").text("修改用户");
	//getRoleList(value);
	getUser(value);
	layer_show("修改用户", $("#showHandle"), 800, 500);
}

/**
 * 批量删除用户
 */
function del(tableName) {
	var s = $(tableName).bootstrapTable('getSelections');
	if (s.length < 1) {
		layer.alert('请至少选择一条数据', {
			icon : 2
		});
		return false;
	}
	var userIds = new Array();
	for (var i = 0; i < s.length; i++) {
		userIds[i] = s[i].id;
	}
	layer.confirm('确认要删除选中的用户吗？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		$.ajax({
			type : 'delete',
			dataType : 'json',
			url : 'ahuser/delete?ids=' + JSON.stringify(userIds),
			success : function(result) {
				if (result.code === 0) {
					layer.msg('删除成功!', {
						icon : 1,
						time : 1000
					}, function() {
						location.reload();
					});
				} else {
					layer.alert(result.msg, {
						icon : 2
					});
				}
			}
		})
	});
}

// 新建用户
function add(s) {
	wstro.progressBarStartUp();
	$("#title").text("新建用户");
	$("input[name='id']").val("");
	$("input[name='wxName']").val("");
	$("input[name='wxId']").val("");
	$("input[name='did']").val("");
	$("input[name='loginTime']").val(formatDate_yyyyMMddHHmmss(new Date()));
	$("input[name='logoutTime']").val(formatDate_yyyyMMddHHmmss(new Date()));
	radio(); // 要重新生成样式
	//getRoleList();
	layer_show("新建用户", $(s), 800, 400);
	wstro.progressBarShutDown();
}

// 获取用户信息
function getUser(userId) {
	$.get("ahuser/info/" + userId, function(r) {
		if (r.code === 0) {
			user = r.user;
			$("input[name='id']").val(user.id);
			$("input[name='wxName']").val(user.wxName);
			$("input[name='wxId']").val(user.wxId);
			$("input[name='did']").val(user.did);
			$("input[name='loginTime']").val(user.loginTime);
			$("input[name='logoutTime']").val(user.logoutTime);

		} else {
			layer.alert(r.msg, {
				icon : 2
			});
		}
	});
}


/**
 * 保存或更新
 */
function saveOrUpdate(e) {
	loadingButton($(e));

	if (!validateForm($("#form"))) {
		return false;
	}

	var userId = $("input[name='id']").val();
	userId = userId == "" ? null : userId;
	var params = "";

	$("#form input").each(function() {
		params += $(this).serialize() + "&";
	});
	var url = userId == null ? "ahuser/save" : "ahuser/update";
	wstro.progressBarStartUp();
	$.ajax({
		type : "POST",
		url : url,
		headers : {
			'Content-Type' : 'application/x-www-form-urlencoded'
		},
		data : params,
		success : function(r) {
			wstro.progressBarShutDown();
			if (r.code === 0) {
				layer.msg('操作成功!', {
					icon : 1,
					time : 1000
				}, function() {
					location.reload();
				});
			} else {
				layer.alert(r.msg, {
					icon : 2
				});
			}
		}
	});
}