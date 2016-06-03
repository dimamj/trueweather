<#macro page title>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${title}</title>
    <link rel="stylesheet" href="/css/styles.css"/>
    <link rel="stylesheet" href="/bower-libs/bootstrap/dist/css/bootstrap.min.css"/>
</head>
<body >
<script src="/bower-libs/angular/angular.min.js"></script>
<script src="/bower-libs/jquery/dist/jquery.min.js"></script>
<script src="/bower-libs/bootstrap/dist/js/bootstrap.min.js"></script>
    <#nested>
</body>
</html>
</#macro>
