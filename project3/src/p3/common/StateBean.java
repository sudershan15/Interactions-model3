package p3.common;

import p3.common.StateBean;

public class StateBean {

	private int stateValue;
	public static volatile StateBean stateBean;
	
	private StateBean() {
		
	}
	
	public static StateBean getInstance(){
		
		if(stateBean == null) {
			synchronized (StateBean.class) {
				if(stateBean == null) {
					stateBean = new StateBean();
					stateBean.setStateValue(1);
				}
			}
		}
		
		return stateBean;
	}
	
	public int getStateValue() {
		return stateValue;
	}

	public void setStateValue(int stateValue) {
		this.stateValue = stateValue;
	}
		
}
