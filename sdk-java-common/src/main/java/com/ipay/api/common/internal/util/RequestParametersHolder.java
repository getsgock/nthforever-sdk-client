package com.ipay.api.common.internal.util;

public class RequestParametersHolder {
	private IpayHashMap protocalMustParams;
	private IpayHashMap protocalOptParams;
	private IpayHashMap applicationParams;

	public IpayHashMap getProtocalMustParams() {
		return protocalMustParams;
	}
	public void setProtocalMustParams(IpayHashMap protocalMustParams) {
		this.protocalMustParams = protocalMustParams;
	}
	public IpayHashMap getProtocalOptParams() {
		return protocalOptParams;
	}
	public void setProtocalOptParams(IpayHashMap protocalOptParams) {
		this.protocalOptParams = protocalOptParams;
	}
	public IpayHashMap getApplicationParams() {
		return applicationParams;
	}
	public void setApplicationParams(IpayHashMap applicationParams) {
		this.applicationParams = applicationParams;
	}
	
}
