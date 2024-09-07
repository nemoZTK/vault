package com.enginius.playwrightautomatedtests.phones.deviceprofiles;

public class phones {

	public static class Iphone14Profile extends MobileDeviceConfig {
		public Iphone14Profile() {
			this.userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1";
			this.viewportWidth = 430;
			this.viewportHeight = 932;
			this.deviceScaleFactor = 3.0;
			this.language = "en-US";
			this.timezone = "America/Los_Angeles";
			this.darkMode = false;
			this.latitude = 37.7749;
			this.longitude = -122.4194;
			this.isOnline = true;
			this.osVersion = "iOS 16.0";
			this.orientation = "portrait";
		}
	}

	public static class Iphone8PlusProfile extends MobileDeviceConfig {
		public Iphone8PlusProfile() {
			this.userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";
			this.viewportWidth = 414;
			this.viewportHeight = 736;
			this.deviceScaleFactor = 3.0;
			this.language = "en-US";
			this.timezone = "America/New_York";
			this.darkMode = false;
			this.latitude = 40.7128;
			this.longitude = -74.0060;
			this.isOnline = true;
			this.osVersion = "iOS 11.0";
			this.orientation = "portrait";
		}
	}

	public static class Iphone11Profile extends MobileDeviceConfig {
		public Iphone11Profile() {
			this.userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1";
			this.viewportWidth = 414;
			this.viewportHeight = 896;
			this.deviceScaleFactor = 2.0;
			this.language = "en-US";
			this.timezone = "Europe/London";
			this.darkMode = false;
			this.latitude = 51.5074;
			this.longitude = -0.1278;
			this.isOnline = true;
			this.osVersion = "iOS 14.0";
			this.orientation = "portrait";
		}
	}

	public static class GalaxyTabS7Profile extends MobileDeviceConfig {
		public GalaxyTabS7Profile() {
			this.userAgent = "Mozilla/5.0 (Linux; Android 10; SM-T870) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.117 Safari/537.36";
			this.viewportWidth = 800;
			this.viewportHeight = 1280;
			this.deviceScaleFactor = 1.5;
			this.language = "en-US";
			this.timezone = "Asia/Seoul";
			this.darkMode = false;
			this.latitude = 37.5665;
			this.longitude = 126.9780;
			this.isOnline = true;
			this.osVersion = "Android 10";
			this.orientation = "portrait";
		}
	}

	public static class GalaxyS20Profile extends MobileDeviceConfig {
		public GalaxyS20Profile() {
			this.userAgent = "Mozilla/5.0 (Linux; Android 10; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.117 Mobile Safari/537.36";
			this.viewportWidth = 360;
			this.viewportHeight = 800;
			this.deviceScaleFactor = 3.0;
			this.language = "en-US";
			this.timezone = "Europe/Berlin";
			this.darkMode = false;
			this.latitude = 52.5200;
			this.longitude = 13.4050;
			this.isOnline = true;
			this.osVersion = "Android 10";
			this.orientation = "portrait";
		}
	}

	public static class Pixel7Profile extends MobileDeviceConfig {
		public Pixel7Profile() {
			this.userAgent = "Mozilla/5.0 (Linux; Android 6; Pixel 7 XL) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36";
			this.viewportWidth = 412;
			this.viewportHeight = 915;
			this.deviceScaleFactor = 3.0;
			this.language = "en-US";
			this.timezone = "Australia/Sydney";
			this.darkMode = true;
			this.latitude = -33.8688;
			this.longitude = 151.2093;
			this.isOnline = true;
			this.osVersion = "Android 12";
			this.orientation = "portrait";
		}
	}

}