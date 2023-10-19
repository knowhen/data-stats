$(document).ready(function() {
    // 使用Thymeleaf表达式将JSON数据解析为JavaScript对象
    // var packageData = JSON.parse(jsonData);

    // 从packageData中提取套餐名称和销售量
    var bundles = [];
    var sales = [];
    for (var i = 0; i < stats.length; i++) {
        bundles.push(stats[i].name);
        sales.push(stats[i].quantity);
    }

    var histogram = echarts.init(document.getElementById('histogram'));

    // 指定图表的配置项和数据
    var option = {
      title: {
        text: '销量统计'
      },
      tooltip: {},
      legend: {
        data: ['销量']
      },
      xAxis: {
        data: bundles
      },
      yAxis: {
      },
      series: [
        {
          name: '销量',
          type: 'bar',
          data: sales
        }
      ]
    };

    // 使用刚指定的配置项和数据显示图表。
    histogram.setOption(option);
});