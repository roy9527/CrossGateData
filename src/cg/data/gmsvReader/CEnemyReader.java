package cg.data.gmsvReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cg.base.util.MathUtil;
import cg.data.item.DropItemGroup;
import cg.data.item.DropItemGroup.DropItem;
import cg.data.limitValue.LimitValueFactory;
import cg.data.limitValue.LimitValueOfShort;
import cg.data.resource.ObjectReader;
import cg.data.resource.ProjectData;
import cg.data.sprite.EnemyInfo;

public class CEnemyReader implements ObjectReader<EnemyInfo> {

	@Override
	public List<EnemyInfo> read(ProjectData projectData) {
		String[] lines = projectData.getTextResource("enemy");
		List<EnemyInfo> list = new ArrayList<EnemyInfo>(lines.length);
		for (String line : lines) {
			list.add(new CEnemyInfo(line));
		}
		return list;
	}
	
	private static class CEnemyInfo implements EnemyInfo {
		
		private static final byte DROP_ITEM_COUNT = 10;
		
		private static final byte STEAL_ITEM_COUNT = 5;
		
		private String name;
		
		private String[] aiParams;
		
		private int enemyId, enemybaseId, aiId, gainExp, battleScore, enemyTalk;
		
		private LimitValueOfShort level, amount;
		
		private boolean canCatch;
		
		private DropItemGroup dropItems, stealItems;
		
		private byte[] actionCode;
		
		private int[] summonCode;
		
		public CEnemyInfo(String line) {
			String[] infos = line.split("\t", -2);
			name = infos[0];
			aiParams = infos[1].split(";");
			enemyId = MathUtil.stringToInt(infos[2]);
			enemybaseId = MathUtil.stringToInt(infos[3]);
			LimitValueFactory limitValueFactory = LimitValueFactory.getInstance();
			level = limitValueFactory.createLimitValue(MathUtil.stringToShort(infos[4]), MathUtil.stringToShort(infos[5]));
			amount = limitValueFactory.createLimitValue(MathUtil.stringToShort(infos[4]), MathUtil.stringToShort(infos[5]));
			aiId = MathUtil.stringToInt(infos[8]);
			gainExp = MathUtil.stringToInt(infos[9]);
			battleScore = MathUtil.stringToInt(infos[10]);
			// 11
			canCatch = infos[12].equals("1");
			List<DropItem> list = new LinkedList<DropItem>();
			for (int i = 0;i < DROP_ITEM_COUNT;i++) {
				list.add(new CDropItem(MathUtil.stringToInt(infos[13 + i]), MathUtil.stringToInt(infos[23 + i], 100)));
			}
			dropItems = new CDropItemGroup(list.toArray(new DropItem[list.size()]));
			list.clear();
			for (int i = 0;i < STEAL_ITEM_COUNT;i++) {
				list.add(new CDropItem(MathUtil.stringToInt(infos[33 + i]), MathUtil.stringToInt(infos[38 + i], 100)));
			}
			stealItems = new CDropItemGroup(list.toArray(new DropItem[list.size()]));
			actionCode = new byte[]{MathUtil.stringToByte(infos[43]), MathUtil.stringToByte(infos[44])};
			summonCode = new int[]{MathUtil.stringToInt(infos[45], -1), MathUtil.stringToInt(infos[46], -1)};
			enemyTalk = MathUtil.stringToInt(infos[47]);
		}

		@Override
		public String getEnemyName() {
			return name;
		}

		@Override
		public int getEnemyId() {
			return enemyId;
		}

		@Override
		public int getEnemyBaseId() {
			return enemybaseId;
		}

		@Override
		public int getAiId() {
			return aiId;
		}

		@Override
		public int getGainExp() {
			return gainExp;
		}

		@Override
		public DropItemGroup getDropItems() {
			return dropItems;
		}

		@Override
		public DropItemGroup getStealItems() {
			return stealItems;
		}

		@Override
		public byte[] getActionCode() {
			return actionCode;
		}

		@Override
		public int[] getSummonCode() {
			return summonCode;
		}

		@Override
		public int getEnemyTalk() {
			return enemyTalk;
		}

		@Override
		public LimitValueOfShort getLevelRange() {
			return level;
		}

		@Override
		public LimitValueOfShort getAmount() {
			return amount;
		}

		@Override
		public String[] getAiParams() {
			return aiParams;
		}

		@Override
		public int getBattleScore() {
			return battleScore;
		}

		@Override
		public boolean getCanCatch() {
			return canCatch;
		}
		
	}

	@Override
	public void output(File outFile, Collection<EnemyInfo> collection)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}

class CDropItem implements DropItem {
	
	private int itemId, rate;
	
	public CDropItem(int itemId, int rate) {
		this.itemId = itemId;
		this.rate = rate;
	}

	public int getItemId() {
		return itemId;
	}

	public int getRate() {
		return rate;
	}

	@Override
	public int getMaxCount() {
		return MIN_COUNT;
	}

}

class CDropItemGroup implements DropItemGroup {
	
	private DropItem[] dropItems;
	
	private int totalRate;
	
	public CDropItemGroup(DropItem[] dropItems) {
		this.dropItems = dropItems;
		for (DropItem dropItem : dropItems) {
			totalRate += dropItem.getRate();
		}
	}

	@Override
	public DropItem[] getDropItems() {
		return dropItems;
	}

	@Override
	public int getTotalRate() {
		return totalRate;
	}
	
}
