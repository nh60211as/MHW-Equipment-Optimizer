package damageCalculator;

public class SingleUseDamageCalculator {

	public static void main(String[] args) {
		DamageBoost baseStat = new DamageBoost(190*1.0,0);

		//技能
		DamageBoost[][] skill = new DamageBoost[4][];

		skill[0] = new DamageBoost[4];
		skill[1] = new DamageBoost[6];
		skill[2] = new DamageBoost[8];
		skill[3] = new DamageBoost[8];

		// 弱點特效
		skill[0][0] = new DamageBoost(0,0);
		skill[0][1] = new DamageBoost(0,0.15);
		skill[0][2] = new DamageBoost(0,0.30);
		skill[0][3] = new DamageBoost(0,0.50);

		// 挑戰者
		for(int i=0;i<=skill[1].length-1;i++) {
			skill[1][i] = new DamageBoost(3*i,0.03*i);
		}

		// 攻擊
		for(int i=0;i<=skill[2].length-1;i++) {
			if(i<=3) // 等級3以下
				skill[2][i] = new DamageBoost(3*i,0);
			else
				skill[2][i] = new DamageBoost(3*i,0.05);
		}

		// 看破
		skill[3][0] = new DamageBoost(0,0);
		skill[3][1] = new DamageBoost(0,0.03);
		skill[3][2] = new DamageBoost(0,0.06);
		skill[3][3] = new DamageBoost(0,0.10);
		skill[3][4] = new DamageBoost(0,0.15);
		skill[3][5] = new DamageBoost(0,0.20);
		skill[3][6] = new DamageBoost(0,0.25);
		skill[3][7] = new DamageBoost(0,0.30);

		// 超會心 0~3 級
		double[] criticalEye = {1.25, 1.3, 1.35, 1.4};
		double baseMinusCriticalDamage = 0.75;

		int skill0 = 3;
		int skill1 = 1;
		int skill2 = 4;
		int skill3 = 6;
		int criticalEyeLevel = 3;

		DamageBoost DamageBoostNow = new DamageBoost(0,0);
		DamageBoostNow.add(baseStat);
		DamageBoostNow.add(skill[0][skill0]);
		DamageBoostNow.add(skill[1][skill1]);
		DamageBoostNow.add(skill[2][skill2]);
		DamageBoostNow.add(skill[3][skill3]);

		double attackNow = DamageBoostNow.attack;
		double criticalNow = DamageBoostNow.critical;
		double criticalEyeNow = criticalEye[criticalEyeLevel];

		if(criticalNow<0) {
			criticalNow = -criticalNow;
			criticalEyeNow = baseMinusCriticalDamage;
		}
		else if(criticalNow>1)
			criticalNow = 1;

		double weaponDamage = attackNow * ( criticalNow*criticalEyeNow + (1-criticalNow)*1 );

		System.out.format("傷害： %1f, 會心率：%d", weaponDamage,(int)(criticalNow*100));
		System.out.println();
	}

}
