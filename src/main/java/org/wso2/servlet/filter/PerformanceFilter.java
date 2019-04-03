package org.wso2.servlet.filter;

import com.codahale.metrics.*;
import com.codahale.metrics.jmx.JmxReporter;

import javax.servlet.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PerformanceFilter implements Filter {
    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private Timer timer;
    private final JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
    private CircularBuffer circularBuffer;

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Initializing the filter");

        circularBuffer = new CircularBuffer(Integer.parseInt(filterConfig.getInitParameter("iar_window_size")));
        timer = metricRegistry.timer("response_times",
                () -> new Timer(new SlidingTimeWindowArrayReservoir(
                        Integer.parseInt(filterConfig.getInitParameter("window_time_minutes")), TimeUnit.MINUTES)));
        metricRegistry.register("InterArrivalRate",
                (Gauge<Float>) () -> circularBuffer.getIAR());
        jmxReporter.start();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        circularBuffer.add(System.currentTimeMillis());
        final Timer.Context context = timer.time();
        System.out.println("Request came to the filter");
        filterChain.doFilter(servletRequest, servletResponse);
        context.stop();
    }

    public void destroy() {
    }
}
