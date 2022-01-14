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

// 图片
function formatImage(value) {
	if (value == null) {
		return "";
	}
	else{
		return '<img height="30px" width="30px" name="my-avatar" src="'+ value +'">';
	}

}
function formatMsg(value,row){
	var str=value.toString().toLowerCase();
	if(str.startsWith("http://") || str.startsWith("https://")){
		value='<a href="'+ value +'" target="_blank">'+ value +'</a>';
	}
	//console.log("formatMsg value:",fromId,row.from_id,row.unread_count,row);
	if(row.unread_count>=1)	{
		value=value+"<span style='color:red;'>[未读]</span>";
		//console.log("formatMsg value:",value);
	}

	return value;
}

function formatMsgAsyn(value, row, index) {
	var info="";

	var fromId=row.from_id;
	var url="ahmsg/getdata/msgbyuserlast";
	$.ajax({
		type: "Get",
		url: url,
		data: { fromuser: fromId },
		async: false,//false非异步，此方式会影响页面性能，但若为true异步，将无法通过ajax获取服务端的值，直接返回了
		success: function (data) {
			console.log("formatMsg data:",data);
			var lastMsg =data.data[0];

			if(lastMsg){
				var msg=lastMsg.msg;

				var str=msg.toString().toLowerCase();
				if(str.startsWith("http://") || str.startsWith("https://")){
					msg='<a href="'+ msg +'" target="_blank">'+ msg +'</a>';
				}

				if(lastMsg.from_id==fromId && lastMsg.unread>=1)	msg=msg+"<span style='color:red;'>[未读]</span>"
				//console.log("formatMsg msg:",msg);

				info= msg;
			}

		},
		error: function () { return ''; }
	});

	return info;
}

// 链接，暂时不用
function formatMsgAjax(value,row) {
	//console.log("formatMsg value:",value);
	var fromId=row.from_id;
	var url="ahmsg/getdata/msgbyuserlast?fromuser=" + fromId;
	//console.log("url=====",url);
	var lastMsg="";
	$.get(url, function(r) {
		//console.log("rrrrr=====",r);
		if (r.code === 0 ) {
			lastMsg =r.data[0];

			if(lastMsg){
				value=lastMsg.msg;

				var str=value.toString().toLowerCase();
				if(str.startsWith("http://") || str.startsWith("https://")){
					value='<a href="'+ value +'" target="_blank">'+ value +'</a>';
				}

				if(lastMsg.from_id==fromId && lastMsg.unread==1)	value=value+"<span style='color:red;'>[未读]</span>"
				console.log("formatMsg value:",value);

				return value;
			}

		} else {
			console.log(fromId+"-formatMsg error :"+r.code);
			return value;
		}
	});


}

// 跳转用户页
function formatUser(value, row) {
	if (value == null) {
		return "";
	}
	else{
		return '<a data-userId="'+ row.from_id + '" href="ahmsg.html?userid='+ row.from_id +'">'+value+'</a>';
	}

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
					//$("img[name='my-avatar']").attr("src","1.jpg");

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
										var userId = $(this).attr("data-userId");
										if (userId == null) {
											layer.close(index);
											return false;
										}
										$
												.ajax({
													type : "POST",
													url : 'ahmsg/updateStatus',
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
				'<a class="edit text-warning" href="javascript:void(0)" title="查看">',
				'<i class="glyphicon glyphicon-edit"></i>查看', '</a>' ].join('');
	} else {
		return [
				'<a class="edit m-r-sm text-warning" href="javascript:void(0)" title="查看">',
				'<i class="glyphicon glyphicon-edit"></i>查看',
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
		user_update(index, row.from_id);
	},
	// 删除
	'click .remove' : function(e, value, row, index) {
		user_delete(index, row.from_id);
	}
};

/*
 * 删除用户
 */
function user_delete(index, value) {
	var userIds = new Array();
	userIds[0] = value;
	layer.confirm('确认要删除吗？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		$.ajax({
			type : 'delete',
			dataType : 'json',
			url : 'ahmsg/delete?ids=' + JSON.stringify(userIds),
			success : function(result) {
				if (result.code === 0) {
					$('#table').bootstrapTable('hideRow', {
						index : index
					}); // 移除Table的一行
					layer.msg('删除成功!', {
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
	$("#title").text("消息");
	//getRoleList(value);
	//getUser(value);
	getUserMsg(value,true);
	layer_show("聊天消息", $("#showHandle"), 800, 600);
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
	layer.confirm('确认要删除选中的吗？', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		$.ajax({
			type : 'delete',
			dataType : 'json',
			url : 'ahmsg/delete?ids=' + JSON.stringify(userIds),
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
	$("#title").text("新建");
	$("input[name='id']").val("");
	$("input[name='fromUser']").val("");
	$("input[name='fromDid']").val("");
	$("input[name='toUser']").val("");
	$("input[name='toDid']").val("");

	//$("input[name='msg']").val("");
	$("textarea[name='msg']").val("");

	$("input[name='unread']").attr("disabled","disabled"); //.val("");
	$("input[name='createTime']").attr("disabled","disabled");; //.val(formatDate_yyyyMMddHHmmss(new Date()));
	$("input[name='uid']").attr("disabled","disabled");; //.val('');
	radio(); // 要重新生成样式
	//getRoleList();
	layer_show("新建", $(s), 800, 400);
	wstro.progressBarShutDown();
}



// 获取用户消息
var fromId;
var userMsg;
var pageNumber=1;
var pageSize=10;
function getUserMsg(userId,clearContent) {
	fromId=userId;

	if(clearContent)	{
		pageNumber=1;
		$("#tbChatMsg").html("");
	}
	$("#msgReply").val("");
	var url="ahmsg/getdata/msgbyuser?fromuser=" + fromId+"&pagenumber="+pageNumber+"&pagesize="+pageSize;
	console.log("url=====",url);
	$.get(url, function(r) {
		//console.log("rrrrr=====",r);
		if (r.code === 0) {
			userMsg =r.page.list;// r.data;

			if(userMsg.length==0){
				layer.alert("没有数据了~", {
					icon : 2
				});

				return;
			}

			for(var i=0;i<userMsg.length; i++ ){
				var html_msg="";
				var msgContent=userMsg[i].msg;
				if(msgContent.startsWith("http://") || msgContent.startsWith("https://")){
					msgContent="<a href='"+msgContent+"' target='_blank'><img src='"+msgContent+"' width='50px' height='50px' ></a>";
				}

				if(userMsg[i].from_id==userId){
					html_msg="<table style='width: 100%'><tr><td style='width: 50px;text-align:center;vertical-align:top;padding: 5px;'><img width='30px' height='30px' src='"+userMsg[i].from_avatar+"'></td><td style='width: auto;'><div style='width:auto;text-aligin：left;border-radius: 10px;padding: 5px' >"+msgContent+"<br><span style='font-size: 6px;color:#cccccc;margin-left: 1px'>["+userMsg[i].create_time+"]</span><br></div></td></tr></table>";
				}else{
					html_msg="<table style='width: 100%'><tr><td style='width: auto;'><div align='right'>"+msgContent+"<br><span style='font-size: 6px;color:#cccccc;margin-right: 1px'>["+userMsg[i].create_time+"]</span><br></td><td style='width: 50px;text-align:center;vertical-align:top;padding: 5px;'><img width='30px' height='30px' src='"+userMsg[i].from_avatar+"'></div></td></tr></table>";
				}
				html_msg=html_msg+"<div style='height: 10px'></div>";
				$("#tbChatMsg").prepend(html_msg);
			}


			/*for(var i=0;i<userMsg.length; i++ ){
				var html_msg="";
				var msgContent=userMsg[i].msg;
				if(msgContent.startsWith("http://") || msgContent.startsWith("https://")){
					msgContent="<a href='"+msgContent+"' target='_blank'><img src='"+msgContent+"' width='50px' height='50px' ></a>";
				}

				if(userMsg[i].from_id==userId){
					html_msg="<div align='left' width='50%'><img width='30px' height='30px' src='"+userMsg[i].from_avatar+"'>"+msgContent+"<br><span style='font-size: 6px;color:#cccccc;margin-left: 50px'>["+userMsg[i].create_time+"]</span><br></div>";
				}else{
					html_msg="<div align='right' width='50%'>"+msgContent+"<img width='30px' height='30px' src='"+userMsg[i].from_avatar+"'><br><span style='font-size: 6px;color:#cccccc;margin-right: 50px'>["+userMsg[i].create_time+"]</span><br></div>";
				}
				$("#dvChatMsg").prepend(html_msg);
			}*/

			if(pageNumber==1){
				$("#dvChatMsg").scrollTop(500) ;
			}else{
				$("#dvChatMsg").scrollTop(0) ;
			}


		} else {
			layer.alert(r.msg, {
				icon : 2
			});
		}
	});
}

function refresh(e){

	getUserMsg(fromId,true);
}
function loadMore(e){
	pageNumber++;
	getUserMsg(fromId,false);
}
function fileSel(){

	var files = $('#ipt')[0].files
	console.log("fileSel:",files);
	upload();
}

//回复消息
function replyMsg(e) {
	var msg=$("#msgReply").val();//$("textarea[name='msg']").val();
	if(msg=="")	return;

	loadingButton($(e));

	/*if (!validateForm($("#form"))) {
		return false;
	}*/

	var params = "fromUser=1&fromDid=&toUser="+fromId+"&toDid=&msg="+msg+"&unread=1&uid=&createTime="+ formatDate_yyyyMMddHHmmss(new Date());

	console.log("======params:"+params);
	var url = "ahmsg/save" ;
	/*wstro.progressBarStartUp();*/
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
					//location.reload(); //刷新页面

					getUserMsg(fromId,true);
				});
			} else {
				layer.alert(r.msg, {
					icon : 2
				});
			}
		}
	});
}

//文件上传
function upload(){

	// 获取文件列表
	var files = $('#ipt')[0].files
	// 判断是否选择了文件
	if (files.length != 1) {
		return alert('请选择一个文件')
	}

	var file=files[0];

	//对文件重命名：用户ID-日期-原文件名
	var newName=fromId+"-"+new Date().getTime()+"-"+file.name;//getGuid()+"-"+file.name
	file = new File([file], newName);
	var fd = new FormData();
	fd.append('file', file);

	console.log("file:",file);
	//console.log("file.name:",file.name);
	// 用ajax传送数据
	$.ajax({
		type: 'post',
		url: '/admin/sys/ahuser/upload',
		contentType: false,
		processData: false,
		data: fd,
		beforeSend: function() {
			$('#loading').show()
		},
		complete: function() {
			$('#loading').hide()
		},
		success: function(res) {
			// 判断是否接收成功
			if (res !== "ok") {
				layer.alert(res, {icon : 2});
				return;
			}
			var serverName='http://file.xxxxxx.com/ahb/uploadfiles/' +newName;
			$("#msgReply").val(serverName);
			replyMsg(null);
			//return alert(res);
			//$('#imgUpload').attr('src', 'http://file.xxxx.com/ahb/uploadfiles/' +newName).css('display', 'block');

		},
		xhr: function xhr() {
			var xhr = new XMLHttpRequest()
			// 获取文件上传的进度
			xhr.upload.onprogress = function(e) {
				// e.lengthComputable表示当前的进度是否是可以计算,返回布尔值
				if (e.lengthComputable) {
					// e.loaded表示下载了多少数据, e.total表示数据总量
					var percentComplete = Math.ceil((e.loaded / e.total) * 100)
					// 让进度条的宽度变化
					$('#progress').css('width', percentComplete)
					// 在进度条中显示百分比
					$('#progress').html(percentComplete +"%")
				}
			}
			// 文件加载完成
			xhr.upload.onload = function() {
				$('#progress').removeClass('progress-bar progress-bar-striped').addClass('progress-bar progress-bar-success')
			}
			return xhr
		}

	})
}



//文件上传（多个文件，且不改文件名）---暂时不用
function uploadFile(){
	 // var pic = $("#pic").get(0).files[0];
	 var pic=$('#pic').get(0).files;

	 var formData = new FormData();
	 for(var i=0;i<pic.length;i++){
		 formData.append("pic['+i+']",pic[i]);
	 }
	 formData.append("file" , pic[0]);

	 $.ajax({
		 type: "POST",
		 url: '/admin/sys/ahuser/upload',
		 data: formData ,
		 processData : false,
		 contentType : false ,//必须false才会自动加上正确的Content-Type
		 xhr: function(){
			 var xhr = $.ajaxSettings.xhr();
			 if(onprogress && xhr.upload) {
				 xhr.upload.addEventListener("progress" , onprogress, false);
				 return xhr;
			 }
		 }
	 });
}


// 获取用户信息
function getUser(userId) {
	$.get("ahmsg/info/" + userId, function(r) {
		if (r.code === 0) {
			user = r.user;
			$("input[name='id']").val(user.id);
			$("input[name='fromUser']").val(user.fromUser);
			$("input[name='fromDid']").val(user.fromDid);
			$("input[name='toUser']").val(user.toUser);
			$("input[name='toDid']").val(user.toDid);

			//$("input[name='msg']").val(user.msg);
			$("textarea[name='msg']").text(user.msg);

			$("input[name='unread']").val(user.unread);	$("input[name='unread']").removeAttr("disabled");
			$("input[name='createTime']").val(user.createTime);	$("input[name='createTime']").removeAttr("disabled");
			$("input[name='uid']").val(user.uid);	$("input[name='uid']").removeAttr("disabled");


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
	$("#form textarea").each(function() {
		params += $(this).serialize() + "&";
	});
	console.log("======params:"+params);
	var url = userId == null ? "ahmsg/save" : "ahmsg/update";
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
