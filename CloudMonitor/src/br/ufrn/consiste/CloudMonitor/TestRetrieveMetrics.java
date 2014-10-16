package br.ufrn.consiste.CloudMonitor;

public class TestRetrieveMetrics {
	
	public static void main(String args[]){
		
		RetrieveMetrics rmeMetrics = new RetrieveMetrics("http://localhost:8080/VmMonitor/Metrics");
		
		rmeMetrics.getLastMetricsVM();
	}

}
