package pcd.ass02.rx_based;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface SourceAnalyser {

	Single<Report> getReport(String directory);

	Flowable<ReportSnapshot>  analyzeSources(String directory);

}
