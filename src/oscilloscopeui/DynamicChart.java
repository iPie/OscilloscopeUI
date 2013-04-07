package oscilloscopeui;

import info.monitorenter.gui.chart.*;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.axis.*;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.util.Range;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

/**
 *
 * @author Ajarax
 */
public class DynamicChart {

    private Chart2D chart;
    private ITrace2D trace;
    private Date startTime;

    public DynamicChart() {
        initilizeChart();
    }

    public DynamicChart(JPanel panel) {
        initilizeChart();
        addChartToPanel(panel);
    }

    public void startPlotting() {
        startTime = new Date();
    }

    public void stopPlotting() {
        chart.removeAllTraces();
    }

    public void addXY(double x, double y) {
        trace.addPoint(x, y);
    }

    public void addY(double y) {
        Date now = new Date();
        trace.addPoint((now.getTime() - startTime.getTime()), y);
    }

    @Deprecated
    public void startTestDrive() {
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {

            private double m_y = 0;
            private long m_starttime = System.currentTimeMillis();

            @Override
            public void run() {
                double rand = Math.random();
                boolean add = (rand >= 0.5) ? true : false;
                this.m_y = (add) ? this.m_y + Math.random() : this.m_y - Math.random();
                trace.addPoint(((double) System.currentTimeMillis() - this.m_starttime), this.m_y);
            }
        };
        timer.schedule(task, 1000, 20);
    }

    private void initilizeChart() {
        chart = new Chart2D();
        trace = new Trace2DLtd(5000);
        trace.setName("Channel 1");
        trace.setPhysicalUnits("mSecs", "Volts");
        chart.setUseAntialiasing(true);
        chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(0, 2.56)));
        trace.setColor(Color.RED);
        chart.addTrace(trace);
        chart.getAxisY().setPaintGrid(true);
    }

    private void addChartToPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());
        panel.add(chart);
        panel.updateUI();
    }
}
