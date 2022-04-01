package edu.fra.uas.Team;

public class Team {

	private String name;

	private int basicPower;

	private double currentForm;

	private double powerLvl;

	public Team(String name, int basicPower, int currentForm) {

		this.name = name;
		this.basicPower = basicPower;
		this.currentForm = currentForm;
		double factor = 0;
		factor = (1 + (this.currentForm / 10));
		this.powerLvl = basicPower * factor;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBasicPower() {
		return basicPower;
	}

	public void setBasicPower(int basicPower) {
		this.basicPower = basicPower;
	}

	public double getCurrentForm() {
		return currentForm;
	}

	public void setCurrentForm(double currentForm) {
		this.currentForm = currentForm;
	}

	public double getPowerLvl() {
		return powerLvl;
	}

	public void setPowerLvl(double powerLvl) {
		this.powerLvl = powerLvl;
	}

	@Override
	public String toString() {
		return "Team: " + name + " Basic Power " + basicPower + " currentForm: " + currentForm + " PowerLvl: "
				+ powerLvl;
	}

}
