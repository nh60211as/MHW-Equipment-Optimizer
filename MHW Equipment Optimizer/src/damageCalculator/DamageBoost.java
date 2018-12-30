package damageCalculator;

public class DamageBoost {
	double attack;
	double critical;
	
	public DamageBoost(double attack, double critical) {
		this.attack = attack;
		this.critical = critical;
	}
	
	public void add(DamageBoost input) {
		this.attack += input.attack;
		this.critical += input.critical;
	}
}
