package com.ultime5528.util;

public class CubicInterpolator {

	private double courbure, deadzoneY, deadzoneX;

	/**
	 * 
	 * @param courbure  Définit le niveau de courbure. 0 : DROIT => 1 : COURBE MAXIMALE
	 * @param deadzoneY Définit le niveau minimal en Y (habituellement: vitesse). 
	 * @param deadzoneX Définit le niveau minimal X (habituellement: poussée sur le joystick).
	 */
	public CubicInterpolator(double courbure, double deadzoneY, double deadzoneX) {

		this.courbure = courbure;
		this.deadzoneY = deadzoneY;
		this.deadzoneX = deadzoneX;
	}

	public double interpolate(double valeur) {

		if (valeur >= deadzoneX) {

			return deadzoneY
					+ (1 - deadzoneY) * (courbure * valeur * valeur * valeur + (1 - courbure) * valeur);

		} else if (valeur <= -deadzoneX) {

			return -deadzoneY
					+ (1 - deadzoneY) * (courbure * valeur * valeur * valeur + (1 - courbure) * valeur);

		} else {

			return interpolate(deadzoneX) / deadzoneX * valeur;

		}
	}

	public double getCourbure() {
		return courbure;
	}

	public void setCourbure(double courbure) {

		this.courbure = courbure;
	}

	public double getDeadzoneVitesse() {
		return deadzoneY;
	}

	public void setDeadzoneVitesse(double deadzoneVitesse) {
		this.deadzoneY = deadzoneVitesse;
	}

	public double getDeadzoneJoystick() {
		return deadzoneX;
	}

	public void setDeadzoneJoystick(double deadzoneJoystick) {
		this.deadzoneX = deadzoneJoystick;
	}

}