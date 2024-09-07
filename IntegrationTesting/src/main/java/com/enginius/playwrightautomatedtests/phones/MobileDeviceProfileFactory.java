package com.enginius.playwrightautomatedtests.phones;

import com.enginius.playwrightautomatedtests.phones.deviceprofiles.MobileDeviceConfig;
import com.enginius.playwrightautomatedtests.phones.deviceprofiles.phones.GalaxyS20Profile;
import com.enginius.playwrightautomatedtests.phones.deviceprofiles.phones.GalaxyTabS7Profile;
import com.enginius.playwrightautomatedtests.phones.deviceprofiles.phones.Iphone11Profile;
import com.enginius.playwrightautomatedtests.phones.deviceprofiles.phones.Iphone14Profile;
import com.enginius.playwrightautomatedtests.phones.deviceprofiles.phones.Iphone8PlusProfile;
import com.enginius.playwrightautomatedtests.phones.deviceprofiles.phones.Pixel7Profile;

public class MobileDeviceProfileFactory {
	public static MobileDeviceConfig createProfile(String profileName) {
		MobileDeviceConfig profile;

		if (profileName == null || profileName.isEmpty()) {
			profile = new MobileDeviceConfig(); // Profilo predefinito
		} else {
			switch (profileName.toLowerCase()) {
				case "iphone14":
					profile = new Iphone14Profile();
					break;
				case "iphone8plus":
					profile = new Iphone8PlusProfile();
					break;
				case "iphone11":
					profile = new Iphone11Profile();
					break;
				case "galaxytabs7":
					profile = new GalaxyTabS7Profile();
					break;
				case "galaxys20":
					profile = new GalaxyS20Profile();
					break;
				case "pixel7":
					profile = new Pixel7Profile();
					break;
				default:
					throw new IllegalArgumentException("Profilo non supportato: " + profileName);
			}
		}

		return profile;
	}
}
