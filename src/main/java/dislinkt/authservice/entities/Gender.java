package dislinkt.authservice.entities;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
	FEMALE(0), MALE(1), OTHER(2);

	private int value;
	private static Map map = new HashMap<>();

	private Gender(int value) {
		this.value = value;
	}

	static {
		for (Gender gender : Gender.values()) {
			map.put(gender.value, gender);
		}
	}

	public static Gender valueOfInt(int gender) {
		return (Gender) map.get(gender);
	}

	public int getValue() {
		return value;
	}
}
