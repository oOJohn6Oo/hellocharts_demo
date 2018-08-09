package definesys.com.chart_git;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import bean.Device;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {
    private LineChartView lineChartView;    //折线的视图
    private Button start;
    private Button end;
    private WheelPicker wheelH;
    private WheelPicker wheelI;
    private ChartUtil chartUtil;
    private Timer timer;
    private boolean isFinish;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("DEMO");
        setContentView(R.layout.activity_main);

        timer = new Timer();
        lineChartView = findViewById(R.id.chart);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        wheelI = findViewById(R.id.wheelI);
        wheelH = findViewById(R.id.wheelH);
        isFinish = true;
        /**
         * 初始化表格工具包
         */
        chartUtil = new ChartUtil(lineChartView);
        chartUtil.init();

        /**
         * wheelH初始化
         */
        List<Integer> heights = new ArrayList<>();
        for (int i = 100; i < 1300; i++)
            heights.add(i);
        wheelH.setData(heights);
        wheelH.setVisibleItemCount(3);
        wheelH.setSelectedItemPosition(980);

        /**
         * wheelI初始化
         */
        List<Integer> inputs = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            inputs.add(i + 1);
        wheelI.setData(inputs);
        wheelI.setVisibleItemCount(3);

        /**
         * 按钮单击事件
         */
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFinish)
                    isFinish = false;
                else{
                    Toast.makeText(MainActivity.this,"已经在运行了哦",Toast.LENGTH_SHORT).show();
                    return;
                }
                count = wheelI.getCurrentItemPosition() + 1;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (isFinish)
                            this.cancel();
                        List<Device> numbers = new ArrayList<>();
                        for (int i = 0; i < count; i++)
                            numbers.add(new Device(i + " ", new Random().nextInt(99) - 99));
                        chartUtil.updateLines(numbers);

                    }
                }, 0, 500);
            }
        });

        /**
         * 结束监控
         */
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFinish = true;
            }
        });
        wheelH.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker wheelPicker, Object o, int i) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) lineChartView.getLayoutParams();
                params.height = i+100;
                lineChartView.setLayoutParams(params);
            }
        });
    }

}
