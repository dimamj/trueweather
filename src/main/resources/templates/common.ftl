<#macro page title>
<!DOCTYPE html>
<html manifest="cache.appcache">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="/css/styles.css"/>
    <link rel="stylesheet" href="/bower-libs/bootstrap/dist/css/bootstrap.min.css"/>
</head>
<body >
    <#nested>
<script src="/bower-libs/angular/angular.min.js"></script>
<script src="/bower-libs/jquery/dist/jquery.min.js"></script>
<script src="/bower-libs/bootstrap/dist/js/bootstrap.min.js"></script>
</body>
</html>
</#macro>
