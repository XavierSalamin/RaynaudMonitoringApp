package fr.hes.raynaudmonitoring.backend.backend.model;

public class Crisis {
	
	
	private int id;
	private String type;
	private int hourStart;
	private int hourEnd;
	
	
	public Crisis(){
	}
	
	public Crisis(int id, String type, int hourStart, int hourEnd) {
		this.id=id;
		this.type=type;
		this.hourStart=hourStart;
		this.hourEnd=hourEnd;
	}


	

	
	
	
	

}
