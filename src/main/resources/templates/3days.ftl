<#import "common.ftl" as c>

<@c.page "3 days weather" "/js/3days.js">
<script type="text/javascript">
window.angularData = {
    <#if cities??>
        cities: ${cities}
    </#if>
};
</script>
<div ng-app="3daysModule" ng-controller="3daysCtrl as vm">
    <div class="header">
        <div class="col-md-offset-10 col-md-2" align="right">
            <h5 style="color: rgba(255,255,255,0.5);font-family: Comic Sans,cursive;">TrueWeather v 0.3</h5></div>
    </div>

    <div class="container" style="width:100%">
        <div class="row">
            <div class="col-md-offset-4 col-md-4" align="center">
                <div class="input-group">
                    <input type="text" class="form-control" style="box-shadow: 0 0 10px rgba(0,0,0,0.4);" id="city"
                           placeholder="Введите город" ng-model="vm.city">
     <span class="input-group-btn">
        <button class="btn btn-success" type="button" ng-click="vm.searchWeather()"
                style="box-shadow: 4px 0 10px rgba(0,0,0,0.4);">Вперед!
        </button>
      </span>
                </div>
            </div>
        </div>

        <div class="row">
            <div ng-cloak class="col-md-offset-4 col-md-4" style="margin-top:5px;text-align: center;"
                 ng-show="vm.cities!=undefined">
                <span ng-repeat="c in vm.cities track by $index">
                     <a href="#" ng-click="vm.go(c)" class="city"><strong>{{c}}</strong></a>{{$last ? '' : ' '}}
                </span>
            </div>
        </div>

        <div class="row">
            <div ng-cloak class="col-md-offset-4 col-md-4" style="margin-top: 1%;text-align: center"
                 ng-show="vm.loadGif">
                <img src="/img/just_vid.gif" class="img-thumbnail" style="opacity: 0.95;" width="70%"/>
            </div>
        </div>

        <div class="row">
            <div ng-cloak align="center" class="col-md-offset-1 col-md-10" style="margin-top: 3%;text-align: left"
                 ng-show="!vm.loadGif">
                <div class="col-md-4" ng-repeat="w in result">
                    <div class="panel panel-default" style="background-color: rgba(255,255,255,0.95)">
                        <div class="panel-heading"><h4><strong>{{w.date}}</strong></h4></div>
                        <div class="panel-body">
                            <table class="table table-hover">
                                <tr>
                                    <th>Время</th>
                                    <th>Температура</th>
                                    <th>Прогноз</th>
                                </tr>
                                <tbody ng-repeat="segment in w.weatherDaySegments">
                                <tr>
                                    <td><h5>{{vm.showPartOfDay($index)}}</h5></td>
                                    <td>
                                        <h3>{{segment.temperature}}
                                            <small>°С</small>
                                        </h3>
                                    </td>
                                    <td><img ng-src="img/{{segment.forecast}}.png" height="30%"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</@c.page>