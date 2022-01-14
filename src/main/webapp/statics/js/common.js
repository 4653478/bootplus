// 命名空间定义
var wstro = window.NameSpace || {};

wstro.base = "/";
wstro.url = "http://dev2.66666684.cn/";

// 工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
const url = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};

T.p = url;

// 全局配置
$.ajaxSetup({
    dataType: "json",
    contentType: "application/json",
    error: function (jqXHR, textStatus, errorThrown) {
        // 取消进度条
        wstro.progressBarShutDown();
        switch (jqXHR.status) {
            case(500):
                layer.alert('服务器系统内部错误', {
                    icon: 2
                });
                break;
            case (401):
                layer.alert('未登录', {
                    icon: 2
                });
                break;
            case (403):
                layer.alert('无权限执行此操作', {
                    icon: 2
                });
                break;
            case (408):
                layer.alert('请求超时', {
                    icon: 2
                });
                break;
            default:
                layer.alert('未知错误,请联系管理员', {
                    icon: 2
                });
        }
    },
    cache: false
});

// 取得地址栏参数
let Request = {
    QueryString: function (item, paramurl) {
        let reg = new RegExp("(^|&)" + item + "=([^&]*)(&|$)", "i");
        let r = paramurl.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
};

// 取得顶窗口地址栏参数
let topRequest = {
    QueryString: function (item) {
        return Request.QueryString(item, top.location.search);
    }
}

/**
 * 替换字符串
 */
let replace = {
    /**
     * 所有\改为/
     */
    FilePath: function (item) {
        return item.replace(/\\/g, "/");
    }
}

// 禁用F5,退格键
function forbidF5(event) {
    var event, evtnode, key;
    event = event || window.event;
    evtnode = event.srcElement ? event.srcElement : event.target;
    key = event.keyCode || event.which;
    // 116 F5 117 F6 8 BACKSPACE
    if (key == 116 || key == 117 || (key == 8 && evtnode.nodeName != "TEXTAREA" && evtnode.nodeName != "INPUT")) {
        eventstop(event);
    } else {
        return true;
    }
}

function eventstop(event) {
    try {
        event.keyCode = 0;
    } catch (err) {
    }
    try {
        event.preventDefault();
    } catch (err) {
    }
    try {
        event.stopPropagation();
    } catch (err) {
    }
    try {
        event.returnValue = false;
    } catch (err) {
    }
    try {
        event.cancelBubble = true;
    } catch (err) {
    }
    return false;
}

/**
 * Button交互
 *            e
 */
function loadingButton(e) {
    let $btn = $(e);
    $btn.attr("disabled", "disabled");
    $btn.button('loading');
    setTimeout(function () {
            $btn.removeAttr("disabled");
            $btn.button('reset');
        },
        2000);
}

/**
 * 时间倒计时
 *
 * @param {}
 *            x 倒计时最后开始分
 * @param {}
 *            y 倒计时最后开始秒
 * @param {}
 *            element 倒计时显示的元素
 */
function countdown(x, y, element) {
    var d = new Date("1111/1/1,0:" + x + ":" + y);
    var interval = setInterval(function () {
            var m = d.getMinutes();
            var s = d.getSeconds();
            m = m < 10 ? "0" + m : m;
            s = s < 10 ? "0" + s : s;
            $(element).text(m + "分" + s + "秒");
            if (m == 0 && s == 0) {
                clearInterval(interval);
                return;
            }
            d.setSeconds(s - 1);
        },
        1000);
}

/**
 * 将Date转为yyy-MM-dd HH:mm:ss时间格式
 *
 * @param {}
 *            now
 * @return {}
 */
function formatDate(now) {
    var year = now.getFullYear();
    var month = now.getMonth() + 1;
    var date = now.getDate();
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    if (hour >= 1 && hour <= 9) {
        hour = "0" + hour;
    }
    if (minute >= 0 && minute <= 9) {
        minute = "0" + minute;
    }
    if (second >= 1 && second <= 9) {
        second = "0" + second;
    }
    return year + "-" + month + "-" + date + " " + (hour == 0 ? "00" : hour) + ":" + (minute == 0 ? "00" : minute);
}
function formatDate_yyyyMMddHHmmss(date) {
    //var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}
function getGuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

$(function () {
    // 折叠ibox
    $('.collapse-link').click(function () {
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
                ibox.resize();
                ibox.find('[id^=map-]').resize();
            },
            50);
    });

    // 关闭ibox
    $('.close-link').click(function () {
        var content = $(this).closest('div.ibox');
        content.remove();
    });
});

/**
 * 弹出层 title 标题 element 显示的元素 w 弹出层宽度（缺省调默认值） h 弹出层高度（缺省调默认值）
 */
function layer_show(title, element, w, h) {
    if (title == null || title == '') {
        title = false;
    }
    if (w == null || w == '') {
        w = 800;
    }
    if (h == null || h == '') {
        h = ($(window).height() - 50);
    }
    layer.open({
        type: 1,
        area: [w + 'px', h + 'px'],//area: [w + 'px', '90%'],//area: [w , h ],
        shadeClose: true,
        shade: 0.2,
        anim: 1,
        // 0-6的动画形式，-1不开启
        maxmin: true,
        // 开启最大化最小化按钮
        fix: false,
        // 不固定
        scrollbar: false,
        // 屏蔽游览器滚动条
        title: title,
        content: $(element)
    });
}

/**
 * 弹出层 title 标题 url 请求的url w 弹出层宽度（缺省调默认值） h 弹出层高度（缺省调默认值）
 */
function layer_show_url(title, url, w, h) {
    if (title == null || title == '') {
        title = false;
    }
    if (w == null || w == '') {
        w = 800;
    }
    if (h == null || h == '') {
        h = ($(window).height() - 50);
    }
    layer.open({
        type: 2,
        area: [w + 'px', h + 'px'],
        shadeClose: true,
        shade: false,
        anim: 1,
        // 0-6的动画形式，-1不开启
        maxmin: true,
        // 开启最大化最小化按钮
        fix: false,
        // 不固定
        scrollbar: false,
        // 屏蔽游览器滚动条
        title: title,
        content: url
    });
}

/**
 * bootstrapValidator
 *
 * @param event
 * @returns boolean
 */
function validateForm(event) {
    $(event).data("bootstrapValidator").resetForm();
    $(event).bootstrapValidator('validate');
    return $(event).data('bootstrapValidator').isValid();
}

// BootStrapTable公用
// 加载服务器数据之前的处理程序，可以用来格式化数据。参数：data为从服务器请求到的数据。
function defaultBootstarpTableHandler(data) {
    if (data.code != 0) {
        layer.alert(data.msg, {
            icon: 2
        });
        return;
    }
    return {
        "total": data.page.totalCount,
        // 总记录数
        "rows": data.page.list
        // 数据
    };
}

/**
 * BootStrap默认日期格式化
 *
 * @param {}
 *            uuix 时间戳(秒)
 * @return {} yyyy-MM-dd HH:MM
 */
function BootstrapTableformatDate(uuix) {
    if (null == uuix || "" == uuix) {
        return null;
    }
    let now = new Date(uuix * 1000);
    let year = now.getFullYear();
    let month = now.getMonth() + 1;
    let date = now.getDate();
    let hour = now.getHours();
    let minute = now.getMinutes();
    let second = now.getSeconds();
    if (hour >= 1 && hour <= 9) {
        hour = "0" + hour;
    }
    if (minute >= 0 && minute <= 9) {
        minute = "0" + minute;
    }
    if (second >= 1 && second <= 9) {
        second = "0" + second;
    }
    return year + "-" + month + "-" + date + " " + (hour == 0 ? "00" : hour) + ":" + (minute == 0 ? "00" : minute);
}

// JS进度条
// 开始
wstro.progressBarStartUp = function (type) {
    if (typeof NProgress != "undefined") {
        if (type === 1) {
            NProgress.start();
        } else {
            NProgress.inc();
        }
    }
};
// 开始到指定位置 0-1
wstro.progressBarSet = function (speed) {
    if (typeof NProgress != "undefined") {
        NProgress.set(speed);
    }
};
// 结束
wstro.progressBarShutDown = function () {
    if (typeof NProgress != "undefined") {
        NProgress.done();
    }
};

//使用“\”对特殊字符进行转义，除数字字母之外，小于127使用16进制“\xHH”的方式进行编码，大于用unicode（非常严格模式）。
const JavaScriptEncode = function (str) {

    let hex = new Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');

    function changeTo16Hex(charCode) {
        return "\\x" + charCode.charCodeAt(0).toString(16);
    }

    function encodeCharx(original) {
        var found = true;
        var thecharchar = original.charAt(0);
        var thechar = original.charCodeAt(0);
        switch (thecharchar) {
            case '\n':
                return "\\n";
                break; //newline
            case '\r':
                return "\\r";
                break; //Carriage return
            case '\'':
                return "\\'";
                break;
            case '"':
                return "\\\"";
                break;
            case '\&':
                return "\\&";
                break;
            case '\\':
                return "\\\\";
                break;
            case '\t':
                return "\\t";
                break;
            case '\b':
                return "\\b";
                break;
            case '\f':
                return "\\f";
                break;
            case '/':
                return "\\x2F";
                break;
            case '<':
                return "\\x3C";
                break;
            case '>':
                return "\\x3E";
                break;
            default:
                found = false;
                break;
        }
        if (!found) {
            if (thechar > 47 && thechar < 58) { //数字
                return original;
            }

            if (thechar > 64 && thechar < 91) { //大写字母
                return original;
            }

            if (thechar > 96 && thechar < 123) { //小写字母
                return original;
            }

            if (thechar > 127) { //大于127用unicode
                var c = thechar;
                var a4 = c % 16;
                c = Math.floor(c / 16);
                var a3 = c % 16;
                c = Math.floor(c / 16);
                var a2 = c % 16;
                c = Math.floor(c / 16);
                var a1 = c % 16;
                return "\\u" + hex[a1] + hex[a2] + hex[a3] + hex[a4] + "";
            } else {
                return changeTo16Hex(original);
            }

        }
    }

    var preescape = str;
    var escaped = "";
    var i = 0;
    for (i = 0; i < preescape.length; i++) {
        escaped = escaped + encodeCharx(preescape.charAt(i));
    }
    return escaped;
};

/**
 * 将字符转换成HTMLEntites，以对抗XSS
 * https://yq.aliyun.com/articles/49904
 * @param str
 * @returns {string|*|string}
 * @constructor
 */
const HtmlEncode = function (str) {
    let hex = new Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');
    let preescape = str;
    let escaped = "";
    for (let i = 0; i < preescape.length; i++) {
        let p = preescape.charAt(i);
        escaped = escaped + escapeCharx(p);
    }

    return escaped;

    function escapeCharx(original) {
        var found = true;
        var thechar = original.charCodeAt(0);
        switch (thechar) {
            case 10:
                return "<br/>";
                break; //newline
            case 32:
                return "&nbsp;";
                break; //space
            case 34:
                return "&quot;";
                break; //"
            case 38:
                return "&amp;";
                break; //&
            case 39:
                return "&#x27;";
                break; //'
            case 47:
                return "&#x2F;";
                break; // /
            case 60:
                return "&lt;";
                break; //<
            case 62:
                return "&gt;";
                break; //>
            case 198:
                return "&AElig;";
                break;
            case 193:
                return "&Aacute;";
                break;
            case 194:
                return "&Acirc;";
                break;
            case 192:
                return "&Agrave;";
                break;
            case 197:
                return "&Aring;";
                break;
            case 195:
                return "&Atilde;";
                break;
            case 196:
                return "&Auml;";
                break;
            case 199:
                return "&Ccedil;";
                break;
            case 208:
                return "&ETH;";
                break;
            case 201:
                return "&Eacute;";
                break;
            case 202:
                return "&Ecirc;";
                break;
            case 200:
                return "&Egrave;";
                break;
            case 203:
                return "&Euml;";
                break;
            case 205:
                return "&Iacute;";
                break;
            case 206:
                return "&Icirc;";
                break;
            case 204:
                return "&Igrave;";
                break;
            case 207:
                return "&Iuml;";
                break;
            case 209:
                return "&Ntilde;";
                break;
            case 211:
                return "&Oacute;";
                break;
            case 212:
                return "&Ocirc;";
                break;
            case 210:
                return "&Ograve;";
                break;
            case 216:
                return "&Oslash;";
                break;
            case 213:
                return "&Otilde;";
                break;
            case 214:
                return "&Ouml;";
                break;
            case 222:
                return "&THORN;";
                break;
            case 218:
                return "&Uacute;";
                break;
            case 219:
                return "&Ucirc;";
                break;
            case 217:
                return "&Ugrave;";
                break;
            case 220:
                return "&Uuml;";
                break;
            case 221:
                return "&Yacute;";
                break;
            case 225:
                return "&aacute;";
                break;
            case 226:
                return "&acirc;";
                break;
            case 230:
                return "&aelig;";
                break;
            case 224:
                return "&agrave;";
                break;
            case 229:
                return "&aring;";
                break;
            case 227:
                return "&atilde;";
                break;
            case 228:
                return "&auml;";
                break;
            case 231:
                return "&ccedil;";
                break;
            case 233:
                return "&eacute;";
                break;
            case 234:
                return "&ecirc;";
                break;
            case 232:
                return "&egrave;";
                break;
            case 240:
                return "&eth;";
                break;
            case 235:
                return "&euml;";
                break;
            case 237:
                return "&iacute;";
                break;
            case 238:
                return "&icirc;";
                break;
            case 236:
                return "&igrave;";
                break;
            case 239:
                return "&iuml;";
                break;
            case 241:
                return "&ntilde;";
                break;
            case 243:
                return "&oacute;";
                break;
            case 244:
                return "&ocirc;";
                break;
            case 242:
                return "&ograve;";
                break;
            case 248:
                return "&oslash;";
                break;
            case 245:
                return "&otilde;";
                break;
            case 246:
                return "&ouml;";
                break;
            case 223:
                return "&szlig;";
                break;
            case 254:
                return "&thorn;";
                break;
            case 250:
                return "&uacute;";
                break;
            case 251:
                return "&ucirc;";
                break;
            case 249:
                return "&ugrave;";
                break;
            case 252:
                return "&uuml;";
                break;
            case 253:
                return "&yacute;";
                break;
            case 255:
                return "&yuml;";
                break;
            case 162:
                return "&cent;";
                break;
            case '\r':
                break;
            default:
                found = false;
                break;
        }
        if (!found) {
            if (thechar > 127) {
                var c = thechar;
                var a4 = c % 16;
                c = Math.floor(c / 16);
                var a3 = c % 16;
                c = Math.floor(c / 16);
                var a2 = c % 16;
                c = Math.floor(c / 16);
                var a1 = c % 16;
                return "&#x" + hex[a1] + hex[a2] + hex[a3] + hex[a4] + ";";
            } else {
                return original;
            }
        }
    }
};