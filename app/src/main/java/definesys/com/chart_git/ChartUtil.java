package definesys.com.chart_git;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import bean.Device;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartUtil {
    //  Views<==>view<==>line
    private LineChartData lineChartData;    //折线的数据
    private LineChartView lineChartView;    //折线的视图
    private List<Line> lines;               //线条集合
    private List<PointValue> points;        //点的集合
    private Map<String,Integer> colors;     //颜色集合
    private Map<String,List<PointValue>> pointsMap;     //点的线集合
    private int position = 0;
    private Axis axisY, axisX;              //x、y轴线
    private Random random = new Random();

    public ChartUtil(LineChartView lineChartView) {
        this.lineChartView = lineChartView;
    }

    public void init() {
        //初始化变量
        pointsMap = new HashMap<>();
        colors = new HashMap<>();
        points = new ArrayList<>();
        lines = new ArrayList<>();
        axisX = new Axis();
        axisY = new Axis();
        //设置坐标轴
        axisX.setTextSize(12);
        axisX.setLineColor(Color.parseColor("#3F51B5"));
        axisX.setTextColor(Color.parseColor("#3F51B5"));
        axisY.setTextSize(8);
        axisY.setHasLines(true);
        axisY.setLineColor(Color.parseColor("#3F51B5"));
        axisY.setTextColor(Color.parseColor("#3F51B5"));

        lineChartData = initDatas(null);
        lineChartView.setLineChartData(lineChartData);

        //初始化视图
        Viewport port = initViewPort(0, 50);//设置x轴始末位置
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(false);//设置图表是可以交互的（拖拽，缩放等效果的前提）
//        lineChartView.setScrollEnabled(true);//允许滚动
//        lineChartView.setValueTouchEnabled(true);   //允许点击数据点
//        lineChartView.setFocusableInTouchMode(true);
//        lineChartView.setViewportCalculationEnabled(false);
//        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);//允许横向拖动
        lineChartView.startDataAnimation();
    }

    /**
     * x轴起始坐标、结束坐标
     */
    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 0;         //y轴    max
        port.bottom = -99;        //Y轴    min
        port.left = left;       //X轴    min
        port.right = right;     //X轴    max
        return port;
    }


    public void updateLines(List<Device> devices) {

        lines.clear();
        for (int i = 0; i < devices.size(); i++) {

            String mac = devices.get(i).getMAC();

            //根据MAC地址标识《==》新的信号《===》插入hashmap
            //  生成新的信号线，生成这条线的颜色
            //  1.  第一次添加
            //  2.  已有，并且是连续的传输
            //  3.  已有，不连续《====》显示？？？||清空再显示？？？？
            if(!pointsMap.containsKey(mac)) {
                colors.put(mac,Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
                pointsMap.put(mac, new ArrayList<PointValue>());
            }

            //  给坐标表赋值
            pointsMap.get(mac).add(new PointValue(position * 5, devices.get(i).getSingleStrench()));

            //只保存200条数据
            if (pointsMap.get(mac).size() > 200)
                pointsMap.get(mac).remove(0);

            //生成线
            lines.add(addPointToLine(pointsMap.get(mac), mac));
        }

        lineChartView.setLineChartData(initDatas(lines));
        //根据点的横坐标实时变换坐标的视图范围
        Viewport port;
        int x = position * 5;
        if (x > 50) {
            port = initViewPort(x - 50, x);
        } else {
            port = initViewPort(0, 50);
        }
        lineChartView.setCurrentViewport(port);//当前窗口
        lineChartView.setMaximumViewport(port);//最大窗口
        position++;
    }

    /**
     * 初始化表格配置
     */
    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }


    private Line addPointToLine(List<PointValue> points, String mac) {
        //根据新的点的集合画出新的线 颜色      点的样式    是否平滑    显示隐藏“点”
        return new Line(points).setColor(colors.get(mac)).setShape(ValueShape.CIRCLE).setCubic(false).setHasPoints(true).setStrokeWidth(1).setPointRadius(1);
    }
}
