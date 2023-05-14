package action.fieldObjects;

import java.util.ArrayList;

import action.game.HitLine;
import action.game.HitRange;
import action.game.Player;

public abstract class HitBoxObject extends SpecialObject {
	protected double vx;
	protected double vy;
	protected double directryVX;
	protected double directryVY;
	protected boolean isDead;

	protected boolean suddenlyChange;

	protected ArrayList<Integer[]> hitCells;
	protected ArrayList<Object[]> hitSos;
	protected ArrayList<Integer[]> overlapCells;
	protected ArrayList<SpecialObject> overlapSos;

	protected double[][] hitPoints;
	public static final double[][] hitPointsNull = {};
	public static final double[][] hitPoints40_40 = { { 20, 20 }, { 20, -20 }, { -20, 20 }, { -20, -20 } };
	public static final double[][] hitPoints40_80 = { { 20, 40 }, { 20, 0 }, { 20, -40 }, { -20, 40 }, { -20, 0 },
			{ -20, -40 } };
	public static final double[][] hitPoints80_80 = { { 40, 40 }, { 40, 0 }, { 40, -40 }, { -40, 40 }, { -40, 0 },
			{ -40, -40 }, { 0, 40 }, { 0, -40 } };

	public HitBoxObject(double x, double y, double width, double height, double[][] hitPoints) {
		super(x, y, width, height);
		vx = 0;
		vy = 0;
		directryVX = 0;
		directryVY = 0;
		isDead = false;
		this.hitPoints = hitPoints;
	}

	public double getVx() {
		return vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public void setDirectryVX(double directryVX) {
		this.directryVX = directryVX;
	}

	public void setDirectryVY(double directryVY) {
		this.directryVY = directryVY;
	}

	public boolean isDead() {
		return isDead;
	}

	public void attacked() {
		this.isDead = true;
	}

	protected void moveAndAdjust() {
		double planX = x + vx + directryVX;
		double planY = y + vy + directryVY;

		directryVX = 0;
		directryVY = 0;

		this.suddenlyChange = false;
		double[] veerPlan;

		while ((veerPlan = hitVeer(planX, planY)) != null) {
			planX = veerPlan[0];
			planY = veerPlan[1];

			hitReaction();

			hitCells = null;
			hitSos = null;

			if (suddenlyChange) {
				break;
			}
		}

		int checkStartX = (int) (this.x - this.width) / 48;
		int checkStartY = (int) (this.y - this.height) / 48;
		int checkEndX = (int) (this.x + this.width) / 48;
		int checkEndY = (int) (this.y + this.height) / 48;

		for (int i = 0; i < checkEndX - checkStartX + 1; i++) {
			for (int j = 0; j < checkEndY - checkStartY + 1; j++) {
				FieldCell fc = game.getField(checkStartX + i - (int) game.getCameraX() / 48 + 7,
						checkStartY + j - (int) game.getCameraY() / 48 + 7);
				if (fc != null) {
					if (this.x - this.width / 2 < (checkStartX + i) * 48 + 48
							&& this.x + this.width / 2 > (checkStartX + i) * 48 &&
							this.y - this.height / 2 < (checkStartY + j) * 48 + 48
							&& this.y + this.height / 2 > (checkStartY + j) * 48) {
						if (overlapCells == null) {
							overlapCells = new ArrayList<Integer[]>();
						}
						overlapCells.add(new Integer[] { checkStartX + i - (int) game.getCameraX() / 48 + 7,
								checkStartY + j - (int) game.getCameraY() / 48 + 7 });
					}
				}
			}
		}

		for (SpecialObject so : game.getSos()) {
			if (so != this && so != null) {
				if (this.x - this.width / 2 < so.getX() + so.getWidth() / 2
						&& this.x + this.width / 2 > so.getX() - so.getWidth() / 2 &&
						this.y - this.height / 2 < so.getY() + so.getHeight() / 2
						&& this.y + this.height / 2 > so.getY() - so.getHeight() / 2) {
					if (overlapSos == null) {
						overlapSos = new ArrayList<SpecialObject>();
					}
					overlapSos.add(so);
				}
			}
		}

		overlapReaction();

		hitCells = null;
		hitSos = null;
		overlapCells = null;
		overlapSos = null;

		if (y < -300) {
			this.isDead = true;
		}
	}

	/**
	 * hitVeer(double, double)は移動先予定の場所のx座標とy座標を指定し
	 * ブロックにぶつかる場合は判定し位置をずらします。
	 *
	 * @param planX
	 * @param planY
	 * @return double[0]にずれる先のx座標、double[1]にずれる先のy座標(ずれる必要のない場合はnullを返す);
	 */
	protected double[] hitVeer(double planX, double planY) {
		if (this.x != planX || this.y != planY) {
			int checkStartX = (int) (Math.min(this.x, planX) - this.width / 2) / 48;
			int checkStartY = (int) (Math.min(this.y, planY) - this.height / 2) / 48;
			int checkEndX = (int) (Math.max(this.x, planX) + this.width / 2) / 48;
			int checkEndY = (int) (Math.max(this.y, planY) + this.height / 2) / 48;

//			if(planX == 651 && planY == 155) {
//				System.out.println("( " + checkStartX + " , " + checkStartY + " ) ->"
//						+ "( " + checkEndX + " , " + checkEndY + " )");
//			}

			double shortestRatio = 1.1;
			double veerPlanX = -1.0;
			double veerPlanY = -1.0;
			double wallD = -1.0;
			double adjustX = 0;
			double adjustY = 0;

			for (int i = 0; i < checkEndX - checkStartX + 1; i++) {
				for (int j = 0; j < checkEndY - checkStartY + 1; j++) {
					FieldCell fc = game.getField(checkStartX + i - (int) game.getCameraX() / 48 + 7,
							checkStartY + j - (int) game.getCameraY() / 48 + 7);
					if (fc != null) {
						double[] hitInfo = hitPlaceAboutFieldObj(fc, planX, planY,
								(checkStartX + i) * 48, (checkStartY + j) * 48);

						if (hitInfo[0] < shortestRatio) {
							shortestRatio = hitInfo[0];
							veerPlanX = hitInfo[1];
							veerPlanY = hitInfo[2];
							wallD = hitInfo[3];
							adjustX = hitInfo[4];
							adjustY = hitInfo[5];
							hitCells = new ArrayList<Integer[]>();
							hitCells.add(new Integer[] { checkStartX + i - (int) game.getCameraX() / 48 + 7,
									checkStartY + j - (int) game.getCameraY() / 48 + 7, (int) hitInfo[6] });
						} else if (hitInfo[0] == shortestRatio && shortestRatio != 1.1) {
							adjustX += hitInfo[4];
							adjustY += hitInfo[5];
							hitCells.add(new Integer[] { checkStartX + i - (int) game.getCameraX() / 48 + 7,
									checkStartY + j - (int) game.getCameraY() / 48 + 7, (int) hitInfo[6] });
						}

//						if (planX == 651 && planY == 155 && hitInfo[0] == 0.0) {
//							System.out.println(checkStartX + i - (int) game.getCameraX() / 48 + 7);
//							System.out.println(checkStartY + j - (int) game.getCameraY() / 48 + 7);
//						}
					}
				}
			}

			for (SpecialObject so : game.getSos()) {
				if (so != this && so != null) {
					double[] hitInfo = hitPlaceAboutFieldObj(so, planX, planY, so.getX() - so.getWidth() / 2,
							so.getY() - so.getHeight() / 2);
					if (hitInfo[0] < shortestRatio) {
						shortestRatio = hitInfo[0];
						veerPlanX = hitInfo[1];
						veerPlanY = hitInfo[2];
						wallD = hitInfo[3];
						adjustX = hitInfo[4];
						adjustY = hitInfo[5];
						hitSos = new ArrayList<Object[]>();
						hitSos.add(new Object[] { so, (int) hitInfo[6] });
					} else if (hitInfo[0] == shortestRatio && shortestRatio != 1.1) {
						adjustX += hitInfo[4];
						adjustY += hitInfo[5];
						if (hitSos == null) {
							hitSos = new ArrayList<Object[]>();
						}
						hitSos.add(new Object[] { so, (int) hitInfo[6] });
					}
				}
			}

			if (!(this instanceof Player)) {
				Player player = game.getPlayer();
				double[] hitInfo = hitPlaceAboutFieldObj(player, planX, planY,
						player.getX() - player.getWidth() / 2, player.getY() - player.getHeight() / 2);
				if (hitInfo[0] < shortestRatio) {
					shortestRatio = hitInfo[0];
					veerPlanX = hitInfo[1];
					veerPlanY = hitInfo[2];
					wallD = hitInfo[3];
					adjustX = hitInfo[4];
					adjustY = hitInfo[5];
					hitSos = new ArrayList<Object[]>();
					hitSos.add(new Object[] { player, (int) hitInfo[6] });
				} else if (hitInfo[0] == shortestRatio && shortestRatio != 1.1) {
					adjustX += hitInfo[4];
					adjustY += hitInfo[5];
					if (hitSos == null) {
						hitSos = new ArrayList<Object[]>();
					}
					hitSos.add(new Object[] { player, (int) hitInfo[6] });
				}
			} else {
				for (int i = 0; i < hitPoints.length; i++) {
					double[] hitInfo = hitPlaceAboutLine(HitRange.mostLeftLine, this.x + hitPoints[i][0],
							this.y + hitPoints[i][1], planX + hitPoints[i][0], planY + hitPoints[i][1],
							game.getCameraX(), game.getCameraY());
					if (hitInfo[0] < shortestRatio) {
						shortestRatio = hitInfo[0];
						veerPlanX = hitInfo[1] - hitPoints[i][0];
						veerPlanY = hitInfo[2] - hitPoints[i][1];
						wallD = hitInfo[3];
						adjustX = hitInfo[4];
						adjustY = hitInfo[5];
					} else if (hitInfo[0] == shortestRatio && shortestRatio != 1.1) {
						adjustX += hitInfo[4];
						adjustY += hitInfo[5];
					}
				}
			}

			if (shortestRatio != 1.1) {
				double gotoX = this.x + (planX - this.x) * shortestRatio;
				double gotoY = this.y + (planY - this.y) * shortestRatio;

				this.x = gotoX + Math.signum(adjustX);
				this.y = gotoY + Math.signum(adjustY);

				if((wallD + 45) % 180 <= 90) {
					if(vy > 0 || (wallD + 45) % 360 < 180) {
						vy = 0;
					}
				} else {
					vx = 0;
				}

				return new double[] { veerPlanX, veerPlanY };
			} else {
				this.x = planX;
				this.y = planY;
				return null;
			}
		}
		return null;
	}

	/**
	 * 当たり判定と移動先予定の場所のx座標とy座標を指定し
	 * 判定しずらす位置を返します。
	 *
	 * @param hitRange
	 * @param planX
	 * @param planY
	 * @return	double[0]にぶつかる位置、double[1]にずれる先のx座標、double[2]にずれる先のy座標
	 * 			double[3]にぶつかった壁の傾き、double[4]にxのadjust値、double[5]にyのadjust値、
	 * 			double[6]にぶつかる方向(0.0 : 上 1.0 : 右 2.0 : 下 3.0 : 左)
	 */
	private double[] hitPlaceAboutFieldObj(FieldObject fo, double planX, double planY, double blockX, double blockY) {
		double shortestRatio = 1.1;
		double veerPlanX = -1.0;
		double veerPlanY = -1.0;
		double wallD = -1.0;
		double adjustX = 0;
		double adjustY = 0;
		int hitDirection = -1;

		for (int i = 0; i < fo.getHitRange().hitLines.length; i++) {
			for (int j = 0; j < hitPoints.length; j++) {
				double moveLineX = hitPoints[j][0];
				double moveLineY = hitPoints[j][1];
				boolean mustDo = false;

				HitLine hitLine = fo.getHitRange().hitLines[i];
				if ((hitPoints[j][0] == this.width / 2 && hitLine.startY < hitLine.endY) ||
						hitPoints[j][0] == -this.width / 2 && hitLine.startY > hitLine.endY ||
						hitPoints[j][1] == this.height / 2 && hitLine.startX > hitLine.endX ||
						hitPoints[j][1] == -this.height / 2 && hitLine.startX < hitLine.endX) {
					mustDo = true;
				}

				if (mustDo) {
					double[] hitInfo = hitPlaceAboutLine(hitLine, this.x + moveLineX,
							this.y + moveLineY, planX + moveLineX, planY + moveLineY, blockX, blockY);
					if (hitInfo[0] < shortestRatio) {
						shortestRatio = hitInfo[0];
						veerPlanX = hitInfo[1] - moveLineX;
						veerPlanY = hitInfo[2] - moveLineY;
						wallD = hitInfo[3];
						adjustX = hitInfo[4];
						adjustY = hitInfo[5];
						//						if(wallA != 0 && wallA != 99) {
						//							System.out.println(wallA);
						//						}
						if ((wallD + 45) % 180 <= 90) {
							if ((wallD + 45) % 360 < 180) {
								hitDirection = 2;
							} else {
								hitDirection = 0;
							}
						} else {
							if ((wallD - 45) % 360 < 180) {
								hitDirection = 3;
							} else {
								hitDirection = 1;
							}
						}
					} else if (hitInfo[0] == shortestRatio) {
						adjustX += hitInfo[4];
						adjustY += hitInfo[5];
					}
				}
			}
		}
		return new double[] { shortestRatio, veerPlanX, veerPlanY, wallD, adjustX, adjustY, hitDirection };
	}

	/**
	 * 当たり判定の基準線と
	 * 移動先予定の場所のx座標とy座標を指定し
	 * 判定しずらす位置を返します。
	 *
	 * @param hitLine
	 * @param planX
	 * @param planY
	 * @return double[0]にぶつかる位置、double[1]にずれる先のx座標、double[2]にずれる先のy座標、
	 *          double[3]に壁の傾き、double[4]にxのadjust値、double[5]にyのadjust値
	 */
	private double[] hitPlaceAboutLine(HitLine hitLine, double startX, double startY, double endX, double endY,
			double lineX, double lineY) {
		double[] place = intersection(lineX + hitLine.startX, lineY + hitLine.startY, lineX + hitLine.endX,
				lineY + hitLine.endY, startX, startY, endX, endY, true);
//		System.out.println((lineX + hitLine.startX) + " " + (lineY + hitLine.startY) + " " + (lineX + hitLine.endX)
//				+ " " + (lineY + hitLine.endY) + " " + startX + " " + startY + " " + endX + " " + endY);
		if (place != null) {
			double ratio;
			if (startX - endX == 0 || (startY - endY) / (startX - endX) > 1) {
				ratio = Math.abs(place[1] - startY) / Math.abs(startY - endY);
			} else {
				ratio = Math.abs(place[0] - startX) / Math.abs(startX - endX);
			}

			//			if (ratio != 1.1 && ratio > 1) {
			//				System.out.println(Math.abs(place[1] - startY) + " " + Math.abs(startY - endY));
			//				if (this instanceof Player) {
			//				}
			//			}

			double wallD = Math.atan((hitLine.endY - hitLine.startY) / (hitLine.endX - hitLine.startX)) * 180 / Math.PI;
			if(hitLine.startX > hitLine.endX) {
				wallD += 180;
			} else if(hitLine.startY > hitLine.endY) {
				wallD += 360;
			}
			double adjustX;
			double adjustY;
			if (hitLine.startX == hitLine.endX) {
				if (hitLine.startY < hitLine.endY) {
					if (startX > lineX + hitLine.startX) {
						return new double[] { 1.1, -1.0, -1.0, -1.0, 0, 0 };
					}
				} else {
					if (startX < lineX + hitLine.startX) {
						return new double[] { 1.1, -1.0, -1.0, -1.0, 0, 0 };
					}
				}
				adjustY = 0;
				adjustX = (hitLine.startY > hitLine.endY) ? 1 : -1;
			} else if (hitLine.startY == hitLine.endY) {
				if (hitLine.startX < hitLine.endX) {
					if (startY < lineY + hitLine.startY) {
						return new double[] { 1.1, -1.0, -1.0, -1.0, 0, 0 };
					}
				} else {
					if (startY > lineY + hitLine.startY) {
						return new double[] { 1.1, -1.0, -1.0, -1.0, 0, 0 };
					}
				}
				adjustX = 0;
				adjustY = (hitLine.startX > hitLine.endX) ? -1 : 1;
			} else {
				if (hitLine.startX < hitLine.endX) {
					double[] inter = intersection(lineX + hitLine.startX, lineY + hitLine.startY, lineX + hitLine.endX,
							lineY + hitLine.endY, startX, startY, startX, startY + 1, false);
					if (startY < inter[1]) {
						return new double[] { 1.1, -1.0, -1.0, -1.0, 0, 0 };
					}
					adjustY = 1;
					adjustX = (wallD < 180) ? -1 : 1;
				} else {
					double[] inter = intersection(lineX + hitLine.startX, lineY + hitLine.startY, lineX + hitLine.endX,
							lineY + hitLine.endY, startX, startY, startX, startY + 1, false);
					if (startY > inter[1]) {
						return new double[] { 1.1, -1.0, -1.0, -1.0, 0, 0 };
					}
					adjustY = -1;
					adjustX = (wallD < 180) ? -1 : 1;
				}
			}


			// ここからveer処理

			double[] placeVeer;
			if (45 < wallD % 180 && 135 > wallD % 180) {
				placeVeer = intersection(lineX + hitLine.startX, lineY + hitLine.startY, lineX + hitLine.endX,
						lineY + hitLine.endY, endX + 1, endY, endX, endY, false);
			} else {
				placeVeer = intersection(lineX + hitLine.startX, lineY + hitLine.startY, lineX + hitLine.endX,
						lineY + hitLine.endY, endX, endY + 1, endX, endY, false);
			}

			if (hitLine.startX == hitLine.endX) {
				if (hitLine.startY > hitLine.endY) {
					placeVeer[0] += 1;
				} else {
					placeVeer[0] += -1;
				}
			} else {
				if (hitLine.startX > hitLine.endX) {
					placeVeer[1] += -1;
				} else {
					placeVeer[1] += 1;
				}
			}
			return new double[] { ratio, placeVeer[0], placeVeer[1], wallD, adjustX, adjustY };
		} else {
			return new double[] { 1.1, -1.0, -1.0, -1.0, 0, 0 };
		}
	}

	/**
	 * 2つの線分の交点を返します。
	 *
	 * @param startX1
	 * @param startY1
	 * @param endX1
	 * @param endY1
	 * @param startX2
	 * @param startY2
	 * @param endX2
	 * @param endY2
	 * @param b 線分として処理し変域を考えるか
	 * @return double[0]に交点のx座標、double[1]に交点のy座標（交点がない場合はnull）。
	 */
	public static double[] intersection(double startX1, double startY1, double endX1, double endY1, double startX2,
			double startY2, double endX2, double endY2, boolean b) {
		double A1 = (startY1 - endY1) / (startX1 - endX1);
		double A2 = (startY2 - endY2) / (startX2 - endX2);
		double B1 = startY1 - A1 * startX1;
		double B2 = startY2 - A2 * startX2;
		//		System.out.println("A : y = " + A1 + "x + " + B1 + " B : y = " + A2 + "x + " + B2);
		if (startX1 - endX1 == 0) {
			double pointY = A2 * startX1 + B2;
			if (!b) {
				return new double[] { startX1, pointY };
			}
			if ((startX1 >= startX2 && startX1 <= endX2) || (startX1 <= startX2 && startX1 >= endX2)) {
				if ((pointY >= startY1 && pointY <= endY1) || (pointY <= startY1 && pointY >= endY1)) {
					return new double[] { startX1, pointY };
				}
			}
			return null;
		} else if (startX2 - endX2 == 0) {
			double pointY = A1 * startX2 + B1;
			if (!b) {
				return new double[] { startX2, pointY };
			}
			if ((startX2 >= startX1 && startX2 <= endX1) || (startX2 <= startX1 && startX2 >= endX1)) {
				if ((pointY >= startY2 && pointY <= endY2) || (pointY <= startY2 && pointY >= endY2)) {
					return new double[] { startX2, pointY };
				}
			}
			return null;
		}
		if (A1 == A2) {
			return null;
		}
		double X = (B2 - B1) / (A1 - A2);
		double Y = A1 * X + B1;
		if (!b) {
			return new double[] { X, Y };
		}
		if ((startX1 <= X && endX1 >= X) || (startX1 >= X && endX1 <= X)) {
			if ((startX2 <= X && endX2 >= X) || (startX2 >= X && endX2 <= X)) {
				return new double[] { X, Y };
			}
		}
		return null;
	}

	protected void hitReaction() {
		if (hitCells != null) {
			for (int i = 0; i < hitCells.size(); i++) {
				game.addTouch(this, hitCells.get(i)[0], hitCells.get(i)[1], hitCells.get(i)[2]);
			}
		}

		if (hitSos != null) {
			for (Object[] hitSo : hitSos) {
				game.addTouch(this, (SpecialObject) (hitSo[0]), (int) (hitSo[1]));
			}
		}
	}

	protected void overlapReaction() {
		if (overlapCells != null) {
			for (Integer[] overlapCell : overlapCells) {
				game.addOverlap(this, overlapCell[0], overlapCell[1]);
			}
		}

		if (overlapSos != null) {
			for (SpecialObject so : overlapSos) {
				game.addOverlap(this, so);
			}
		}
	}

	public abstract void touched(FieldCell fc, int direction);

	public abstract void overlaped(FieldCell fc);

	public void die() {
		if (!(this instanceof Player)) {
			game.addDeletes(this);
		}
	}
}
