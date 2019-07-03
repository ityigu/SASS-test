<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../base.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->

</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <section class="content-header">
        <h1>
            统计分析
            <small>在线人数统计</small>
        </h1>
    </section>
    <section class="content">
        <div class="box box-primary">
            <div id="main" style="width: 600px;height:400px;"></div>
        </div>
    </section>
</div>
</body>

<script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="${ctx}/plugins/echarts/echarts.min.js"></script>
<script type="text/javascript">
    //基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    //2.发送ajxs请求
    var url = "${pageContext.request.contextPath}/stat/findOnlineData.do";
    $.get(url,function (date) {
        //3.处理数据，展示折线图
        var time = [];
        var count = [];
        for (var i = 0;i<date.length;i++){
            time[i] = date[i].时间;
            count[i]=date[i].次数;
        }

        var option = {
            xAxis: {
                type: 'category',
                data: time
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: count,
                type: 'line'
            }]
        };
        myChart.setOption(option);
    })
    
</script>

</html>