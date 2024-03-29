package com.coin2012.wikipulse.DomainModel;

/**
 * This class represents a edit from Wikipedia.
 *  
 * @author Stefan
 *
 */
public class WikiEdit {
	private String revid;
	private String parentid;
	private String user;
	private String timestamp;
	private String comment;
	private String content;

	public String getRevid() {
		return revid;
	}

	public void setRevid(String revid) {
		this.revid = revid;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
