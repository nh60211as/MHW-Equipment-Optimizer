package damageCalculator;

import java.util.ArrayList;
import java.util.LinkedList;

public class DamageCalculator {
	public static void main(String[] args) {
		DamageBoost[] baseStat = new DamageBoost[6];
		baseStat[0] = new DamageBoost(190*1.1,0);
		baseStat[1] = new DamageBoost(210*1.0,0);
		baseStat[2] = new DamageBoost(210*1.0,-0.20);
		baseStat[3] = new DamageBoost(190*1.0,0);
		baseStat[4] = new DamageBoost(210*1.1,-0.20);
		baseStat[5] = new DamageBoost(230*1.1,-0.30);

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


		LinkedList<Double> damageList = new LinkedList<Double>();
		LinkedList<ArrayList<Integer>> damageListIndex = new LinkedList<ArrayList<Integer>>();

		damageList.add((double) 0);
		damageListIndex.add(new ArrayList<Integer>());
		int sortSize = 130;

		for(int weapon=0;weapon<=baseStat.length-1;weapon++) 
			for(int skill0=0;skill0<=skill[0].length-1;skill0++) 
				for(int skill1=0;skill1<=skill[1].length-1;skill1++)
					for(int skill2=0;skill2<=skill[2].length-1;skill2++) 
						for(int skill3=0;skill3<=skill[3].length-1;skill3++) 
							for(int criticalEyeLevel=0;criticalEyeLevel<=criticalEye.length-1;criticalEyeLevel++) {
								DamageBoost DamageBoostNow = new DamageBoost(0,0);
								DamageBoostNow.add(baseStat[weapon]);
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
								ArrayList<Integer> index = new ArrayList<Integer>();
								index.add(weapon);
								index.add(skill0);
								index.add(skill1);
								index.add(skill2);
								index.add(skill3);
								index.add(criticalEyeLevel);
								index.add((int) attackNow);
								index.add((int) (criticalNow*100));

								for(int i=0;i<=Math.min(damageList.size()-1, sortSize-1);i++) {
									if(weaponDamage>=damageList.get(i)) {
										damageList.add(i, weaponDamage);
										damageListIndex.add(i, index);
										break;
									}
								}
							}

		for(int i=sortSize-10;i<=sortSize-1;i++) {
			System.out.format("傷害： %1f", damageList.get(i));
			for(Integer indexNow:damageListIndex.get(i))
				System.out.print(", " + indexNow);
			System.out.println();
		}
		
	}
}
