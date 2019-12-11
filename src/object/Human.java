package object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import animation.SpriteHorizontal;

public class Human {
	private int coordX;
	private int coordY;
	private boolean show;
	private boolean inElevator;
	private int floorStart;
	private int floorFinish;
	private Building building;
	private ArrayList<SpriteHorizontal> listHumanGoInSprite;
	private ArrayList<SpriteHorizontal> listHumanGoOutSprite;
	private SpriteHorizontal humanGoInSprite;
	private SpriteHorizontal humanGoOutSprite;
	private SpriteHorizontal humanSprite;
	private boolean stopHuman;
	private boolean hasNextHuman;

	public Human(int floorStart, int floorFinish, Building building) throws IOException {
		this.floorStart = floorStart;
		stopHuman = false;
		hasNextHuman = false;
		this.floorFinish = floorFinish;
		this.building = building;
		this.show = true;
		this.inElevator = false;
		setStartCoord();
		listHumanGoInSprite = new ArrayList<>();
		listHumanGoInSprite.add(new SpriteHorizontal("humangoin1.png", 12, 200));
		listHumanGoInSprite.add(new SpriteHorizontal("humangoin2.png", 12, 200));
		listHumanGoInSprite.add(new SpriteHorizontal("humangoin3.png", 12, 200));
		listHumanGoInSprite.add(new SpriteHorizontal("humangoin4.png", 12, 200));
		listHumanGoInSprite.add(new SpriteHorizontal("humangoin5.png", 12, 200));
		listHumanGoInSprite.add(new SpriteHorizontal("humangoin6.png", 12, 200));
		listHumanGoOutSprite = new ArrayList<>();
		listHumanGoOutSprite.add(new SpriteHorizontal("humangoout1.png", 12, 200));
		listHumanGoOutSprite.add(new SpriteHorizontal("humangoout2.png", 12, 200));
		listHumanGoOutSprite.add(new SpriteHorizontal("humangoout3.png", 12, 200));
		listHumanGoOutSprite.add(new SpriteHorizontal("humangoout4.png", 12, 200));
		listHumanGoOutSprite.add(new SpriteHorizontal("humangoout5.png", 12, 200));
		listHumanGoOutSprite.add(new SpriteHorizontal("humangoout6.png", 12, 200));
		humanGoInSprite = listHumanGoInSprite.get(floorFinish - 1);
		humanGoOutSprite = listHumanGoOutSprite.get(floorFinish - 1);
		humanSprite = humanGoInSprite;
	}

	public void setCoord() {
		Elevator elevator = building.getElevator();
		if (!isInElevator()) {                           
			if (getCoordYStart() == coordY) {
				if (coordX < 500) {                  //di cho den khi vao khu vuc cho 
					coordX++;
					humanSprite = humanGoInSprite;
				} else {                              //vao roi thi dung lai
					stopHuman = true;
				}
			} 
			if (coordX >= 500) {                        // dang o khu cho hoac dang di vao
				if (elevator.isStop() && elevator.isOpenning()) {
					if (getCoordYStart() == elevator.getCoordY()) {
						if (getCoorYFinish() < elevator.getCoordY() && elevator.isMovingUp()) {
							if (coordX <= 595 - elevator.numberHumanIn() * 10) {       //di sau vao trong 
								if (!elevator.isFull()) {             
									coordX++;
									stopHuman = false;
								}
							} else {                             //dung lai 
								this.setInElevator(true);
								stopHuman = true;
							}
						}
						if (getCoorYFinish() > elevator.getCoordY() && elevator.isMovingDown()) { //tuong tu
							if (coordX <= 595 - elevator.numberHumanIn() * 10) {
								if (!elevator.isFull()) {
									coordX++;
									stopHuman = false;
								}
							} else {
								this.setInElevator(true);
								stopHuman = true;
							}
						}
					}
				}
			}
			if (getCoorYFinish() == coordY) { //dang o ngoai thang may, dang di ra thi di ra khoi man hinh roi bien mat
				if (coordX > 0) {
					stopHuman = false;
					coordX--;
					humanSprite = humanGoOutSprite;
				} else {
					this.show = false;
				}
			}

		}
		if (isInElevator()) {     
			this.coordY = elevator.getCoordY();
			if (elevator.isStop()) {   
				if (getCoorYFinish() == elevator.getCoordY()) { //den tang ket thuc
					if (coordX >= 500) {                 
						if (elevator.isOpenning()) { //dang ben trong va thang may mo cua thi di ra
							coordX--;
							stopHuman = false;
							humanSprite = humanGoOutSprite;
						}
					} else {
						setInElevator(false);          
					}
				}
			}
		}

	}

	public void setHasNextHuman() {
		ArrayList<Human> listHuman = building.getListHuman();
		hasNextHuman = false;
		int distance = 40;
		if (coordX >= 400) {
			distance = 10;
		}
		if (this.getCoordY() == this.getCoordYStart()) {
			for (int i = 0; i < listHuman.size(); i++) {
				if (listHuman.get(i).getCoordY() == this.getCoordY()) {   // nhung nguoi dang chung tang voi nguoi hien tai
					if (listHuman.get(i).getCoordY() == listHuman.get(i).getCoordYStart()) {  //chi xet nhung nguoi dang o sanh doi
						if (listHuman.get(i).getCoordX() < (this.getCoordX() + distance)
								&& listHuman.get(i).getCoordX() > this.getCoordX()) {
							hasNextHuman = true;
							if (coordX > 595 - building.getElevator().numberHumanIn() * 10) {
								this.setInElevator(true);
								stopHuman = true;
							}
						}
					}
				}
			}
		} else if (this.getCoordY() == this.getCoorYFinish()) {
			for (int i = 0; i < listHuman.size(); i++) {
				if (listHuman.get(i).getCoordY() == this.getCoordY()) {
					if (listHuman.get(i).getCoordY() == listHuman.get(i).getCoorYFinish()) {
						if (listHuman.get(i).getCoordX() > (this.getCoordX() - distance)
								&& listHuman.get(i).getCoordX() < this.getCoordX()) {
							hasNextHuman = true;

						}
					}
				}
			}

		}

	}

	public boolean isHasNextHuman() {
		return hasNextHuman;
	}

	private void setStartCoord() {
		coordX = 0;
		coordY = getCoordYStart();
	}

	public int getCoordX() {
		return coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public int getFloorStart() {
		return floorStart;
	}

	public int getFloorFinish() {
		return floorFinish;
	}

	public int getCoordYStart() {
		return (building.getNumberFloors() - floorStart + 1) * (building.getHeight() / building.getNumberFloors());
	}

	public int getCoorYFinish() {
		return (building.getNumberFloors() - floorFinish + 1) * (building.getHeight() / building.getNumberFloors());
	}

	public boolean isShow() {
		return show;
	}

	public boolean isInElevator() {
		return inElevator;
	}

	public void setInElevator(boolean inElevator) {
		this.inElevator = inElevator;
	}

	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		if (!stopHuman) {
			if (!hasNextHuman) {
				humanSprite.nextFrame();
			}
		}
		g2d.drawImage(humanSprite.getListFrame().get(humanSprite.getCurrentFrame()).getImageFrame(),
				coordX - humanSprite.getWidth(), coordY - humanSprite.getHeight(), null);
	}

}
