package fr.kosmosuniverse.kuffle.Core;

public class RewardElem {
	private String name;
	private Integer amount;
	private Boolean enchantOn = false;
	private String enchant = null;
	private Integer level;
	private Boolean effectOn = false;
	private String effect = null;
	
	public RewardElem(String _name, Integer _amount, String _enchant, Integer _level, String _effect) {
		name = _name;
		amount = _amount;
		if (_enchant != null && !_enchant.equals("")) {
			enchantOn = true;
			enchant = _enchant;
			level = _level;
		}
		
		if (_effect != null && !_effect.equals("")) {
			effectOn = true;
			effect = _effect;
		}
	}
	
	public String getName() {
		return (name);
	}
	
	public Integer getAmount() {
		return (amount);
	}
	
	public Boolean enchant() {
		return (enchantOn);
	}
	
	public Boolean effect() {
		return (effectOn);
	}
	
	public Integer getLevel() {
		return (level);
	}
	
	public String getEnchant() {
		return (enchant);
	}
	
	public String getEffect() {
		return (effect);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Name: [" + name + "]").append("\n");
		sb.append("Amount: [" + amount + "]").append("\n");
		if (enchantOn) {
			sb.append("Enchant: [" + enchant + "]").append("\n");
			sb.append("Level: [" + level + "]").append("\n");
		}
		
		if (effectOn) {
			sb.append("Effect: [" + effect + "]").append("\n");
		}
	
		return (sb.toString());
	}
}