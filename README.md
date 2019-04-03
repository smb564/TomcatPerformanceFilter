# Tomcat Filter for Measuring Performance
A filter for measuring tomcat performance using Dropwizzard metrics and report inter-arrival rates through JMX. This was specifically designed for the purpose of observing server performance for parameter tuinig. However, you can use this for other use cases with slight modifications.

## Instructions
Run the following command to build the product.<br />
<code>mvn clean install</code>

This will create a single jar (<code>tomcat-perf-filter-1.0-SNAPSHOT.jar</code>) consisting of all the dependicies in the <code>target</code> folder.

Copy this jar into <code>{tomcat-directory}/lib</code> folder

Then go to the corresponding web.xml (usually located at <code>{tomcat-directory}/webapps/{app-name}/WEB-INF/web.xml</code>) for the correct servlet application and add the following lines as shown below.

```xml
<web-app>
----- other definitions -----

   <filter>
        <filter-name>PerformanceFilter</filter-name>
        <filter-class>org.wso2.servlet.filter.PerformanceFilter</filter-class>
        <init-param>
            <!-- Inter Arrival Rate request window size for average calculation -->
            <param-name>iar_window_size</param-name>
            <param-value>1000</param-value>
        </init-param>
        <init-param>
            <!-- Duration (time window) for which performance numbers are measure -->
            <param-name>window_time_minutes</param-name>
            <param-value>5</param-value>
        </init-param>
    </filter>

    <filter-mapping>
	    <filter-name>PerformanceFilter</filter-name>
        <url-pattern>*</url-pattern>
    </filter-mapping>
</web-app>
```


Now it should be good to go!
