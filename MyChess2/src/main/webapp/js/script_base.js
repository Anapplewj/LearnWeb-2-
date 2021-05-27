// 几个重要参数的解释:
// userId: 用户登陆时获取到的. (测试阶段可以先写死)
// roomId: 当前这局游戏的房间号. 通过匹配结果获取到
// isWhite: 当前这局游戏是否是白子. 通过匹配结果获取到
// 这三个属性包裹到一个 gameInfo 对象中

// 这个数字应该是登陆后从服务器获取的, 目前在页面写死

gameInfo = {
    userId: myUserId,
    roomId: null,
    isWhite: true,
}

//////////////////////////////////////////////////
// 设定界面显示相关操作
//////////////////////////////////////////////////
function onClick(userId) {
    startMatch(userId);
    // 将按钮设置为不可点击, 并修改文本
    $("#matchButton").attr('disabled',true);
    $("#matchButton").text("匹配中...");
}

function hideMatchButton() {
    $("#matchButton").hide();
}

function setScreenText(me) {
    if (me) {
        $("#screen").text("轮到你落子了!")
    } else {
        $("#screen").text("轮到对方落子了!")
    }
}

//////////////////////////////////////////////////
// 初始化 websocket TODO
//////////////////////////////////////////////////
var webSocket=new WebSocket('ws://127.0.0.1:8080/game/'+gameInfo.userId);

webSocket.onopen=function () {
    console.log("建立连接!");
}

webSocket.onclose=function () {
    console.log("断开连接!");
}

webSocket.onerror=function(){
    console.log("连接异常!");
}

//在不同的流程中的处理方式不同,匹配的请求和落子的请求需要分开处理.
webSocket.onmessage=function (event) {
    var message=event.data;
    if(message=='duplicationLogin'){
        //该用户已经登录过了
        alert("您的账号已经被登录过了");
        $("#matchButton").attr('disabled',true);
        $("#screen").text("您的账号已经被登录过了!");
    }
}

//DOM API
window.onbeforeunload=function () {
    //页面关闭之前,先主动关闭websocket
    webSocket.close();
}
//////////////////////////////////////////////////
// 实现匹配逻辑 TODO
//////////////////////////////////////////////////

//用户点击开始匹配按钮,就会进行匹配
// 这个按钮就是在匹配按钮的点击回调函数中进行调用的

function startMatch(userId){
    var message={
        //在js中操作json是不需要加上引号的
        type:"startMatch",
        userId:'userId'
    };
    //通过下面这个函数,来处理服务器返回的匹配响应
    webSocket.onmessage=handlerStartMatch;
    //将js对象,转成JSON格式的字符串
    webSocket.send(JSON.stringify(message));
}

//当客户端收到服务端处理的匹配请求的响应结果时,就会自动调用
//webSocket.onmessage=hendlerStartMatch;
function handlerStartMatch(event){
    //1.先把服务器响应的数据取出,并解析成js对象
    console.log("handlerStartMatch: "+event.data);
    var response=JSON.parse(event.data);
    if(response.type!='startMatch'){
        console.log("handlerStartMatch: 无效的响应! type:"+response.type);
        return;
    }
    //2.从响应中得到了一些信息,房间id和是否先手
    gameInfo.isWhite=response.isWhite;
    gameInfo.roomId=response.roomId;
    gameInfo.userId=response.userId;
    //3.隐藏匹配按钮
    hideMatchButton();
    //4.设置提示信息,并且提示当前该谁落子了
    setScreenText(gameInfo.isWhite);
    //5.初始化棋盘
    initGame();
}
//////////////////////////////////////////////////
// 匹配成功, 则初始化一局游戏
//////////////////////////////////////////////////
function initGame() {
    // 是我下还是对方下. 根据服务器分配的先后手情况决定
    var me = gameInfo.isWhite;
    // 游戏是否结束
    var over = false;
    var chessBoard = [];
    //初始化chessBord数组(表示棋盘的数组)
    for (var i = 0; i < 15; i++) {
        chessBoard[i] = [];
        for (var j = 0; j < 15; j++) {
            chessBoard[i][j] = 0;
        }
    }
    var chess = document.getElementById('chess');
    var context = chess.getContext('2d');
    context.strokeStyle = "#BFBFBF";
    // 背景图片
    var logo = new Image();
    logo.src = "images/background.jpg";
    logo.onload = function () {
        context.drawImage(logo, 0, 0, 450, 450);
        initChessBoard();
    }

    // 绘制棋盘网格
    function initChessBoard() {
        for (var i = 0; i < 15; i++) {
            context.moveTo(15 + i * 30, 15);
            context.lineTo(15 + i * 30, 430);
            context.stroke();
            context.moveTo(15, 15 + i * 30);
            context.lineTo(435, 15 + i * 30);
            context.stroke();
        }
    }

    // 绘制一个棋子, me 为 true
    function oneStep(i, j, isWhite) {
        context.beginPath();
        context.arc(15 + i * 30, 15 + j * 30, 13, 0, 2 * Math.PI);
        context.closePath();
        var gradient = context.createRadialGradient(15 + i * 30 + 2, 15 + j * 30 - 2, 13, 15 + i * 30 + 2, 15 + j * 30 - 2, 0);
        if (!isWhite) {
            gradient.addColorStop(0, "#0A0A0A");
            gradient.addColorStop(1, "#636766");
        } else {
            gradient.addColorStop(0, "#D1D1D1");
            gradient.addColorStop(1, "#F9F9F9");
        }
        context.fillStyle = gradient;
        context.fill();
    }

    chess.onclick = function (e) {
        if (over) {
            return;
        }
        if (!me) {
            return;
        }
        var x = e.offsetX;
        var y = e.offsetY;
        // 注意, 横坐标是列, 纵坐标是行
        var col = Math.floor(x / 30);
        var row = Math.floor(y / 30);
        if (chessBoard[row][col] == 0) {
            // TODO 新增发送数据给服务器的逻辑
            oneStep(col, row, gameInfo.isWhite);
            chessBoard[row][col] = 1;
            // 通过这个语句控制落子轮次
            // me = !me;
        }
    }

    // TODO 新增处理服务器返回数据的请求
    //      并绘制棋子, 以及判定胜负
}

