$(function () {
    var web_socket = null;
    var could_click_chessboard = false;

    function initWebSocket() {
        // 初始化web_socket
        var roomName = document.getElementById("room_name").value;
        var userName = document.getElementById("user_name").value;
        //判断浏览器是否支持web_socket
        if ('WebSocket' in window) {
            var url = "ws://" + document.location.host + "/webSocket/" + roomName + "/" + userName;
            console.log(url);
            web_socket = new WebSocket(url);
            web_socket.onopen = function () {
            }
            web_socket.onclose = function () {
                showConnectDiv();
            }
            web_socket.onerro = function () {
                showConnectDiv();
            }
            web_socket.onmessage = function (message) {
                doMessage(message.data)
            }
        } else {
            alert("浏览器不支持。。。")
        }
    }

    //关闭连接
    function close() {
        if (web_socket != null) {
            web_socket.close()
        }
    }

    //显示棋盘
    function showChessDiv() {
        $("#join_div").hide();
        $("#chess_div").show();
    }

    //显示入口
    function showConnectDiv() {
        $("#chess_div").hide();
        $("#join_div").show();
    }

    //加入房间
    $("#join_room").click(function () {
        var roomName = document.getElementById("room_name").value;
        if (roomName.length != 3) {
            alert("输入3位数字房间号！");
            return;
        }
        var userName = document.getElementById("user_name").value.trim();
        if (userName.length == 0) {
            alert("输入您的游戏名称！");
            return;
        }
        if (userName.length > 8) {
            alert("您的游戏名称过长！");
            return;
        }
        console.log("加入房间")
        initWebSocket();
    });

    //黑方点击开始
    $("#chess_start").click(function () {
        var room_name = document.getElementById("room_name").value;
        if (web_socket != null) {
            var message = '{"actionType":"0","roomName":"' + room_name + '"}';
            web_socket.send(message);
        }
    });

    //点击棋盘
    $(".chess-click-th").click(function () {
        if (could_click_chessboard) {
            var do_point = $(this).prop('id');
            //获取要落子的div，如果未落过子（无id），则继续
            var div_id = $("#" + do_point).find("div").prop("id");
            if (div_id.length == 0) {
                could_click_chessboard = false;
                var room_name = document.getElementById("room_name").value;
                if (web_socket != null) {
                    var message = '{"actionType":"1","roomName":"' + room_name + '","doPoint":"' + do_point + '"}';
                    web_socket.send(message);
                }
            }
        }
    });

    //处理服务器消息
    function doMessage(data) {
        var obj = JSON.parse(data);
        //加入房间
        if (obj.messageType == 0) {
            doRoomInfo(obj);
        }
        //开启对局
        if (obj.messageType == 1) {
            doStart(obj);
        }
        //下棋
        if (obj.messageType == 2) {
            doPointIsWin(obj);
        }
    }

    //处理加入房间信息
    function doRoomInfo(obj) {
        var is_joined = obj.isJoined;
        if (is_joined == 1) {
            var room_name = obj.roomName;
            var black_user_name = obj.blackName;
            var white_user_name = obj.whiteName;
            showChessDiv();
            $("#room_name_joined").text("房间号：" + room_name);
            $("#user_name_black").text(black_user_name);
            $("#user_name_white").text(white_user_name);
            if (white_user_name.length > 0) {
                $("#chess_waiting").hide();
                $("#chess_start").show();
            }
            var self_name = document.getElementById("user_name").value;
            if (self_name == black_user_name) {
                $("#user_type").val("1");
                $("#chess_start").text("开始");
                $("#chess_start").removeAttr("disabled");
                $("#user_name_black").addClass("self-name-color");
                $("#user_name_white").removeClass("self-name-color");
            }
            if (self_name == white_user_name) {
                $("#user_type").val("0");
                $("#chess_start").text("等待开始");
                $("#chess_start").prop("disabled", "disabled");
                $("#user_name_white").addClass("self-name-color");
                $("#user_name_black").removeClass("self-name-color");
            }
        }
        if (is_joined == 0) {
            alert("加入失败！");
        }
        if (is_joined == -1) {
            alert("房间已满！");
        }
    }

    //判断是否开启了对局
    function doStart(obj) {
        var chess_is_start = obj.isStart;
        if (chess_is_start == 1) {
            $("#user_thinking_black").show();
            $("#chess_start").hide();
            $("#chess_table").show();
            var user_type = document.getElementById("user_type").value;
            if (user_type == 1) {
                could_click_chessboard = true;
            }
        }
    }

    //判断落子胜负
    function doPointIsWin(obj) {
        var chess_is_win = obj.isWin;
        if (chess_is_win != -1) {
            var chess_color = obj.userType;
            var chess_point = obj.doPoint;
            //落子的div添加id
            $("#" + chess_point).find("div").prop("id","div_"+chess_point);
            //处理落子
            if (chess_color == 0) {
                $("#" + chess_point).find("div").addClass("chess-white-point");
                $("#user_thinking_white").hide();
                $("#user_thinking_black").show();
            }
            if (chess_color == 1) {
                $("#" + chess_point).find("div").addClass("chess-black-point");
                $("#user_thinking_black").hide();
                $("#user_thinking_white").show();
            }
            var user_type = document.getElementById("user_type").value;
            if (user_type != chess_color) {
                could_click_chessboard = true;
            }
            if (chess_is_win == 1) {
                var user_name_is_win = chess_color == 0 ? "白" : "黑";
                alert(user_name_is_win + "方胜！");
                could_click_chessboard = false;
                $(".thinking-p").hide();
                $("#leave_room").show();
            }
        }
    }

    //离开房间
    $("#leave_room").click(function () {
        close();
        $("#leave_room").hide();
        $("#chess_start").hide();
        $("#chess_table").hide();
        $(".thinking-p").hide();
        $("#chess_waiting").show();
    });

    //页面关闭之前
    window.onbeforeunload = function () {
        close();
    }
});