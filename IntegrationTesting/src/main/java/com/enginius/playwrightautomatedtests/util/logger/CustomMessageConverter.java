package com.enginius.playwrightautomatedtests.util.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

public class CustomMessageConverter extends CompositeConverter<ILoggingEvent> {

	private CustomLoggerUtil customLoggerUtil = new CustomLoggerUtil();

	@Override
	protected String transform(ILoggingEvent event, String in) {
		return customLoggerUtil.colorizeMessage(in);
	}
}
