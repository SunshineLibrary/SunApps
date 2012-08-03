package com.ssl.curriculum.math.logic;

import com.ssl.curriculum.math.listener.ActivityDataReceiver;
import com.ssl.curriculum.math.listener.EdgeReceiver;
import com.ssl.curriculum.math.listener.PageFlipListener;
import com.ssl.curriculum.math.model.ActivityStatus;
import com.ssl.curriculum.math.model.Edge;
import com.ssl.curriculum.math.model.activity.DomainActivityData;
import com.ssl.curriculum.math.presenter.MainActivityPresenter;
import com.ssl.curriculum.math.service.ActivityContentProvider;
import com.ssl.curriculum.math.service.EdgeContentProvider;
import com.ssl.curriculum.math.task.FetchActivityDataTask;
import com.ssl.curriculum.math.task.FetchEdgeTask;

import java.util.ArrayList;

public class ActivityFlowController implements EdgeReceiver, ActivityDataReceiver, PageFlipListener {
	private ArrayList<DomainActivityData> domainActivityStack = new ArrayList<DomainActivityData>();

	private int currentPosition = -1;

	public ActivityStatus curStatus;

	public MainActivityPresenter presenter;
    private EdgeContentProvider edgeContentProvider;
    private ActivityContentProvider activityContentProvider;
    private ArrayList<Edge> edges;
    private int currentActivityId;
    private int currentSectionId;

    public ActivityFlowController(MainActivityPresenter presenter, EdgeContentProvider edgeContentProvider, ActivityContentProvider activityContentProvider){
		this.presenter = presenter;
        this.presenter.setPageFlipListener(this);
        this.edgeContentProvider = edgeContentProvider;
        this.activityContentProvider = activityContentProvider;
    }
	
	public void loadDomainActivityData(int domainSectionId, int domainActivityId){
        this.currentActivityId = domainSectionId;
        currentSectionId = domainActivityId;
        startLoadTask(domainSectionId, domainActivityId);
	}

    private void startLoadTask(int domainSectionId, int domainActivityId) {
        DomainActivityData pseudoDataDomain = new DomainActivityData(domainSectionId, domainActivityId);
        pseudoDataDomain.activityId = domainActivityId;
        FetchEdgeTask task = new FetchEdgeTask(this.edgeContentProvider, this, pseudoDataDomain);
        task.execute();
    }

    @Override
	public void onShowNext() {
		if(currentPosition == domainActivityStack.size() - 1){
			FetchEdgeTask task = new FetchEdgeTask(edgeContentProvider, this, this.domainActivityStack.get(this.currentPosition));
			task.execute();
		}else{
			currentPosition++;
			presenter.present(this.domainActivityStack.get(currentPosition),1);
		}
	}
	
	@Override
	public void onShowPrevious(){
		if(currentPosition > 0){
			currentPosition --;
			presenter.present(domainActivityStack.get(currentPosition),-1);
		}else{
			/** 
			 * We are at the start of the stack (download unit) and cannot go back further without extra measures
			 * (Such as loading a new section or the such)
			 */
		}
	}


    public boolean isPassToNextEdge(String condition, ActivityStatus curStatus){
		return true;
	}
	
	@Override
	public void onReceivedActivityData(DomainActivityData dataDomain){
		domainActivityStack.add(dataDomain);
		currentPosition++;
		presenter.present(dataDomain, 1);
	}
	
	@Override
	public void onReceivedEdges(ArrayList<Edge> edges) {
        this.edges = edges;
        if (edges == null || edges.size() == 0) {
            return;
        }
        fetchActivity(currentActivityId);
    }

    private void fetchActivity(int currentActivityId) {
        FetchActivityDataTask task = new FetchActivityDataTask(this, activityContentProvider, currentActivityId, currentSectionId);
        task.execute();
    }
}
