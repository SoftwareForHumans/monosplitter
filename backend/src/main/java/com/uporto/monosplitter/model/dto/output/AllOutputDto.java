package com.uporto.monosplitter.model.dto.output;

public class AllOutputDto {
	private SystemModelOutputDto systemModel;
	private ServiceCutsOutputDto serviceCuts;
	
	public AllOutputDto() {
		
	}

	public SystemModelOutputDto getSystemModel() {
		return systemModel;
	}

	public void setSystemModel(SystemModelOutputDto systemModel) {
		this.systemModel = systemModel;
	}

	public ServiceCutsOutputDto getServiceCuts() {
		return serviceCuts;
	}

	public void setServiceCuts(ServiceCutsOutputDto serviceCuts) {
		this.serviceCuts = serviceCuts;
	}
	
	
}
