package com.splitbill.metrics;

import com.splitbill.metrics.config.PrometheusConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(PrometheusConfig.class)
public class MetricsAutoConfiguration {
}
