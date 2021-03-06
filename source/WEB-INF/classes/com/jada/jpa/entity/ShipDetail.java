/*
 * Copyright 2007-2010 JadaSite.

 * This file is part of JadaSite.
 
 * JadaSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JadaSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JadaSite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jada.jpa.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ShipDetail generated by hbm2java
 */
@Entity
@Table(name = "ship_detail")
public class ShipDetail implements java.io.Serializable {

	private static final long serialVersionUID = -8617698974321561341L;
	private Long shipDetailId;
	private Integer seqNum;
	private Integer itemShipQty;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private OrderItemDetail orderItemDetail;
	private ShipHeader shipHeader;

	public ShipDetail() {
	}

	public ShipDetail(Integer seqNum, String recUpdateBy,
			Date recUpdateDatetime, String recCreateBy, Date recCreateDatetime) {
		this.seqNum = seqNum;
		this.recUpdateBy = recUpdateBy;
		this.recUpdateDatetime = recUpdateDatetime;
		this.recCreateBy = recCreateBy;
		this.recCreateDatetime = recCreateDatetime;
	}

	public ShipDetail(Integer seqNum, Integer itemShipQty, String recUpdateBy,
			Date recUpdateDatetime, String recCreateBy, Date recCreateDatetime,
			OrderItemDetail orderItemDetail, ShipHeader shipHeader) {
		this.seqNum = seqNum;
		this.itemShipQty = itemShipQty;
		this.recUpdateBy = recUpdateBy;
		this.recUpdateDatetime = recUpdateDatetime;
		this.recCreateBy = recCreateBy;
		this.recCreateDatetime = recCreateDatetime;
		this.orderItemDetail = orderItemDetail;
		this.shipHeader = shipHeader;
	}

	@Id
	@GeneratedValue
	@Column(name = "ship_detail_id", nullable = false)
	public Long getShipDetailId() {
		return this.shipDetailId;
	}

	public void setShipDetailId(Long shipDetailId) {
		this.shipDetailId = shipDetailId;
	}

	@Column(name = "seq_num", nullable = false)
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	@Column(name = "item_ship_qty")
	public Integer getItemShipQty() {
		return this.itemShipQty;
	}

	public void setItemShipQty(Integer itemShipQty) {
		this.itemShipQty = itemShipQty;
	}

	@Column(name = "rec_update_by", nullable = false, length = 20)
	public String getRecUpdateBy() {
		return this.recUpdateBy;
	}

	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rec_update_datetime", nullable = false)
	public Date getRecUpdateDatetime() {
		return this.recUpdateDatetime;
	}

	public void setRecUpdateDatetime(Date recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}

	@Column(name = "rec_create_by", nullable = false, length = 20)
	public String getRecCreateBy() {
		return this.recCreateBy;
	}

	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "rec_create_datetime", nullable = false)
	public Date getRecCreateDatetime() {
		return this.recCreateDatetime;
	}

	public void setRecCreateDatetime(Date recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item_detail_id")
	public OrderItemDetail getOrderItemDetail() {
		return this.orderItemDetail;
	}

	public void setOrderItemDetail(OrderItemDetail orderItemDetail) {
		this.orderItemDetail = orderItemDetail;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ship_header_id")
	public ShipHeader getShipHeader() {
		return this.shipHeader;
	}

	public void setShipHeader(ShipHeader shipHeader) {
		this.shipHeader = shipHeader;
	}

}
