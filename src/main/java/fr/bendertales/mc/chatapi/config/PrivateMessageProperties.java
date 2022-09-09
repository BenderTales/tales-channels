package fr.bendertales.mc.chatapi.config;

public class PrivateMessageProperties {

	private String consoleFormat;
	private String senderIsYouFormat;
	private String senderIsOtherFormat;

	public String getConsoleFormat() {
		return consoleFormat;
	}

	public void setConsoleFormat(String consoleFormat) {
		this.consoleFormat = consoleFormat;
	}

	public String getSenderIsYouFormat() {
		return senderIsYouFormat;
	}

	public void setSenderIsYouFormat(String senderIsYouFormat) {
		this.senderIsYouFormat = senderIsYouFormat;
	}

	public String getSenderIsOtherFormat() {
		return senderIsOtherFormat;
	}

	public void setSenderIsOtherFormat(String senderIsOtherFormat) {
		this.senderIsOtherFormat = senderIsOtherFormat;
	}
}
