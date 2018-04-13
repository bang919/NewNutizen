package com.nutizen.nu.bean.response;


import java.io.Serializable;
import java.util.List;


public class AdvertisementBean implements Serializable {

	/**
	 * action : start_picture_ad
	 * picture_array : [{"url":"https://vmse.nutizen.asia/devapi/media/img/20180327143238_default_img.jpg?advertisement=42&advertisers=15&plan=9","ad_id":42,"position":"TopLeft","delay":1,"time":1}]
	 */

	private String action;
	private List<PictureArrayBean> picture_array;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<PictureArrayBean> getPicture_array() {
		return picture_array;
	}

	public void setPicture_array(List<PictureArrayBean> picture_array) {
		this.picture_array = picture_array;
	}

	public static class PictureArrayBean {
		/**
		 * url : https://vmse.nutizen.asia/devapi/media/img/20180327143238_default_img.jpg?advertisement=42&advertisers=15&plan=9
		 * ad_id : 42
		 * position : TopLeft
		 * delay : 1
		 * time : 1
		 */

		private String url;
		private int ad_id;
		private String position;
		private int delay;
		private int time;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getAd_id() {
			return ad_id;
		}

		public void setAd_id(int ad_id) {
			this.ad_id = ad_id;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public int getDelay() {
			return delay;
		}

		public void setDelay(int delay) {
			this.delay = delay;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
	}
}
