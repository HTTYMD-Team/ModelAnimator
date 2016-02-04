package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import animation.Animation;

public class ModelHelper implements Serializable {
	private static final long serialVersionUID = 1L;

	private int texH;
	private int texV;

	private Component head;
	private List<Component> legs = new ArrayList<Component>();
	private Component wing;

	private Animation idle;
	private Animation flying;

	public boolean isHead(Component component) {
		return head == component;
	}

	public boolean isLeg(Component component) {
		return legs.contains(component);
	}

	public boolean isWing(Component component) {
		return wing == component;
	}

	public Component getHead() {
		return head;
	}

	public void setHead(Component component) {
		head = component;
	}

	public List<Component> getLegs() {
		return legs;
	}

	public void setLeg(Component component) {
		legs.add(component);
	}

	public void removeLeg(Component component) {
		legs.remove(component);
	}

	public Component getWing() {
		return wing;
	}

	public void setWing(Component component) {
		wing = component;
	}

	public int getTexH() {
		return texH;
	}

	public void setTexH(int texH) {
		this.texH = texH;
	}

	public int getTexV() {
		return texV;
	}

	public void setTexV(int texV) {
		this.texV = texV;
	}

	public Animation getIdle() {
		return idle;
	}

	public void setIdle(Animation idle) {
		this.idle = idle;
	}

	public Animation getFlying() {
		return flying;
	}

	public void setFlying(Animation flying) {
		this.flying = flying;
	}
}
