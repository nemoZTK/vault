package com.enginius.playwrightautomatedtests.phones.deviceprofiles;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.ColorScheme;

public class MobileDeviceConfig {
	protected String userAgent;
	protected int viewportWidth;
	protected int viewportHeight;
	protected double deviceScaleFactor;
	protected String language;
	protected String timezone;
	protected boolean darkMode;
	protected double latitude;
	protected double longitude;
	protected boolean isOnline;
	protected String osVersion;
	protected String orientation;

	public Browser.NewContextOptions setDefaultContextOptions() {
		ColorScheme colorScheme = (darkMode) ? ColorScheme.DARK : ColorScheme.LIGHT;
		return new Browser.NewContextOptions().setViewportSize(viewportWidth, viewportHeight).setUserAgent(userAgent)
				.setDeviceScaleFactor(deviceScaleFactor).setIsMobile(true).setLocale(language).setTimezoneId(timezone)
				.setOffline(!isOnline).setGeolocation(latitude, longitude).setColorScheme(colorScheme)
				.setHasTouch(true);
	}

	public void changeOrientation(Page page) {
		if ("landscape".equals(orientation)) {
			page.evaluate("window.screen.orientation.lock('landscape');");
		} else if ("portrait".equals(orientation)) {
			page.evaluate("window.screen.orientation.lock('portrait');");
		}
	}

	public void changeOsVersion(Page page, String osVersion) {
		if (osVersion != null) {
			page.evaluate("navigator.oscpu = '" + osVersion + "';");
		}
	}

	public Browser.NewContextOptions setCustomContextOptions(String language, String timezone, Boolean darkMode,
			Double latitude, Double longitude, Boolean isOnline, String osVersion, String orientation) {

		if (language != null) {
			this.language = language;
		}
		if (timezone != null) {
			this.timezone = timezone;
		}
		if (darkMode != null) {
			this.darkMode = darkMode;
		}
		if (latitude != null) {
			this.latitude = latitude;
		}
		if (longitude != null) {
			this.longitude = longitude;
		}
		if (isOnline != null) {
			this.isOnline = isOnline;
		}
		if (osVersion != null) {
			this.osVersion = osVersion;
		}
		if (orientation != null) {
			this.orientation = orientation;
		}
		return setDefaultContextOptions();
	}

}