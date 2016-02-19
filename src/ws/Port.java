package ws;

import java.util.Collection;

import com.dynatrace.diagnostics.pdk.Monitor;
import com.dynatrace.diagnostics.pdk.MonitorEnvironment;
import com.dynatrace.diagnostics.pdk.MonitorMeasure;
import com.dynatrace.diagnostics.pdk.Status;

public class Port extends TCPPortMonitor implements Monitor {

	private static final String METRIC_GROUP = "TCP Port Monitor";
	private static final String MSR_PORT_STATUS = "portStatus";
	private static final String MSR_RESP_TIME = "ResponseTime";

	@Override
	public Status setup(MonitorEnvironment env) throws Exception {
		return super.setup(env);
	}

	@Override
	public Status execute(MonitorEnvironment env) throws Exception {
		Status result = super.execute(env);

		Collection<MonitorMeasure> measures;
		if ((measures = env.getMonitorMeasures(METRIC_GROUP, MSR_PORT_STATUS)) != null) {
			for (MonitorMeasure measure : measures)
				measure.setValue(isMatchRuleSuccess() ? 1 : 0);
	}
		if ((measures = env.getMonitorMeasures(METRIC_GROUP, MSR_RESP_TIME)) != null) {
			for (MonitorMeasure measure : measures)
				measure.setValue(getResponseTime());
		}

		return result;
	}

	@Override
	public void teardown(MonitorEnvironment env) throws Exception {
		super.teardown(env);
	}

}
