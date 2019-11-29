//路径连接符
var nextPath = "/";
//获取项目路径
var pathName = document.location.pathname;
var pathLength = pathName.substr(1).indexOf(nextPath) + 1;
var pathHead = pathName.substr(0, pathLength) + nextPath;