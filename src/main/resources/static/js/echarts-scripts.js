// 使用 ECharts
$(document).ready(function() {
        // 基于准备好的dom，初始化echarts实例
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
            data: xAxisData
          },
          yAxis: {
          },
          series: [
            {
              name: '销量',
              type: 'bar',
              data: seriesData
            }
          ]
        };

        // 使用刚指定的配置项和数据显示图表。
        histogram.setOption(option);
});
