<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <%@include file="style.jsp" %>
    <script type="text/javascript">
        function deal1(){
            var urlStr="${pageContext.request.contextPath}/createTestData";
            var returnStr = $.ajax({
                url: urlStr,
                cache: false,
                async: true
            }).responseText;
            alert("创建测试数据线程执行中");
        }

        function deal2(){
            self.location.href="${pageContext.request.contextPath}/testDownload2";
        }

        function deal3(){
            var urlStr="${pageContext.request.contextPath}/testDownload3";
            var returnStr = $.ajax({
                url: urlStr,
                cache: false,
                async: false
            }).responseText;
            alert(returnStr);
        }

        function doDownload(fileId){
            self.location.href="${pageContext.request.contextPath}/downloadFile?fileId="+fileId;
        }

    </script>
</head>
<body style="margin:6px;">
<div id="dlDiv" class="easyui-accordion" style="width:800px;height:600px;">
    <div title="创建测试数据">
        <input type="button" value="执行" onclick="deal1();">
    </div>
    <div title="下载大容量文件">
        <input type="button" value="执行" onclick="deal3();">
        <table id="bigDataTb" class="easyui-datagrid"
               url="${pageContext.request.contextPath}/listDownloadFile"
               title="下载文件列表" rownumbers="true"
               singleSelect="true" pagination="true">
            <thead>
            <tr>
                <th field="fileId" width="80">文件编号</th>
                <th field="fileStatus" width="80">文件状态</th>
                <th field="startTimeStr" width="150">开始时间</th>
                <th field="finishTimeStr" width="150">结束时间</th>
                <th field="interval" width="150">耗时</th>
                <th field="action" width="100" formatter="actionFmt"></th>
                <script type="text/javascript">
                    function actionFmt(val, row){
                        if (row.fileStatus == "S"){
                            return "<a href='#nulllink' onclick='doDownload("+row.fileId+");'>下载文件</a>";
                        }
                    }
                </script>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>
